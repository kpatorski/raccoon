package raccoon.neuralnetwork;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import raccoon.neuralnetwork.activationfunction.ActivationFunction;
import raccoon.neuralnetwork.activationfunction.FunctionId;

import java.util.*;
import java.util.stream.Stream;

class OutputLayer {
    private final List<Output> outputs = new ArrayList<>();

    OutputLayer(List<Output> outputs) {
        this.outputs.addAll(outputs);
    }

    static OutputLayer of(long numberOfOutputs,
                          ActivationFunction function,
                          WeightsGenerator weightsGenerator) {
        List<Output> outputs = Stream.generate(() -> new Output(function, weightsGenerator.next()))
                .limit(numberOfOutputs)
                .toList();
        return new OutputLayer(outputs);
    }

    Stream<Output> outputs() {
        return outputs.stream();
    }

    Stream<Signal> signals() {
        return outputs.stream()
                .map(Output::signal);
    }

    Snapshot toSnapshot() {
        return new Snapshot().outputs(outputs.stream().map(Output::toSnapshot).toList());
    }

    static class Output implements Receiver {
        private final Bias bias;
        private final ActivationFunction activationFunction;
        private final Set<Link> incomingLinks = new HashSet<>();

        Output(ActivationFunction activationFunction, Link.Weight biasWeight) {
            this.activationFunction = activationFunction;
            bias = Bias.of(biasWeight);
        }

        private Signal signal() {
            return activateOutput(receiveSignals());
        }

        private Signal receiveSignals() {
            return incomingLinks.stream()
                    .map(Link::potential)
                    .collect(Signal.sum())
                    .add(bias.emit());
        }

        private Signal activateOutput(Signal signal) {
            return activationFunction.onSignal(signal);
        }

        @Override
        public void linkWithEmitter(Link emitter) {
            incomingLinks.add(emitter);
        }

        Snapshot toSnapshot() {
            return new Snapshot(activationFunction.id(), bias.toSnapshot())
                    .incomingLinks(Link.toSnapshot(incomingLinks));
        }

        @Data
        @RequiredArgsConstructor
        static class Snapshot {
            @NonNull
            private final FunctionId activationFunction;
            private final double bias;
            @NonNull
            private List<Link.Snapshot> incomingLinks = new ArrayList<>();

            Snapshot addLink(@NonNull Link.Snapshot link) {
                incomingLinks.add(link);
                return this;
            }
        }
    }

    @Data
    static class Snapshot {
        @NonNull
        private List<Output.Snapshot> outputs = new ArrayList<>();

        Snapshot addOutput(@NonNull Output.Snapshot output) {
            outputs.add(output);
            return this;
        }

        Stream<Link.Snapshot> links() {
            return outputs.stream()
                    .map(Output.Snapshot::incomingLinks)
                    .flatMap(Collection::stream);
        }
    }
}


