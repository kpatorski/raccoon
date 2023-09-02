package raccoon.neuralnetwork;

import raccoon.neuralnetwork.activationfunction.ActivationFunction;

import java.util.HashSet;
import java.util.Set;

class Neuron implements Emitter, Receiver {
    private final Set<Link> outgoingLinks = new HashSet<>();
    private final Set<Link> incomingLinks = new HashSet<>();
    private final ActivationFunction activationFunction;

    Neuron(ActivationFunction activationFunction) {
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
