package raccoon.neuralnetwork;

import java.util.HashSet;
import java.util.Set;

interface NeuronModule {

}

class InputNeuron implements Transmitter {
    private final Set<Link> outgoingLinks = new HashSet<>();

    void transmit(Signal signal) {
        outgoingLinks.forEach(link -> link.transmit(signal));
    }

    @Override
    public void addOutput(Link link) {
        outgoingLinks.add(link);
    }
}

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

interface Receiver {
    void addInput(Link link);
}

interface Transmitter {
    void addOutput(Link link);
}

