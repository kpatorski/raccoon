package raccoon.neuralnetwork;

import java.util.Collection;
import java.util.Iterator;
import java.util.stream.Stream;

class ConnectLayers {
    private final WeightsGenerator weightGenerator;

    ConnectLayers(WeightsGenerator weightGenerator) {
        this.weightGenerator = weightGenerator;
    }

    void eachNeurons(Layer<Input> inputLayer,
                     HiddenLayers hiddenLayers,
                     Layer<Output> outputLayer) {
        if (hiddenLayers.isEmpty()) {
            connectInputWithOutput(inputLayer, outputLayer);
        } else {
            connectInputWithHiddenLayers(inputLayer, hiddenLayers);
            connectHiddenLayers(hiddenLayers.iterator());
            connectHiddenLayersWithOutput(hiddenLayers, outputLayer);
        }
    }

    private void connectInputWithOutput(Layer<Input> inputLayer, Layer<Output> outputLayer) {
        eachNeurons(inputLayer.neurons(), outputLayer.neurons());
    }

    private <E extends Emitter, R extends Receiver> void eachNeurons(Collection<E> emitters,
                                                                     Collection<R> receivers) {
        emitters.forEach(input -> connectToEachReceiver(input, receivers.stream()));
    }

    private <R extends Receiver> void connectToEachReceiver(Emitter emitter, Stream<R> receivers) {
        receivers.forEach(receiver -> {
            Link link = Link.between(emitter, receiver, weightGenerator.next());
            emitter.linkWithReceiver(link);
            receiver.linkWithEmitter(link);
        });
    }

    private void connectInputWithHiddenLayers(Layer<Input> inputLayer, HiddenLayers hiddenLayers) {
        eachNeurons(inputLayer.neurons(), hiddenLayers.first().neurons());
    }

    private void connectHiddenLayers(Iterator<Layer<Neuron>> layers) {
        while (layers.hasNext()) {
            Layer<Neuron> left = layers.next();
            if (layers.hasNext()) {
                eachNeurons(left.neurons(), layers.next().neurons());
            }
        }
    }

    private void connectHiddenLayersWithOutput(HiddenLayers hiddenLayers, Layer<Output> outputLayer) {
        eachNeurons(hiddenLayers.last().neurons(), outputLayer.neurons());
    }
}
