package raccoon.neuralnetwork;

class Bias {
    private static final Signal SIGNAL = new Signal(1);
    private final Link linkToReceiver;

    private Bias(Link linkToReceiver) {
        this.linkToReceiver = linkToReceiver;
    }

    static Bias of(Link.Weight weight) {
        return new Bias(Link.of(weight));
    }

    Signal emit() {
        return linkToReceiver
                .transmit(SIGNAL)
                .potential();
    }

    double toSnapshot() {
        return linkToReceiver.toSnapshot().weight();
    }
}
