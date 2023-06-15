package raccoon.neuralnetwork.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

public class NeuronsLayers {
    private final List<NeuronLayer> layers = new ArrayList<>();

    public void add(NeuronLayer layer) {
        layers.add(layer);
    }

    public boolean isEmpty() {
        return layers.isEmpty();
    }

    public NeuronLayer first() {
        return layers.get(0);
    }

    public NeuronLayer last() {
        return layers.get(layers.size() - 1);
    }

    public Iterator<NeuronLayer> iterator() {
        return layers.iterator();
    }

    void forEachNeuron(Consumer<Neuron> consumer) {
        layers.forEach(layer -> layer.neurons().forEach(consumer));
    }
}
