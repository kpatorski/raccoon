package raccoon.neuralnetwork

import raccoon.neuralnetwork.core.ActivationFunction

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
