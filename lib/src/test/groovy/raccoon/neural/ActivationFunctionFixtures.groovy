package raccoon.neural

class ActivationFunctionFixtures {
    static ActivationFunction linearFunction() {
        new ActivationFunction() {
            @Override
            Signal onSignal(Signal signal) {
                signal
            }
        }
    }
}
