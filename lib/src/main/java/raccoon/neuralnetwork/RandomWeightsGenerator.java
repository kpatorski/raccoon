package raccoon.neuralnetwork;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.random.RandomGenerator;

@NoArgsConstructor(access = AccessLevel.PACKAGE)
class RandomWeightsGenerator implements WeightsGenerator {
    private static final int MIN_WEIGHT = -1;
    private static final int MAX_WEIGHT = 1;

    @Override
    public Link.Weight next() {
        return new Link.Weight(RandomGenerator.getDefault().nextDouble(MIN_WEIGHT, MAX_WEIGHT));
    }

}
