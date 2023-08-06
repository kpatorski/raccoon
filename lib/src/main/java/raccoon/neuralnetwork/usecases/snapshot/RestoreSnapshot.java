package raccoon.neuralnetwork.usecases.snapshot;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import raccoon.neuralnetwork.NeuralNetwork;
import raccoon.neuralnetwork.core.Link;
import raccoon.neuralnetwork.core.Network;

public class RestoreSnapshot {
    private static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(Link.class, new LinkSerializer())
            .create();

    public NeuralNetwork from(String snapshot) {
        return GSON.fromJson(snapshot, Network.class);
    }
}
