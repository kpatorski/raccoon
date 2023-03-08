package raccoon.neural

import spock.lang.Specification

import static raccoon.neural.ActivationFunctionFixtures.linearFunction

class PerceptronTest extends Specification {
    def "incoming signals changes perceptron potential"() {
        given:
        def perceptron = new Perceptron(linearFunction())

        expect:
        perceptron.activate(signals.collect { signal -> Signal.of(signal) })
        perceptron.potential().value() == expectedPotential

        where:
        signals   || expectedPotential
        [-1, -1]  || -2
        [-1]      || -1
        []        || 0
        [1]       || 1
        [1, 1]    || 2
        [1, 2, 3] || 6
    }

}
