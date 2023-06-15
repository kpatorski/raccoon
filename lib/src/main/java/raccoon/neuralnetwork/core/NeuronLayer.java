package raccoon.neuralnetwork.core;

import java.util.ArrayList;
import java.util.Collection;

public class NeuronLayer {
    private final Collection<Neuron> neurons = new ArrayList<>();

    public void add(Neuron neuron) {
        neurons.add(neuron);
    }

    public Collection<Neuron> neurons() {
        return neurons;
    }
}
