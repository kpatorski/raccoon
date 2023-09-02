package raccoon.neuralnetwork;

import java.util.Collection;
import java.util.Iterator;

public class NeuralNetwork {
    private final InputLayer inputLayer;
    private final NeuronsLayers neuronsLayers;
    private final OutputLayer outputLayer;

    NeuralNetwork(InputLayer inputLayer, NeuronsLayers neuronsLayers, OutputLayer outputLayer) {
        this.inputLayer = inputLayer;
        this.neuronsLayers = neuronsLayers;
        this.outputLayer = outputLayer;
    }

    public Collection<Signal> emit(Collection<Signal> signals) {
        return emit(signals.iterator())
                .outputs();
    }

    private NeuralNetwork emit(Iterator<Signal> signals) {
        inputLayer.forEach(neuron -> neuron.emit(signals.next()));
        neuronsLayers.forEachNeuron(Neuron::transmit);
        return this;
    }

    private Collection<Signal> outputs() {
        return outputLayer.outputs().stream()
                .map(Output::signal)
                .toList();
    }
}
