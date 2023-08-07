package raccoon.neuralnetwork.usecases.createnetwork

import raccoon.neuralnetwork.NeuralNetwork
import raccoon.neuralnetwork.usecases.snapshot.TakeSnapshot
import spock.lang.Specification

import static org.hamcrest.Matchers.*
import static raccoon.neuralnetwork.AssertJson.assertThat
import static raccoon.neuralnetwork.core.activationfunction.ActivationFunctions.linearFunction

class CreateNewNetworkTest extends Specification {

    def "neural network has no hidden layer"() {
        when:
        NeuralNetwork network = CreateNewNetwork.ofRandomWeights()
                .inputLayer(2)
                .outputLayer(2, linearFunction())

        then:
        assertThat(TakeSnapshot.of(network))
                .on('$.inputLayer.inputs[*]').satisfies(hasSize(2))
                .on('$.inputLayer.inputs[0].outgoingLinks[*]').satisfies(hasSize(2))
                .on('$.inputLayer.inputs[1].outgoingLinks[*]').satisfies(hasSize(2))

                .on('$.neuronsLayers.layers[*]').satisfies(hasSize(0))

                .on('$.outputLayer.outputs[*]').satisfies(hasSize(2))
                .on('$.outputLayer.outputs[0].activationFunction').satisfies(equalTo("LINEAR"))
                .on('$.outputLayer.outputs[0].incomingLinks[*]').satisfies(hasSize(2))
                .on('$.outputLayer.outputs[1].incomingLinks[*]').satisfies(hasSize(2))
                .on('$.outputLayer.outputs[1].activationFunction').satisfies(equalTo("LINEAR"))
    }

    def "neural network has hidden layer"() {
        given: "a network"
        NeuralNetwork network = CreateNewNetwork.ofRandomWeights()
                .inputLayer(2)
                .hiddenLayer(1, linearFunction())
                .hiddenLayer(2, linearFunction())
                .outputLayer(2, linearFunction())

        and: "its snapshot"
        def snapshot = TakeSnapshot.of(network)

        expect: "network has single input layer, 2 hidden layers and single output layer"
        assertThat(snapshot)
                .on('$.*').satisfies(hasSize(3))
                .on('$.inputLayer').satisfies(notNullValue())
                .on('$.neuronsLayers').satisfies(notNullValue())
                .on('$.outputLayer').satisfies(notNullValue())

        and: "input layer has 2 neurons"
        assertThat(snapshot)
                .on('$.inputLayer.inputs[*]').satisfies(hasSize(2))
                .on('$.inputLayer.inputs[0].outgoingLinks[*]').satisfies(hasSize(1))
                .on('$.inputLayer.inputs[1].outgoingLinks[*]').satisfies(hasSize(1))

        and: "first hidden layer has 1 neuron"
        assertThat(snapshot)
                .on('$.neuronsLayers.layers[0].neurons[*]').satisfies(hasSize(1))
                .on('$.neuronsLayers.layers[0].neurons[0].activationFunction').satisfies(equalTo("LINEAR"))
                .on('$.neuronsLayers.layers[0].neurons[0].outgoingLinks[*]').satisfies(hasSize(2))
                .on('$.neuronsLayers.layers[0].neurons[0].incomingLinks[*]').satisfies(hasSize(2))

        and: "second hidden layer has 2 neuron"
        assertThat(snapshot)
                .on('$.neuronsLayers.layers[1].neurons[*]').satisfies(hasSize(2))
                .on('$.neuronsLayers.layers[1].neurons[0].activationFunction').satisfies(equalTo("LINEAR"))
                .on('$.neuronsLayers.layers[1].neurons[0].outgoingLinks[*]').satisfies(hasSize(2))
                .on('$.neuronsLayers.layers[1].neurons[0].incomingLinks[*]').satisfies(hasSize(1))
                .on('$.neuronsLayers.layers[1].neurons[1].activationFunction').satisfies(equalTo("LINEAR"))
                .on('$.neuronsLayers.layers[1].neurons[1].outgoingLinks[*]').satisfies(hasSize(2))
                .on('$.neuronsLayers.layers[1].neurons[1].incomingLinks[*]').satisfies(hasSize(1))

        and: "output layer has 2 neurons"
        assertThat(snapshot)
                .on('$.outputLayer.outputs[*]').satisfies(hasSize(2))
                .on('$.outputLayer.outputs[0].activationFunction').satisfies(equalTo("LINEAR"))
                .on('$.outputLayer.outputs[0].incomingLinks[*]').satisfies(hasSize(2))
                .on('$.outputLayer.outputs[1].activationFunction').satisfies(equalTo("LINEAR"))
                .on('$.outputLayer.outputs[1].incomingLinks[*]').satisfies(hasSize(2))
    }
}
