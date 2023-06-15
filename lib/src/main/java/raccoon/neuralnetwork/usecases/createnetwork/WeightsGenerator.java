package raccoon.neuralnetwork.usecases.createnetwork;

import raccoon.neuralnetwork.core.Link.Weight;
//TODO: make it package private once a snapshot is introduced
public interface WeightsGenerator {
    Weight next();
}
