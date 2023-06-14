package raccoon.neuralnetwork;

class Link {
    private Signal inputSignal;
    private Signal outputSignal;
    private Weight weight;

    private Link(Weight weight) {
        this.weight = weight;
    }

    public static Link ofWeight(Weight weight) {
        return new Link(weight);
    }

    void transmit(Signal signal) {
        inputSignal = signal;
        outputSignal = multiply(weight, inputSignal);
    }

    private static Signal multiply(Weight weight, Signal signal) {
        return signal.multiply(weight.value());
    }

    Signal signal() {
        return outputSignal;
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
