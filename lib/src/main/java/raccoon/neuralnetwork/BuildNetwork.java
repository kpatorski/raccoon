package raccoon.neuralnetwork;

import java.util.random.RandomGenerator;
import java.util.stream.Stream;

public class BuildNetwork {
    private static final WeightsGenerator RANDOM_WEIGHTS = randomWeightsGenerator();

    private static WeightsGenerator randomWeightsGenerator() {
        return () -> new Link.Weight(RandomGenerator.getDefault().nextDouble(-1, 1));
    }

    public static SetupFirstLayer newNetwork() {
        return new Builder(RANDOM_WEIGHTS);
    }

    static SetupFirstLayer ofFixedWeights(WeightsGenerator weights) {
        return new Builder(weights);
    }

    public static class Builder implements SetupFirstLayer, SetupNextLayer, SetupLastLayer {
        private final Layer<InputNeuron> inputLayer = new Layer<>();
        private final HiddenLayers hiddenLayers = new HiddenLayers();
        private final Layer<OutputNeuron> outputLayer = new Layer<>();
        private final ConnectLayers connectLayers;

        private Builder(WeightsGenerator weightsGenerator) {
            connectLayers = new ConnectLayers(weightsGenerator);
        }

        @Override
        public SetupNextLayer inputLayer(long numberOfNeurons) {
            Stream.generate(InputNeuron::new)
                    .limit(numberOfNeurons)
                    .forEach(inputLayer::add);
            return this;
        }

        @Override
        public SetupNextLayer hiddenLayer(long numberOfNeurons, ActivationFunction activationFunction) {
            Layer<HiddenNeuron> layer = new Layer<>();
            Stream.generate(() -> new HiddenNeuron(activationFunction))
                    .limit(numberOfNeurons)
                    .forEach(layer::add);
            hiddenLayers.add(layer);
            return this;
        }

        @Override
        public NeuralNetwork outputLayer(long numberOfNeurons, ActivationFunction activationFunction) {
            Stream.generate(() -> new OutputNeuron(activationFunction))
                    .limit(numberOfNeurons)
                    .forEach(outputLayer::add);
            connectLayers.eachNeurons(inputLayer, hiddenLayers, outputLayer);
            return new NeuralNetwork(inputLayer, hiddenLayers, outputLayer);
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
