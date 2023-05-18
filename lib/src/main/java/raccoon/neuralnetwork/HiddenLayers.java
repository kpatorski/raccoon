package raccoon.neuralnetwork;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

class HiddenLayers {
    private final List<Layer<HiddenNeuron>> layers = new ArrayList<>();

    HiddenLayers add(Layer<HiddenNeuron> layer) {
        layers.add(layer);
        return this;
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

    HiddenLayers forEachNeuron(Consumer<HiddenNeuron> consumer) {
        layers.forEach(layer -> layer.neurons().forEach(consumer));
        return this;
    }
}

