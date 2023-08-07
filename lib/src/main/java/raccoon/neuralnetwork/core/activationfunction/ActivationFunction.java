package raccoon.neuralnetwork.core.activationfunction;

import raccoon.neuralnetwork.Signal;

public interface ActivationFunction {
    FunctionId id();

    Signal onSignal(Signal signal);
}
