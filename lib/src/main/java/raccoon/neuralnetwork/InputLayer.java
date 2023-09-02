package raccoon.neuralnetwork;

import java.util.*;

class InputLayer {
    private final List<Input> inputs = new ArrayList<>();

    void add(Input input) {
        inputs.add(input);
    }

    void emit(List<Signal> signals) {
        emit(signals.iterator());
    }

    private void emit(Iterator<Signal> signal) {
        inputs.forEach(input -> input.emit(signal.next()));
    }

    List<Input> inputs() {
        return inputs;
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
    }
}
