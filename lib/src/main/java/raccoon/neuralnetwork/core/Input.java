package raccoon.neuralnetwork.core;

import raccoon.neuralnetwork.Signal;

import java.util.HashSet;
import java.util.Set;

public class Input implements Emitter {
    private final Set<Link> outgoingLinks = new HashSet<>();

    void emit(Signal signal) {
        outgoingLinks.forEach(link -> link.transmit(signal));
    }

    @Override
    public void linkWithReceiver(Link receiver) {
        outgoingLinks.add(receiver);
    }
}
