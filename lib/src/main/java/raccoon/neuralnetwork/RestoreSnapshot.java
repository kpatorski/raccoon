package raccoon.neuralnetwork;

import raccoon.neuralnetwork.InputLayer.Input;
import raccoon.neuralnetwork.Link.Weight;
import raccoon.neuralnetwork.NeuronLayer.Neuron;
import raccoon.neuralnetwork.OutputLayer.Output;
import raccoon.neuralnetwork.activationfunction.ActivationFunctions;

import java.util.*;
import java.util.stream.Stream;

class RestoreSnapshot {
    private RestoreSnapshot() {
    }

    static NeuralNetwork restore(NeuralNetwork.Snapshot snapshot) {
        Links links = Links.of(snapshot.links());
        InputLayer inputLayer = new InputLayer();
        snapshot.inputs()
                .map(inputSnapshot -> restoreInput(inputSnapshot, links))
                .forEach(inputLayer::add);

        NeuronsLayers neuronsLayers = new NeuronsLayers();
        snapshot.neuronLayers()
                .map(neuronLayerSnapshot -> restoreNeuronLayer(neuronLayerSnapshot, links))
                .forEach(neuronsLayers::add);

        OutputLayer outputLayer = new OutputLayer();
        snapshot.outputs()
                .map(outputSnapshot -> restoreOutput(outputSnapshot, links))
                .forEach(outputLayer::add);
        return new NeuralNetwork(inputLayer, neuronsLayers, outputLayer);
    }

    private static Input restoreInput(Input.Snapshot snapshot, Links links) {
        Input input = new Input();
        links.linksByIds(ids(snapshot.outgoingLinks()))
                .forEach(input::linkWithReceiver);
        return input;
    }

    private static NeuronLayer restoreNeuronLayer(NeuronLayer.Snapshot snapshot, Links links) {
        NeuronLayer neuronLayer = new NeuronLayer();

        snapshot.neurons().stream()
                .map(neuronSnapshot -> restoreNeuron(neuronSnapshot, links))
                .forEach(neuronLayer::add);

        return neuronLayer;
    }

    private static Neuron restoreNeuron(Neuron.Snapshot snapshot, Links links) {
        Neuron neuron = new Neuron(ActivationFunctions.of(snapshot.activationFunction()));

        links.linksByIds(ids(snapshot.incomingLinks()))
                .forEach(neuron::linkWithEmitter);
        links.linksByIds(ids(snapshot.outgoingLinks()))
                .forEach(neuron::linkWithReceiver);
        return neuron;
    }

    private static Output restoreOutput(Output.Snapshot snapshot, Links links) {
        Output output = new Output(ActivationFunctions.of(snapshot.activationFunction()));
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
            snapshots.forEach(link -> links.add(link.id(), Link.ofWeight(new Weight(link.weight()))));
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
