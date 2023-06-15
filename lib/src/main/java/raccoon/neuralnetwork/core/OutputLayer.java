package raccoon.neuralnetwork.core;

import java.util.ArrayList;
import java.util.Collection;

public class OutputLayer {
    private final Collection<Output> outputs = new ArrayList<>();

    public void add(Output output) {
        outputs.add(output);
    }

    public Collection<Output> outputs() {
        return outputs;
    }
}


