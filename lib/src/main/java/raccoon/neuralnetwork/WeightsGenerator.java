package raccoon.neuralnetwork;

import raccoon.neuralnetwork.Link.Weight;

interface WeightsGenerator {
    Weight next();
}
