package raccoon.neuralnetwork.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Consumer;

public class InputLayer {
    private final Collection<Input> inputs = new ArrayList<>();

    public void add(Input input) {
        inputs.add(input);
    }

    public void forEach(Consumer<Input> consumer) {
        inputs.forEach(consumer);
    }

    public Collection<Input> inputs() {
        return inputs;
    }
}
