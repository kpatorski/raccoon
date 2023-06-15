package raccoon.neuralnetwork.core;

import raccoon.neuralnetwork.Signal;

@FunctionalInterface
public interface ActivationFunction {
    Signal onSignal(Signal signal);
}
