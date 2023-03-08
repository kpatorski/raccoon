package raccoon.neural;

import java.util.Collection;

class Perceptron {
  private Signal potential = new Signal(0);
  private final ActivationFunction activationFunction;

  Perceptron(ActivationFunction activationFunction) {
    this.activationFunction = activationFunction;
  }

  Perceptron activate(Collection<Signal> signals) {
    potential = activationFunction.onSignal(Signal.merge(signals));
    return this;
  }

  Signal potential() {
    return potential;
  }
}
