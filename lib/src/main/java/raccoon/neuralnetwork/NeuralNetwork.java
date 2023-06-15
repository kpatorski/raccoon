package raccoon.neuralnetwork;

import java.util.Collection;

public interface NeuralNetwork {
    Collection<Signal> emit(Collection<Signal> inputs);
}
