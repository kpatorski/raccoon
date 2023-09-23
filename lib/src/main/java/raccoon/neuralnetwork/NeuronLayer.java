package raccoon.neuralnetwork;

import lombok.Data;
import lombok.NonNull;
import raccoon.neuralnetwork.activationfunction.ActivationFunction;
import raccoon.neuralnetwork.activationfunction.FunctionId;

import java.util.*;
import java.util.stream.Stream;

import static java.util.stream.Stream.concat;

class NeuronLayer {
    private final List<Neuron> neurons = new ArrayList<>();

    void add(Neuron neuron) {
        neurons.add(neuron);
    }

    List<Neuron> neurons() {
        return neurons;
    }

    void transmit() {
        neurons.forEach(Neuron::transmit);
    }

    Snapshot toSnapshot() {
        return new Snapshot().neurons(neurons.stream().map(Neuron::toSnapshot).toList());
    }

    static class Neuron implements Emitter, Receiver {
        private final Set<Link> outgoingLinks = new HashSet<>();
        private final Set<Link> incomingLinks = new HashSet<>();
        private final ActivationFunction activationFunction;

        Neuron(ActivationFunction activationFunction) {
            this.activationFunction = activationFunction;
        }

        private void transmit() {
            Signal signal = activateNeuron(receiveSignals());
            outgoingLinks.forEach(link -> link.transmit(signal));
        }

        private Signal activateNeuron(Signal signal) {
            return activationFunction.onSignal(signal);
        }

        private Signal receiveSignals() {
            return incomingLinks.stream()
                    .map(Link::outgoingSignal)
                    .collect(Signal.sum());
        }

        @Override
        public void linkWithReceiver(Link receiver) {
            outgoingLinks.add(receiver);
        }

        @Override
        public void linkWithEmitter(Link emitter) {
            incomingLinks.add(emitter);
        }

        Snapshot toSnapshot() {
            return new Snapshot(activationFunction.id())
                    .incomingLinks(Link.toSnapshot(incomingLinks))
                    .outgoingLinks(Link.toSnapshot(outgoingLinks));
        }

        @Data
        static class Snapshot {
            @NonNull
            private final FunctionId activationFunction;
            @NonNull
            private List<Link.Snapshot> incomingLinks = new ArrayList<>();
            @NonNull
            private List<Link.Snapshot> outgoingLinks = new ArrayList<>();
        }
    }

    @Data
    static class Snapshot {
        @NonNull
        private List<Neuron.Snapshot> neurons = new ArrayList<>();

        Stream<Link.Snapshot> links() {
            return concat(incomingLinks(), outgoingLinks());
        }

        private Stream<Link.Snapshot> incomingLinks() {
            return neurons.stream()
                    .map(Neuron.Snapshot::incomingLinks)
                    .flatMap(Collection::stream);
        }

        private Stream<Link.Snapshot> outgoingLinks() {
            return neurons.stream()
                    .map(Neuron.Snapshot::outgoingLinks)
                    .flatMap(Collection::stream);
        }
    }
}
