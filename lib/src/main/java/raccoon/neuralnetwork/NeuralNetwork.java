package raccoon.neuralnetwork;

import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Stream.concat;

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

    Snapshot toSnapshot() {
        return new Snapshot(inputLayer.toSnapshot(), neuronsLayers.toSnapshot(), outputLayer.toSnapshot());
    }

    record Snapshot(InputLayer.Snapshot inputLayer,
                    NeuronsLayers.Snapshot neuronsLayers,
                    OutputLayer.Snapshot outputLayer) {

        Stream<InputLayer.Input.Snapshot> inputs() {
            return inputLayer.inputs().stream();
        }

        Stream<NeuronLayer.Snapshot> neuronLayers() {
            return neuronsLayers.layers().stream();
        }

        Stream<OutputLayer.Output.Snapshot> outputs() {
            return outputLayer.outputs().stream();
        }

        Stream<Link.Snapshot> links() {
            return concat(concat(inputLayer.links(), neuronsLayers.links()), outputLayer.links());
        }

    }
}
