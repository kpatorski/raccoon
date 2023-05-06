package raccoon.neural;

import java.util.Collection;

record Signal(double value) {

  static Signal of(double value) {
    return new Signal(value);
  }

  static Signal sum(Collection<Signal> signals) {
    return new Signal(signals.stream()
        .mapToDouble(Signal::value)
        .sum());
  }

  @Override
  public String toString() {
    return String.valueOf(value);
  }
}
