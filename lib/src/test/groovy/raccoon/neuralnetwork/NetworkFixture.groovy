package raccoon.neuralnetwork

import raccoon.neuralnetwork.activationfunction.FunctionId

import static java.util.UUID.randomUUID
import static raccoon.neuralnetwork.activationfunction.FunctionId.LINEAR

class NetworkFixture {
    static DefineLayers ofInputs(Input... inputs) {
        assert inputs != null && !inputs.toList().isEmpty()
        return new Builder(inputs.toList())
    }

    static class Builder implements DefineLayers, DefineLinks {
        private final Map<String, InputLayer.Input.Snapshot> inputByID = new HashMap<>()

        private final Map<String, NeuronLayer.Neuron.Snapshot> neuronById = new HashMap<>()
        private final Collection<NeuronsLayer> neuronLayers = new ArrayList<>()

        private final Map<String, OutputLayer.Output.Snapshot> outputByID = new HashMap<>()

        Builder(Collection<Input> inputs) {
            inputs.forEach { input -> inputByID.put(input.id(), input.toSnapshot()) }
        }

        @Override
        DefineLayers andNeuronLayer(Neuron... neurons) {
            assert neurons != null && !neurons.toList().isEmpty()
            def layer = new NeuronsLayer()
            neuronLayers.add(layer)

            List.of(neurons).forEach(neuron -> {
                def snapshot = neuron.toSnapshot()
                neuronById.put(neuron.id(), snapshot)
                layer.neurons.add(snapshot)
            })
            this
        }

        @Override
        DefineLinks andOutputs(Output... outputs) {
            assert outputs != null && !outputs.toList().isEmpty()
            List.of(outputs).forEach { outputByID.put(it.id(), it.toSnapshot()) }
            this
        }

        @Override
        DefineLinks connect(String emitterId, Collection<String> receiverIds, Collection<Double> weights) {
            assert !receiverIds.isEmpty() && receiverIds.size() == weights.size()
            def links = weights.collect { weight -> link(weight) }

            if (isInput(emitterId)) {
                connectInputAndReceivers(inputByID.get(emitterId), receiverIds, links)
            } else {
                connectNeuronAndReceivers(neuronById.get(emitterId), receiverIds, links)
            }
            this
        }

        private static raccoon.neuralnetwork.Link.Snapshot link(double weight) {
            new raccoon.neuralnetwork.Link.Snapshot(randomUUID(), weight)
        }

        private boolean isInput(String id) {
            inputByID.containsKey(id)
        }

        private void connectInputAndReceivers(InputLayer.Input.Snapshot input,
                                              Collection<String> receiverIds,
                                              List<raccoon.neuralnetwork.Link.Snapshot> links) {
            receiverIds.eachWithIndex { String id, int index ->
                def link = links[index]
                if (isOutput(id)) {
                    connectInputToOutput(input, outputByID.get(id), link)
                } else {
                    connectInputToNeuron(input, neuronById.get(id), link)
                }
            }
        }

        private boolean isOutput(String id) {
            outputByID.containsKey(id)
        }

        private static void connectInputToOutput(
                InputLayer.Input.Snapshot input,
                OutputLayer.Output.Snapshot output,
                raccoon.neuralnetwork.Link.Snapshot link) {
            input.addLink(link)
            output.addLink(link)
        }

        private static void connectInputToNeuron(
                InputLayer.Input.Snapshot input,
                NeuronLayer.Neuron.Snapshot neuron,
                raccoon.neuralnetwork.Link.Snapshot link) {
            input.addLink(link)
            neuron.addIncomingLink(link)
        }

        private void connectNeuronAndReceivers(NeuronLayer.Neuron.Snapshot neuron,
                                               Collection<String> receiverIds,
                                               List<raccoon.neuralnetwork.Link.Snapshot> links) {
            receiverIds.eachWithIndex { String id, int index ->
                def link = links[index]
                if (isOutput(id)) {
                    connectNeuronToOutput(neuron, outputByID.get(id), link)
                } else {
                    connectNeuronToNeuron(neuron, neuronById.get(id), link)
                }
            }
        }

        private static void connectNeuronToOutput(
                NeuronLayer.Neuron.Snapshot neuron,
                OutputLayer.Output.Snapshot output,
                raccoon.neuralnetwork.Link.Snapshot link) {
            neuron.addOutgoingLink(link)
            output.addLink(link)
        }

        private static void connectNeuronToNeuron(
                NeuronLayer.Neuron.Snapshot emitter,
                NeuronLayer.Neuron.Snapshot receiver,
                raccoon.neuralnetwork.Link.Snapshot link) {
            emitter.addOutgoingLink(link)
            receiver.addIncomingLink(link)
        }

        @Override
        NeuralNetwork create() {
            NeuralNetwork.Snapshot snapshot = new NeuralNetwork.Snapshot(
                    createInputLayerSnapshot(),
                    createNeuronsLayerSnapshot(),
                    createOutputLayerSnapshot()
            )
            RestoreSnapshot.restore(snapshot)
        }

        private InputLayer.Snapshot createInputLayerSnapshot() {
            def layer = new InputLayer.Snapshot()
            inputByID.values().forEach { it -> layer.addInput(it) }
            layer
        }

        private NeuronsLayers.Snapshot createNeuronsLayerSnapshot() {
            def layer = new NeuronsLayers.Snapshot()
            neuronLayers
                    .collect { neuronLayer -> neuronLayer.toSnapshot() }
                    .forEach { neuronLayer -> layer.addLayer(neuronLayer) }
            layer
        }

        private OutputLayer.Snapshot createOutputLayerSnapshot() {
            def layer = new OutputLayer.Snapshot()
            outputByID.values().forEach { it -> layer.addOutput(it) }
            layer
        }
    }

    interface DefineLayers {
        DefineLayers andNeuronLayer(Neuron... neurons)

        DefineLinks andOutputs(Output... outputs)
    }

    interface DefineLinks extends Create {
        DefineLinks connect(String emitterId, Collection<String> receiverIds, Collection<Double> weights)
    }

    interface Create {
        NeuralNetwork create()
    }

    record Input(String id) {
        InputLayer.Input.Snapshot toSnapshot() {
            new InputLayer.Input.Snapshot()
        }
    }

    static class NeuronsLayer {
        private final List<NeuronLayer.Neuron.Snapshot> neurons = new ArrayList<>()

        NeuronLayer.Snapshot toSnapshot() {
            new NeuronLayer.Snapshot().addNeurons(neurons)
        }
    }

    record Neuron(String id, FunctionId activationFunction, double bias) {
        NeuronLayer.Neuron.Snapshot toSnapshot() {
            new NeuronLayer.Neuron.Snapshot(activationFunction, bias)
        }
    }

    record Output(String id, FunctionId activationFunction, double bias) {
        OutputLayer.Output.Snapshot toSnapshot() {
            new OutputLayer.Output.Snapshot(activationFunction, bias)
        }
    }

    record Link(String from, String to, double weight) {
    }

    static Input input(String id) {
        new Input(id)
    }

    static Neuron neuron(String id, FunctionId activationFunction = LINEAR, double bias) {
        new Neuron(id, activationFunction, bias)
    }

    static Output output(String id, FunctionId activationFunction = LINEAR, double bias) {
        new Output(id, activationFunction, bias)
    }
}
