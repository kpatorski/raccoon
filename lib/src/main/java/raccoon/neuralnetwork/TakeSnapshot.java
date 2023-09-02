package raccoon.neuralnetwork;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import raccoon.neuralnetwork.activationfunction.ActivationFunction;

class TakeSnapshot {
    private static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(Link.class, new LinkSerializer())
            .registerTypeAdapter(ActivationFunction.class, new ActivationFunctionSerializer())
            .create();

    private TakeSnapshot() {
    }

    static String of(NeuralNetwork network) {
        return GSON.toJson(network);
    }
}
