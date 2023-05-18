package raccoon.neuralnetwork;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Consumer;

class Layer<T> {
    private final Collection<T> neurons = new ArrayList<>();

    Layer<T> add(T neuron) {
        neurons.add(neuron);
        return this;
    }

    Layer<T> forEach(Consumer<T> consumer) {
        neurons.forEach(consumer);
        return this;
    }

    Collection<T> neurons() {
        return neurons;
    }
}
