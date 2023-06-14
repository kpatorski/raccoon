package raccoon.neuralnetwork;

import java.util.Collection;
import java.util.Iterator;
import java.util.stream.Stream;

class ConnectLayers {
    private final WeightsGenerator weightGenerator;

    ConnectLayers(WeightsGenerator weightGenerator) {
        this.weightGenerator = weightGenerator;
    }

    void eachNeurons(InputLayer inputLayer,
                     NeuronsLayers neuronsLayers,
                     OutputLayer outputLayer) {
        if (neuronsLayers.isEmpty()) {
            connectInputWithOutput(inputLayer, outputLayer);
        } else {
            connectInputWithNeuronLayers(inputLayer, neuronsLayers);
            connectNeuronLayers(neuronsLayers.iterator());
            connectNeuronsLayersWithOutput(neuronsLayers, outputLayer);
        }
    }

    private void connectInputWithOutput(InputLayer inputLayer, OutputLayer outputLayer) {
        eachNeurons(inputLayer.inputs(), outputLayer.outputs());
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

    private void connectInputWithNeuronLayers(InputLayer inputLayer, NeuronsLayers neuronsLayers) {
        eachNeurons(inputLayer.inputs(), neuronsLayers.first().neurons());
    }

    private void connectNeuronLayers(Iterator<NeuronLayer> layers) {
        while (layers.hasNext()) {
            NeuronLayer left = layers.next();
            if (layers.hasNext()) {
                eachNeurons(left.neurons(), layers.next().neurons());
            }
        }
    }

    private void connectNeuronsLayersWithOutput(NeuronsLayers neuronsLayers, OutputLayer outputLayer) {
        eachNeurons(neuronsLayers.last().neurons(), outputLayer.outputs());
    }
}
