package raccoon.neuralnetwork;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import raccoon.neuralnetwork.activationfunction.ActivationFunction;

class RestoreSnapshot {
    private static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(Link.class, new LinkSerializer())
            .registerTypeAdapter(ActivationFunction.class, new ActivationFunctionSerializer())
            .create();

    private RestoreSnapshot() {
    }

    public static NeuralNetwork from(String snapshot) {
        return GSON.fromJson(snapshot, NeuralNetwork.class);
    }
}
