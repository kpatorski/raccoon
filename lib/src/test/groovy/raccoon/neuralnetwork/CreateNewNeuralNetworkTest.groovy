package raccoon.neuralnetwork

import spock.lang.Specification

import java.util.stream.Stream

import static raccoon.neuralnetwork.activationfunction.ActivationFunctions.linearFunction
import static raccoon.neuralnetwork.activationfunction.FunctionId.LINEAR

class CreateNewNeuralNetworkTest extends Specification {

    def "neural network has no hidden layer"() {
        given:
        def network = CreateNewNetwork.ofRandomWeights()
                .inputLayer(2)
                .outputLayer(2, linearFunction())
                .toSnapshot()

        expect: "there are two inputs"
        def inputs = network.inputs().toList()
        inputs.size() == 2

        and: "connected with 2 linear outputs"
        network.outputs().collect(it -> it.activationFunction()) == [LINEAR, LINEAR]
        areConnected(inputs[0], network.outputs())
        areConnected(inputs[1], network.outputs())

        and: "there are no hidden neurons"
        network.neuronsLayers().layers().isEmpty()
    }

    def "neural network has hidden layer"() {
        given:
        def network = CreateNewNetwork.ofRandomWeights()
                .inputLayer(2)
                .hiddenLayer(1, linearFunction())
                .hiddenLayer(2, linearFunction())
                .outputLayer(2, linearFunction())
                .toSnapshot()

        expect: "there are two inputs"
        def inputs = network.inputs().toList()
        inputs.size() == 2

        and: "connected with 1 linear neuron of first hidden layer"
        def hiddenLayers = network.neuronsLayers().layers()
        hiddenLayers[0].neurons().collect(it -> it.activationFunction()) == [LINEAR]
        areConnected(inputs[0], hiddenLayers[0].neurons())
        areConnected(inputs[1], hiddenLayers[0].neurons())

        and: "first hidden layer is connected with 2 linear neurons of second layer"
        hiddenLayers[1].neurons().collect(it -> it.activationFunction()) == [LINEAR, LINEAR]
        areConnected(hiddenLayers[0].neurons()[0], hiddenLayers[1].neurons())

        and: "second hidden layer is connected 2 linear outputs"
        network.outputs().collect(it -> it.activationFunction()) == [LINEAR, LINEAR]
        areConnected(hiddenLayers[1].neurons()[0], network.outputs())
        areConnected(hiddenLayers[1].neurons()[1], network.outputs())
    }

    private void areConnected(InputLayer.Input.Snapshot input, Stream<OutputLayer.Output.Snapshot> outputs) {
        outputs.forEach { output ->
            assert output.incomingLinks().stream().anyMatch { link -> input.outgoingLinks().contains(link) }
        }
    }

    private void areConnected(InputLayer.Input.Snapshot input, List<NeuronLayer.Neuron.Snapshot> neurons) {
        neurons.forEach { neuron ->
            assert neuron.incomingLinks().stream().anyMatch { link -> input.outgoingLinks().contains(link) }
        }
    }

    private void areConnected(NeuronLayer.Neuron.Snapshot neuron, List<NeuronLayer.Neuron.Snapshot> neurons) {
        neurons.forEach { receiver ->
            assert receiver.incomingLinks().stream().anyMatch { link -> neuron.outgoingLinks().contains(link) }
        }
    }

    private void areConnected(NeuronLayer.Neuron.Snapshot neuron, Stream<OutputLayer.Output.Snapshot> outputs) {
        outputs.forEach { output ->
            assert output.incomingLinks().stream().anyMatch { link -> neuron.outgoingLinks().contains(link) }
        }
    }
}
