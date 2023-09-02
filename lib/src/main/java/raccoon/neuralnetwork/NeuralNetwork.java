package raccoon.neuralnetwork;

import java.util.List;

public class NeuralNetwork {
    private final InputLayer inputLayer;
    private final NeuronsLayers neuronsLayers;
    private final OutputLayer outputLayer;

    NeuralNetwork(InputLayer inputLayer, NeuronsLayers neuronsLayers, OutputLayer outputLayer) {
        this.inputLayer = inputLayer;
        this.neuronsLayers = neuronsLayers;
        this.outputLayer = outputLayer;
    }

    public List<Signal> emit(List<Signal> signals) {
        return emitSignals(signals).receiveOutputs();
    }

    private NeuralNetwork emitSignals(List<Signal> signals) {
        inputLayer.emit(signals);
        neuronsLayers.transmit();
        return this;
    }

    private List<Signal> receiveOutputs() {
        return outputLayer.signals().toList();
    }
}
