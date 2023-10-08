package raccoon.neuralnetwork

import spock.lang.Specification

import static raccoon.neuralnetwork.NetworkFixture.input
import static raccoon.neuralnetwork.NetworkFixture.output
import static raccoon.neuralnetwork.NetworkFixture.neuron

class ForwardPropagationTest extends Specification {

    def "input neurons transmit signals to the outputs "() {
        given:
        def network = NetworkFixture
                .ofInputs(input("I1"), input("I2"))
                .andOutputs(output("O1", 0.1), output("O2", 0.2))
                .connect("I1", ["O1", "O2"], [0.5d, 1d])
                .connect("I2", ["O1", "O2"], [0.5d, 1d])
                .create()

        expect:
        network.emit([signal(input1), signal(input2)]) == [signal(outputs[0]), signal(outputs[1])]

        where:
        input1 | input2 || outputs
        0      | 0      || [0.1d, 0.2d]
        0.5d   | 0.5d   || [0.6d, 1.2d]
        1      | 1      || [1.1d, 2.2d]
    }

    def "input neurons transmit signals to output neuron through hidden neurons"() {
        given:
        def network = NetworkFixture
                .ofInputs(input("I1"), input("I2"))
                .andNeuronLayer(neuron("H1", 0.10), neuron("H2", 0.11), neuron("H3", 0.12))
                .andNeuronLayer(neuron("H10", 0.20), neuron("H11", 0.21))
                .andOutputs(output("O1", 0.30), output("O2", 0.31))
                .connect("I1", ["H1", "H2", "H3"], [0.1d, 0.1d, 0.1d])
                .connect("I2", ["H1", "H2", "H3"], [0.2d, 0.2d, 0.2d])
                .connect("H1", ["H10", "H11"], [0.3d, 0.3d])
                .connect("H2", ["H10", "H11"], [0.3d, 0.3d])
                .connect("H3", ["H10", "H11"], [0.3d, 0.3d])
                .connect("H10", ["O1", "O2"], [0.4d, 0.4d])
                .connect("H11", ["O1", "O2"], [0.5d, 0.5d])
                .create()


        expect:
        network.emit([signal(input1), signal(input2)]) == [signal(outputs[0]), signal(outputs[1])]

        where:
        input1 | input2 || outputs
        0.5d   | 1      || [0.7766d, 0.7866d]
        1      | 0.5d   || [0.7361d, 0.7461d]
        0      | 0      || [0.5741d, 0.5841000000000001d]
    }

    private Signal signal(double value) {
        new Signal(value)
    }
}
