package raccoon.neuralnetwork;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

interface NetworkModule {
}

class HiddenLayers {
    private final List<Layer<HiddenNeuron>> layers = new ArrayList<>();

    void add(Layer<HiddenNeuron> layer) {
        layers.add(layer);
    }

    boolean isEmpty() {
        return layers.isEmpty();
    }

    Layer<HiddenNeuron> first() {
        return layers.get(0);
    }

    Layer<HiddenNeuron> last() {
        return layers.get(layers.size() - 1);
    }

    Iterator<Layer<HiddenNeuron>> iterator() {
        return layers.iterator();
    }

    void forEachNeuron(Consumer<HiddenNeuron> consumer) {
        layers.forEach(layer -> layer.neurons().forEach(consumer));
    }
}

class Layer<T> {
    private final Collection<T> neurons = new ArrayList<>();

    void add(T neuron) {
        neurons.add(neuron);
    }

    void forEach(Consumer<T> consumer) {
        neurons.forEach(consumer);
    }

    Collection<T> neurons() {
        return neurons;
    }
}
