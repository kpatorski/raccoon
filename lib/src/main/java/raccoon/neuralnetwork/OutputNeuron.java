package raccoon.neuralnetwork;

import java.util.HashSet;
import java.util.Set;

class OutputNeuron implements Receiver {
    private final Set<Link> incomingLinks = new HashSet<>();
    private final ActivationFunction activationFunction;

    OutputNeuron(ActivationFunction activationFunction) {
        this.activationFunction = activationFunction;
    }

    Signal signal() {
        return activationFunction.onSignal(totalIncomingSignal());
    }

    private Signal totalIncomingSignal() {
        return incomingLinks.stream()
                .map(Link::signal)
                .collect(Signal.sum());
    }

    @Override
    public void addInput(Link link) {
        incomingLinks.add(link);
    }
}
