package raccoon.neural;

@FunctionalInterface
public interface ActivationFunction {
  Signal onSignal(Signal signal);
}
