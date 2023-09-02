package raccoon.neuralnetwork;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Consumer;

class InputLayer {
    private final Collection<Input> inputs = new ArrayList<>();

    void add(Input input) {
        inputs.add(input);
    }

    void forEach(Consumer<Input> consumer) {
        inputs.forEach(consumer);
    }

    Collection<Input> inputs() {
        return inputs;
    }
}
