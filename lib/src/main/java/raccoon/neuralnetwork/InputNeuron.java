package raccoon.neuralnetwork;

import java.util.HashSet;
import java.util.Set;

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
