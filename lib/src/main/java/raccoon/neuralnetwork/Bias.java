package raccoon.neuralnetwork;

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
