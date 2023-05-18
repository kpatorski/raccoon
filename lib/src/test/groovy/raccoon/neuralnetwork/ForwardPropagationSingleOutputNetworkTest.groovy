package raccoon.neuralnetwork

import spock.lang.Specification

import static raccoon.neuralnetwork.ActivationFunctionFixtures.linearFunction
import static raccoon.neuralnetwork.Link.Weight

class SingleNeuronNetworkTest extends Specification {

    def "input neurons transmit signals to output neuron"() {
        given:
        def weights = fixedWeights([0.1d, 0.1d, 0.1d, 0.2d, 0.2d, 0.2d, 0.3d, 0.3d, 0.3d, 0.3d, 0.3d, 0.3d, 0.4d, 0.4d, 0.5d, 0.5d])

        and:
        NeuralNetwork network = BuildNetwork.ofFixedWeights(weights)
                .inputLayer(2)
                .hiddenLayer(3, linearFunction())
                .hiddenLayer(2, linearFunction())
                .outputLayer(2, linearFunction())

        expect:
        network.inputs([signal(input1), signal(input2)]).outputs() == [signal(outputs[0]), signal(outputs[1])]

        where:
        input1 | input2 || outputs
        0.5d   | 1      || [0.20249999999999999d, 0.20249999999999999d]
        1      | 0.5d   || [0.16199999999999998d, 0.16199999999999998d]
        0      | 0      || [0d, 0d]
    }

    private WeightsGenerator fixedWeights(Collection<Double> weights) {
        def iterator = weights.collect { it -> new Weight(it) }.iterator()
        return new WeightsGenerator() {
            @Override
            Weight next() {
                iterator.next()
            }
        }
    }

    private Signal signal(double value) {
        new Signal(value)
    }
}
