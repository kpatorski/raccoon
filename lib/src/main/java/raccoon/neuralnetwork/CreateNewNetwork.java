package raccoon.neuralnetwork;

import raccoon.neuralnetwork.InputLayer.Input;
import raccoon.neuralnetwork.NeuronLayer.Neuron;
import raccoon.neuralnetwork.OutputLayer.Output;
import raccoon.neuralnetwork.activationfunction.ActivationFunction;

import java.util.random.RandomGenerator;
import java.util.stream.Stream;

class CreateNewNetwork {
    private static final WeightsGenerator RANDOM_WEIGHTS = randomWeightsGenerator();

    private static WeightsGenerator randomWeightsGenerator() {
        return () -> new Link.Weight(RandomGenerator.getDefault().nextDouble(-1, 1));
    }

    public static SetupFirstLayer ofRandomWeights() {
        return new Builder(RANDOM_WEIGHTS);
    }

    static SetupFirstLayer ofFixedWeights(WeightsGenerator weights) {
        return new Builder(weights);
    }

    public static class Builder implements SetupFirstLayer, SetupNextLayer, SetupLastLayer {
        private final InputLayer inputLayer = new InputLayer();
        private final NeuronsLayers neuronsLayers = new NeuronsLayers();
        private final OutputLayer outputLayer = new OutputLayer();
        private final ConnectLayers connectLayers;

        private Builder(WeightsGenerator weightsGenerator) {
            connectLayers = new ConnectLayers(weightsGenerator);
        }

        @Override
        public SetupNextLayer inputLayer(long numberOfNeurons) {
            Stream.generate(Input::new)
                    .limit(numberOfNeurons)
                    .forEach(inputLayer::add);
            return this;
        }

        @Override
        public SetupNextLayer hiddenLayer(long numberOfNeurons, ActivationFunction activationFunction) {
            NeuronLayer layer = new NeuronLayer();
            Stream.generate(() -> new Neuron(activationFunction))
                    .limit(numberOfNeurons)
                    .forEach(layer::add);
            neuronsLayers.add(layer);
            return this;
        }

        @Override
        public NeuralNetwork outputLayer(long numberOfNeurons, ActivationFunction activationFunction) {
            Stream.generate(() -> new Output(activationFunction))
                    .limit(numberOfNeurons)
                    .forEach(outputLayer::add);
            connectLayers.eachNeurons(inputLayer, neuronsLayers, outputLayer);
            return new NeuralNetwork(inputLayer, neuronsLayers, outputLayer);
        }
    }

    public interface SetupFirstLayer {
        SetupNextLayer inputLayer(long numberOfNeurons);
    }

    public interface SetupNextLayer extends SetupLastLayer {
        SetupNextLayer hiddenLayer(long numberOfNeurons, ActivationFunction activationFunction);
    }

    public interface SetupLastLayer {
        NeuralNetwork outputLayer(long numberOfNeurons, ActivationFunction activationFunction);
    }
}
