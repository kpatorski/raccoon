package raccoon.neuralnetwork;

import java.util.ArrayList;
import java.util.Collection;

class NeuronLayer {
    private final Collection<Neuron> neurons = new ArrayList<>();

    void add(Neuron neuron) {
        neurons.add(neuron);
    }

    Collection<Neuron> neurons() {
        return neurons;
    }
}
