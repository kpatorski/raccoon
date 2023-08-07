package raccoon.neuralnetwork.usecases.snapshot;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import raccoon.neuralnetwork.NeuralNetwork;
import raccoon.neuralnetwork.core.Link;
import raccoon.neuralnetwork.core.Network;
import raccoon.neuralnetwork.core.activationfunction.ActivationFunction;

public class RestoreSnapshot {
    private static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(Link.class, new LinkSerializer())
            .registerTypeAdapter(ActivationFunction.class, new ActivationFunctionSerializer())
            .create();

    private RestoreSnapshot() {
    }

    public static NeuralNetwork from(String snapshot) {
        return GSON.fromJson(snapshot, Network.class);
    }
}
