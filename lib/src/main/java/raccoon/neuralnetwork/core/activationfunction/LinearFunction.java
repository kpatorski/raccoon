package raccoon.neuralnetwork.core.activationfunction;

import raccoon.neuralnetwork.Signal;

class LinearFunction implements ActivationFunction {
    LinearFunction() {
    }

    @Override
    public FunctionId id() {
        return FunctionId.LINEAR;
    }

    @Override
    public Signal onSignal(Signal signal) {
        return signal;
    }

    @Override
    public String toString() {
        return id().name();
    }
}
