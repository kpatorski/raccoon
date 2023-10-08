package raccoon.neuralnetwork;

import raccoon.neuralnetwork.InputLayer.Input;
import raccoon.neuralnetwork.NeuronLayer.Neuron;
import raccoon.neuralnetwork.OutputLayer.Output;
import raccoon.neuralnetwork.activationfunction.ActivationFunctions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

class RestoreSnapshot {
    private RestoreSnapshot() {
    }

    static NeuralNetwork restore(NeuralNetwork.Snapshot snapshot) {
        Links links = Links.of(snapshot.links());
        InputLayer inputLayer = restoreInputLayer(snapshot, links);
        NeuronsLayers neuronsLayers = restoreNeuronLayers(snapshot, links);
        OutputLayer outputLayer = restoreOutputLayer(snapshot, links);
        return new NeuralNetwork(inputLayer, neuronsLayers, outputLayer);
    }

    private static InputLayer restoreInputLayer(NeuralNetwork.Snapshot snapshot, Links links) {
        var inputs = snapshot.inputs()
                .map(inputSnapshot -> restoreInput(inputSnapshot, links))
                .toList();
        return new InputLayer(inputs);
    }

    private static Input restoreInput(Input.Snapshot snapshot, Links links) {
        Input input = new Input();
        links.linksByIds(ids(snapshot.outgoingLinks()))
                .forEach(input::linkWithReceiver);
        return input;
    }

    private static NeuronsLayers restoreNeuronLayers(NeuralNetwork.Snapshot snapshot, Links links) {
        var neuronLayers = snapshot.neuronLayers()
                .map(neuronLayerSnapshot -> restoreNeuronLayer(neuronLayerSnapshot, links))
                .toList();
        return new NeuronsLayers(neuronLayers);
    }

    private static NeuronLayer restoreNeuronLayer(NeuronLayer.Snapshot snapshot, Links links) {
        var neurons = snapshot.neurons().stream()
                .map(neuronSnapshot -> restoreNeuron(neuronSnapshot, links))
                .toList();
        return new NeuronLayer(neurons);
    }

    private static Neuron restoreNeuron(Neuron.Snapshot snapshot, Links links) {
        Neuron neuron = new Neuron(ActivationFunctions.of(snapshot.activationFunction()), restoreBias(snapshot.bias()));

        links.linksByIds(ids(snapshot.incomingLinks()))
                .forEach(neuron::linkWithEmitter);
        links.linksByIds(ids(snapshot.outgoingLinks()))
                .forEach(neuron::linkWithReceiver);
        return neuron;
    }

    private static Link.Weight restoreBias(double bias) {
        return new Link.Weight(bias);
    }

    private static OutputLayer restoreOutputLayer(NeuralNetwork.Snapshot snapshot, Links links) {
        var outputs = snapshot.outputs()
                .map(outputSnapshot -> restoreOutput(outputSnapshot, links))
                .toList();
        return new OutputLayer(outputs);
    }

    private static Output restoreOutput(Output.Snapshot snapshot, Links links) {
        Output output = new Output(ActivationFunctions.of(snapshot.activationFunction()), restoreBias(snapshot.bias()));
        links.linksByIds(ids(snapshot.incomingLinks()))
                .forEach(output::linkWithEmitter);
        return output;
    }

    private static List<UUID> ids(List<Link.Snapshot> snapshots) {
        return snapshots.stream()
                .map(Link.Snapshot::id)
                .toList();
    }

    private static class Links {
        private final Map<UUID, Link> linkById = new HashMap<>();

        private static Links of(Stream<Link.Snapshot> snapshots) {
            Links links = new Links();
            snapshots.forEach(link -> links.add(link.id(), Link.fromSnapshot(link)));
            return links;
        }

        private void add(UUID id, Link link) {
            linkById.put(id, link);
        }

        private List<Link> linksByIds(List<UUID> ids) {
            return ids.stream()
                    .map(linkById::get)
                    .toList();
        }
    }
}
