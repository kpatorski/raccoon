package raccoon.neuralnetwork.activationfunction;

public class ActivationFunctions {
    private static final ActivationFunction LINEAR = new LinearFunction();

    private ActivationFunctions() {
    }

    public static ActivationFunction linearFunction() {
        return LINEAR;
    }

    public static ActivationFunction of(FunctionId id) {
        return switch (id) {
            case LINEAR -> LINEAR;
        };
    }
}
