package raccoon.neuralnetwork;

import java.util.Collection;
import java.util.Iterator;

public class NeuralNetwork {
    private final Layer<InputNeuron> inputLayer;
    private final HiddenLayers hiddenLayers;
    private final Layer<OutputNeuron> outputLayer;

    NeuralNetwork(Layer<InputNeuron> inputLayer, HiddenLayers hiddenLayers, Layer<OutputNeuron> outputLayer) {
        this.inputLayer = inputLayer;
        this.hiddenLayers = hiddenLayers;
        this.outputLayer = outputLayer;
    }

    public NeuralNetwork inputs(Collection<Signal> signals) {
        return inputs(signals.iterator());
    }

    private NeuralNetwork inputs(Iterator<Signal> signals) {
        inputLayer.forEach(neuron -> neuron.transmit(signals.next()));
        hiddenLayers.forEachNeuron(HiddenNeuron::transmit);
        return this;
    }

    public Collection<Signal> outputs() {
        return outputLayer.neurons().stream()
                .map(OutputNeuron::signal)
                .toList();
    }

}
