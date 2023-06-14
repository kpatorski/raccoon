package raccoon.neuralnetwork;

import java.util.ArrayList;
import java.util.Collection;
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

class NeuronLayer {
    private final Collection<Neuron> neurons = new ArrayList<>();

    void add(Neuron neuron) {
        neurons.add(neuron);
    }

    Collection<Neuron> neurons() {
        return neurons;
    }
}

class OutputLayer {
    private final Collection<Output> outputs = new ArrayList<>();

    void add(Output output) {
        outputs.add(output);
    }

    Collection<Output> outputs() {
        return outputs;
    }
}

