package raccoon.neuralnetwork;

class Link {
    private Signal incommingSignal = new Signal(0);
    private Signal outgoingSignal = incommingSignal;
    private Weight weight;

    private Link(Weight weight) {
        this.weight = weight;
    }

    public static Link ofWeight(Weight weight) {
        return new Link(weight);
    }

    void transmit(Signal signal) {
        incommingSignal = signal;
        outgoingSignal = multiply(weight, incommingSignal);
    }

    private static Signal multiply(Weight weight, Signal signal) {
        return signal.multiply(weight.value());
    }

    Signal outgoingSignal() {
        return outgoingSignal;
    }

    Link setWeight(Weight weight) {
        this.weight = weight;
        return this;
    }

    record Weight(double value) {
        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }
}
