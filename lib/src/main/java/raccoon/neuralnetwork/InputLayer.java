package raccoon.neuralnetwork;

import lombok.Data;
import lombok.NonNull;

import java.util.*;
import java.util.stream.Stream;

class InputLayer {
    private final List<Input> inputs = new ArrayList<>();

    InputLayer(List<Input> inputs) {
        this.inputs.addAll(inputs);
    }

    static InputLayer of(long amountOfInputs) {
        var inputs = Stream.generate(Input::new)
                .limit(amountOfInputs)
                .toList();
        return new InputLayer(inputs);
    }

    void emit(List<Signal> signals) {
        emit(signals.iterator());
    }

    private void emit(Iterator<Signal> signal) {
        inputs.forEach(input -> input.emit(signal.next()));
    }

    Snapshot toSnapshot() {
        return new Snapshot().inputs(this.inputs.stream().map(Input::toSnapshot).toList());
    }

    <R extends Receiver> void connect(Stream<R> receivers, WeightsGenerator weightsGenerator) {
        receivers.forEach(receiver -> connect(receiver, weightsGenerator));
    }

    private void connect(Receiver receiver, WeightsGenerator weightsGenerator) {
        inputs.forEach(input -> Link.between(input, receiver, weightsGenerator.next()));
    }

    static class Input implements Emitter {
        private final Set<Link> outgoingLinks = new HashSet<>();

        private void emit(Signal signal) {
            outgoingLinks.forEach(link -> link.transmit(signal));
        }

        @Override
        public void linkWithReceiver(Link receiver) {
            outgoingLinks.add(receiver);
        }

        Snapshot toSnapshot() {
            return new Snapshot().outgoingLinks(outgoingLinks.stream().map(Link::toSnapshot).toList());
        }

        @Data
        static class Snapshot {
            @NonNull
            private List<Link.Snapshot> outgoingLinks = new ArrayList<>();

            Snapshot addLink(@NonNull Link.Snapshot link) {
                outgoingLinks.add(link);
                return this;
            }
        }
    }

    @Data
    static class Snapshot {
        @NonNull
        private List<Input.Snapshot> inputs = new ArrayList<>();

        Snapshot addInput(@NonNull Input.Snapshot input) {
            inputs.add(input);
            return this;
        }

        Stream<Link.Snapshot> links() {
            return inputs.stream()
                    .map(Input.Snapshot::outgoingLinks)
                    .flatMap(Collection::stream);
        }
    }
}
