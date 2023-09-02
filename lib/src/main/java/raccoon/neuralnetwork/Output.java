package raccoon.neuralnetwork;

import raccoon.neuralnetwork.activationfunction.ActivationFunction;

import java.util.HashSet;
import java.util.Set;

class Output implements Receiver {
    private final Set<Link> incomingLinks = new HashSet<>();
    private final ActivationFunction activationFunction;

    Output(ActivationFunction activationFunction) {
        this.activationFunction = activationFunction;
    }

    Signal signal() {
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
