package raccoon.neuralnetwork;

@FunctionalInterface
public interface ActivationFunction {
    Signal onSignal(Signal signal);
}
