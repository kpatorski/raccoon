package raccoon.neuralnetwork;

import java.util.Collection;
import java.util.Iterator;
import java.util.stream.Stream;

class ConnectLayers {
    private final WeightsGenerator weightGenerator;

    ConnectLayers(WeightsGenerator weightGenerator) {
        this.weightGenerator = weightGenerator;
    }

    void eachNeurons(Layer<InputNeuron> inputLayer,
                     HiddenLayers hiddenLayers,
                     Layer<OutputNeuron> outputLayer) {
        if (hiddenLayers.isEmpty()) {
            connectInputWithOutput(inputLayer, outputLayer);
        } else {
            connectInputWithHiddenLayers(inputLayer, hiddenLayers);
            connectHiddenLayers(hiddenLayers.iterator());
            connectHiddenLayersWithOutput(hiddenLayers, outputLayer);
        }
    }

    private void connectInputWithOutput(Layer<InputNeuron> inputLayer, Layer<OutputNeuron> outputLayer) {
        eachNeurons(inputLayer.neurons(), outputLayer.neurons());
    }

    private <T extends Transmitter, R extends Receiver> void eachNeurons(Collection<T> transmitters,
                                                                         Collection<R> receivers) {
        transmitters.forEach(input -> connectToEachReceiver(input, receivers.stream()));
    }

    private <R extends Receiver> void connectToEachReceiver(Transmitter transmitter, Stream<R> receivers) {
        receivers.forEach(receiver -> {
            Link link = Link.between(transmitter, receiver, weightGenerator.next());
            transmitter.addOutput(link);
            receiver.addInput(link);
        });
    }

    private void connectInputWithHiddenLayers(Layer<InputNeuron> inputLayer, HiddenLayers hiddenLayers) {
        eachNeurons(inputLayer.neurons(), hiddenLayers.first().neurons());
    }

    private void connectHiddenLayers(Iterator<Layer<HiddenNeuron>> layers) {
        while (layers.hasNext()) {
            Layer<HiddenNeuron> left = layers.next();
            if (layers.hasNext()) {
                eachNeurons(left.neurons(), layers.next().neurons());
            }
        }
    }

    private void connectHiddenLayersWithOutput(HiddenLayers hiddenLayers, Layer<OutputNeuron> outputLayer) {
        eachNeurons(hiddenLayers.last().neurons(), outputLayer.neurons());
    }
}
