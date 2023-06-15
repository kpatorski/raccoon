package raccoon.neuralnetwork.core;

import raccoon.neuralnetwork.Signal;

public class Bias implements Emitter {
    private static final Signal SIGNAL = new Signal(1);
    private Link linkToReceiver;

    @Override
    public void linkWithReceiver(Link receiver) {
        this.linkToReceiver = receiver;
    }

    public void emit() {
        linkToReceiver.transmit(SIGNAL);
    }
}
