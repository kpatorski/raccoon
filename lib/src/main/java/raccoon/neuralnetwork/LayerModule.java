package raccoon.neuralnetwork;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

class HiddenLayers {
    private final List<Layer<Neuron>> layers = new ArrayList<>();

    void add(Layer<Neuron> layer) {
        layers.add(layer);
    }

    boolean isEmpty() {
        return layers.isEmpty();
    }

    Layer<Neuron> first() {
        return layers.get(0);
    }

    Layer<Neuron> last() {
        return layers.get(layers.size() - 1);
    }

    Iterator<Layer<Neuron>> iterator() {
        return layers.iterator();
    }

    void forEachNeuron(Consumer<Neuron> consumer) {
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
