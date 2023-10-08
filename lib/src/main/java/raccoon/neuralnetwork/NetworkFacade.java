package raccoon.neuralnetwork;

public class NetworkFacade {
    private static final WeightsGenerator RANDOM_WEIGHTS = new RandomWeightsGenerator();

    private NetworkFacade() {
    }

    public static CreateNewNetwork.SetupFirstLayer newNetwork() {
        return CreateNewNetwork.ofWeights(RANDOM_WEIGHTS);
    }

    public static String serializeToJson(NeuralNetwork network) {
        return SerializeNetwork.toJson(network);
    }

    public static NeuralNetwork deserializeFromJson(String json) {
        return SerializeNetwork.fromJson(json);
    }
}
