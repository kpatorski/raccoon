package raccoon.neuralnetwork;

import com.google.gson.Gson;

class SerializeNetwork {
    private static final Gson GSON = new Gson();

    private SerializeNetwork() {
    }

    static NeuralNetwork fromJson(String json) {
        return RestoreSnapshot.restore(jsonToSnapshot(json));
    }

    private static NeuralNetwork.Snapshot jsonToSnapshot(String json) {
        return GSON.fromJson(json, NeuralNetwork.Snapshot.class);
    }

    static String toJson(NeuralNetwork network) {
        return GSON.toJson(network.toSnapshot());
    }
}
