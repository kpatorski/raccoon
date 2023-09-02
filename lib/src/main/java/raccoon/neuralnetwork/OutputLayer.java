package raccoon.neuralnetwork;

import java.util.ArrayList;
import java.util.Collection;

class OutputLayer {
    private final Collection<Output> outputs = new ArrayList<>();

    void add(Output output) {
        outputs.add(output);
    }

    Collection<Output> outputs() {
        return outputs;
    }
}


