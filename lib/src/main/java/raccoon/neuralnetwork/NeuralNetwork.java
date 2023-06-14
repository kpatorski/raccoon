package raccoon.neuralnetwork;

import java.util.Collection;
import java.util.Iterator;

public class NeuralNetwork {
    private final Layer<Input> inputLayer;
    private final HiddenLayers hiddenLayers;
    private final Layer<Output> outputLayer;

    NeuralNetwork(Layer<Input> inputLayer, HiddenLayers hiddenLayers, Layer<Output> outputLayer) {
        this.inputLayer = inputLayer;
        this.hiddenLayers = hiddenLayers;
        this.outputLayer = outputLayer;
    }

    public NeuralNetwork inputs(Collection<Signal> signals) {
        return inputs(signals.iterator());
    }

    private NeuralNetwork inputs(Iterator<Signal> signals) {
        inputLayer.forEach(neuron -> neuron.emit(signals.next()));
        hiddenLayers.forEachNeuron(Neuron::emit);
        return this;
    }

    public Collection<Signal> outputs() {
        return outputLayer.neurons().stream()
                .map(Output::signal)
                .toList();
    }

}
