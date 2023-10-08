package raccoon.neuralnetwork;

import raccoon.neuralnetwork.activationfunction.ActivationFunction;

class CreateNewNetwork {

    static SetupFirstLayer ofWeights(WeightsGenerator weightsGenerator) {
        return new Builder(weightsGenerator);
    }

    public static class Builder implements SetupFirstLayer, SetupNextLayer, SetupLastLayer {
        private final WeightsGenerator weightsGenerator;
        private InputLayer inputLayer;
        private final NeuronsLayers hiddenLayers = NeuronsLayers.empty();

        private Builder(WeightsGenerator weightsGenerator) {
            this.weightsGenerator = weightsGenerator;
        }

        @Override
        public SetupNextLayer ofInputs(long numberOfInputs) {
            inputLayer = InputLayer.of(numberOfInputs);
            return this;
        }

        @Override
        public SetupNextLayer hiddenLayer(long numberOfNeurons, ActivationFunction activationFunction) {
            hiddenLayers.add(numberOfNeurons, activationFunction, weightsGenerator);
            return this;
        }

        @Override
        public NeuralNetwork outputLayer(long numberOfOutputs, ActivationFunction activationFunction) {
            var outputLayer = OutputLayer.of(numberOfOutputs, activationFunction, weightsGenerator);
            hiddenLayers
                    .ifEmpty(() -> inputLayer.connect(outputLayer.outputs(), weightsGenerator))
                    .ifNotEmpty(() -> hiddenLayers
                            .first(first -> inputLayer.connect(first.neurons(), weightsGenerator))
                            .last(last -> last.connect(outputLayer.outputs(), weightsGenerator)));
            return new NeuralNetwork(inputLayer, hiddenLayers, outputLayer);
        }
    }

    public interface SetupFirstLayer {
        SetupNextLayer ofInputs(long numberOfInputs);
    }

    public interface SetupNextLayer extends SetupLastLayer {
        SetupNextLayer hiddenLayer(long numberOfNeurons, ActivationFunction activationFunction);
    }

    public interface SetupLastLayer {
        NeuralNetwork outputLayer(long numberOfOutputs, ActivationFunction activationFunction);
    }
}
