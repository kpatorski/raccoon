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

    NeuronLayer(List<Neuron> neurons) {
        this.neurons.addAll(neurons);
    }

    static NeuronLayer of(long numberOfNeurons,
                          ActivationFunction function,
                          WeightsGenerator weightsGenerator) {
        var neurons = Stream.generate(() -> new Neuron(function, weightsGenerator.next()))
                .limit(numberOfNeurons)
                .toList();
        return new NeuronLayer(neurons);
    }

    <R extends Receiver> void connect(Stream<R> receivers, WeightsGenerator weightsGenerator) {
        receivers.forEach(receiver -> connect(receiver, weightsGenerator));
    }

    private <R extends Receiver> void connect(R receiver, WeightsGenerator weightsGenerator) {
        neurons.forEach(neuron -> Link.between(neuron, receiver, weightsGenerator.next()));
    }

    Stream<Neuron> neurons() {
        return neurons.stream();
    }

    void transmit() {
        neurons.forEach(Neuron::transmit);
    }

    Snapshot toSnapshot() {
        return new Snapshot().neurons(neurons.stream().map(Neuron::toSnapshot).toList());
    }

    static class Neuron implements Emitter, Receiver {
        private final Bias bias;
        private final Set<Link> outgoingLinks = new HashSet<>();
        private final Set<Link> incomingLinks = new HashSet<>();
        private final ActivationFunction activationFunction;

        Neuron(ActivationFunction activationFunction, Link.Weight biasWeight) {
            this.activationFunction = activationFunction;
            bias = Bias.of(biasWeight);
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
                    .map(Link::potential)
                    .collect(Signal.sum())
                    .add(bias.emit());
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
            return new Snapshot(activationFunction.id(), bias.toSnapshot())
                    .incomingLinks(Link.toSnapshot(incomingLinks))
                    .outgoingLinks(Link.toSnapshot(outgoingLinks));
        }

        @Data
        static class Snapshot {
            @NonNull
            private final FunctionId activationFunction;
            private final double bias;
            @NonNull
            private List<Link.Snapshot> incomingLinks = new ArrayList<>();
            @NonNull
            private List<Link.Snapshot> outgoingLinks = new ArrayList<>();

            Snapshot addIncomingLink(@NonNull Link.Snapshot link) {
                incomingLinks.add(link);
                return this;
            }

            Snapshot addOutgoingLink(@NonNull Link.Snapshot link) {
                outgoingLinks.add(link);
                return this;
            }
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

        Snapshot addNeurons(@NonNull Collection<Neuron.Snapshot> neurons) {
            this.neurons.addAll(neurons);
            return this;
        }
    }
}
