package raccoon.neuralnetwork;

import java.util.HashSet;
import java.util.Set;

class Input implements Emitter {
    private final Set<Link> outgoingLinks = new HashSet<>();

    void emit(Signal signal) {
        outgoingLinks.forEach(link -> link.transmit(signal));
    }

    @Override
    public void linkWithReceiver(Link receiver) {
        outgoingLinks.add(receiver);
    }
}

class Neuron implements Emitter, Receiver {
    private final Set<Link> outgoingLinks = new HashSet<>();
    private final Set<Link> incomingLinks = new HashSet<>();
    private final ActivationFunction activationFunction;

    Neuron(ActivationFunction activationFunction) {
        this.activationFunction = activationFunction;
    }

    void emit() {
        Signal signal = activationFunction.onSignal(totalIncomingSignal());
        outgoingLinks.forEach(link -> link.transmit(signal));
    }

    private Signal totalIncomingSignal() {
        return incomingLinks.stream()
                .map(Link::signal)
                .collect(Signal.sum());
    }

    @Override
    public void linkWithReceiver(Link receiver) {
        outgoingLinks.add(receiver);
    }

    @Override
    public void linkWithEmitter(Link emitter) {
        incomingLinks.add(emitter);
    }
}

class Output implements Receiver {
    private final Set<Link> incomingLinks = new HashSet<>();
    private final ActivationFunction activationFunction;

    Output(ActivationFunction activationFunction) {
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
    public void linkWithEmitter(Link emitter) {
        incomingLinks.add(emitter);
    }
}

class Bias implements Emitter {
    private static final Signal SIGNAL = new Signal(1);
    private Link linkToReceiver;

    @Override
    public void linkWithReceiver(Link receiver) {
        this.linkToReceiver = receiver;
    }

    void emit() {
        linkToReceiver.transmit(SIGNAL);
    }
}

interface Receiver {
    void linkWithEmitter(Link emitter);
}

interface Emitter {
    void linkWithReceiver(Link receiver);
}

