package raccoon.neuralnetwork.core;

import raccoon.neuralnetwork.Signal;

import java.util.HashSet;
import java.util.Set;

public class Output implements Receiver {
    private final Set<Link> incomingLinks = new HashSet<>();
    private final ActivationFunction activationFunction;

    public Output(ActivationFunction activationFunction) {
        this.activationFunction = activationFunction;
    }

    Signal signal() {
        return activationFunction.onSignal(totalIncomingSignal());
    }

    private Signal totalIncomingSignal() {
        return incomingLinks.stream()
                .map(Link::outgoingSignal)
                .collect(Signal.sum());
    }

    @Override
    public void linkWithEmitter(Link emitter) {
        incomingLinks.add(emitter);
    }
}
