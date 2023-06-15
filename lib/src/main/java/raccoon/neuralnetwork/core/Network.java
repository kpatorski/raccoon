package raccoon.neuralnetwork.core;

import raccoon.neuralnetwork.NeuralNetwork;
import raccoon.neuralnetwork.Signal;

import java.util.Collection;
import java.util.Iterator;

public class Network implements NeuralNetwork {
    private final InputLayer inputLayer;
    private final NeuronsLayers neuronsLayers;
    private final OutputLayer outputLayer;

    public Network(InputLayer inputLayer, NeuronsLayers neuronsLayers, OutputLayer outputLayer) {
        this.inputLayer = inputLayer;
        this.neuronsLayers = neuronsLayers;
        this.outputLayer = outputLayer;
    }

    @Override
    public Collection<Signal> emit(Collection<Signal> signals) {
        return emit(signals.iterator())
                .outputs();
    }

    private Network emit(Iterator<Signal> signals) {
        inputLayer.forEach(neuron -> neuron.emit(signals.next()));
        neuronsLayers.forEachNeuron(Neuron::emit);
        return this;
    }

    private Collection<Signal> outputs() {
        return outputLayer.outputs().stream()
                .map(Output::signal)
                .toList();
    }
}
