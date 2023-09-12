package raccoon.neuralnetwork;

public class NetworkFacade {
    private NetworkFacade() {
    }

    public static CreateNewNetwork.SetupFirstLayer newNetwork() {
        return CreateNewNetwork.ofRandomWeights();
    }

    public static String serializeToJson(NeuralNetwork network) {
        return SerializeNetwork.toJson(network);
    }

    public static NeuralNetwork deserializeFromJson(String json) {
        return SerializeNetwork.fromJson(json);
    }
}
