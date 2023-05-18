package raccoon.neuralnetwork;

import java.util.HashSet;
import java.util.Set;

class HiddenNeuron implements Transmitter, Receiver {
    private final Set<Link> outgoingLinks = new HashSet<>();
    private final Set<Link> incomingLinks = new HashSet<>();
    private final ActivationFunction activationFunction;

    HiddenNeuron(ActivationFunction activationFunction) {
        this.activationFunction = activationFunction;
    }

    void transmit() {
        Signal signal = activationFunction.onSignal(totalIncomingSignal());
        outgoingLinks.forEach(link -> link.transmit(signal));
    }

    private Signal totalIncomingSignal() {
        return incomingLinks.stream()
                .map(Link::signal)
                .collect(Signal.sum());
    }

    @Override
    public void addOutput(Link link) {
        outgoingLinks.add(link);
    }

    @Override
    public void addInput(Link link) {
        incomingLinks.add(link);
    }
}
