package raccoon.neuralnetwork.usecases.snapshot;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import raccoon.neuralnetwork.NeuralNetwork;
import raccoon.neuralnetwork.core.Link;

public class TakeSnapshot {
    private static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(Link.class, new LinkSerializer())
            .create();

    public String of(NeuralNetwork network) {
        return GSON.toJson(network);
    }
}
