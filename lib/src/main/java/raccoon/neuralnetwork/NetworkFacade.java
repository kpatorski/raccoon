package raccoon.neuralnetwork;

public class NetworkFacade {
    private NetworkFacade() {
    }

    public static CreateNewNetwork.SetupFirstLayer newNetwork() {
        return CreateNewNetwork.ofRandomWeights();
    }

    public static String serializeToJson(NeuralNetwork network) {
        return TakeSnapshot.of(network);
    }

    public static NeuralNetwork deserializeFromJson(String json) {
        return RestoreSnapshot.from(json);
    }
}
