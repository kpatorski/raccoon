package raccoon.neuralnetwork;

import raccoon.neuralnetwork.activationfunction.ActivationFunction;

import java.util.*;
import java.util.stream.Stream;

class OutputLayer {
    private final List<Output> outputs = new ArrayList<>();

    void add(Output output) {
        outputs.add(output);
    }

    List<Output> outputs() {
        return outputs;
    }

    Stream<Signal> signals() {
        return outputs.stream()
                .map(Output::signal);
    }

    static class Output implements Receiver {
        private final Set<Link> incomingLinks = new HashSet<>();
        private final ActivationFunction activationFunction;

        Output(ActivationFunction activationFunction) {
            this.activationFunction = activationFunction;
        }

        private Signal signal() {
            return activateOutput(receiveSignals());
        }

        private Signal receiveSignals() {
            return incomingLinks.stream()
                    .map(Link::outgoingSignal)
                    .collect(Signal.sum());
        }

        private Signal activateOutput(Signal signal) {
            return activationFunction.onSignal(signal);
        }

        @Override
        public void linkWithEmitter(Link emitter) {
            incomingLinks.add(emitter);
        }
    }
}


