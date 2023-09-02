package raccoon.neuralnetwork;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

class NeuronsLayers {
    private final List<NeuronLayer> layers = new ArrayList<>();

    void add(NeuronLayer layer) {
        layers.add(layer);
    }

    boolean isEmpty() {
        return layers.isEmpty();
    }

    NeuronLayer first() {
        return layers.get(0);
    }

    NeuronLayer last() {
        return layers.get(layers.size() - 1);
    }

    Iterator<NeuronLayer> iterator() {
        return layers.iterator();
    }

    void forEachNeuron(Consumer<Neuron> consumer) {
        layers.forEach(layer -> layer.neurons().forEach(consumer));
    }
}
