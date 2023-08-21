package raccoon.neuralnetwork.core;

import raccoon.neuralnetwork.Signal;
import raccoon.neuralnetwork.core.activationfunction.ActivationFunction;

import java.util.HashSet;
import java.util.Set;

public class Neuron implements Emitter, Receiver {
    private final Set<Link> outgoingLinks = new HashSet<>();
    private final Set<Link> incomingLinks = new HashSet<>();
    private final ActivationFunction activationFunction;

    public Neuron(ActivationFunction activationFunction) {
        this.activationFunction = activationFunction;
    }

    void transmit() {
        Signal signal = activateNeuron(receiveSignals());
        outgoingLinks.forEach(link -> link.transmit(signal));
    }

    private Signal activateNeuron(Signal signal) {
        return activationFunction.onSignal(signal);
    }

    private Signal receiveSignals() {
        return incomingLinks.stream()
                .map(Link::outgoingSignal)
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
