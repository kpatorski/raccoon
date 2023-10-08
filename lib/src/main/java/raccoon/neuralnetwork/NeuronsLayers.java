package raccoon.neuralnetwork;

import lombok.Data;
import lombok.NonNull;
import raccoon.neuralnetwork.activationfunction.ActivationFunction;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

class NeuronsLayers {
    private final List<NeuronLayer> layers = new ArrayList<>();

    NeuronsLayers(List<NeuronLayer> layers) {
        this.layers.addAll(layers);
    }

    static NeuronsLayers empty() {
        return new NeuronsLayers(new ArrayList<>());
    }

    NeuronsLayers add(long numberOfNeurons,
                      ActivationFunction function,
                      WeightsGenerator weightsGenerator) {
        var neuronLayer = NeuronLayer.of(numberOfNeurons, function, weightsGenerator);
        last(last -> last.connect(neuronLayer.neurons(), weightsGenerator));
        layers.add(neuronLayer);
        return this;
    }

    NeuronsLayers ifEmpty(Runnable runnable) {
        if (layers.isEmpty()) {
            runnable.run();
        }
        return this;
    }

    NeuronsLayers ifNotEmpty(Runnable runnable) {
        if (!layers.isEmpty()) {
            runnable.run();
        }
        return this;
    }

    NeuronsLayers first(Consumer<NeuronLayer> consumer) {
        return ifNotEmpty(() -> consumer.accept(layers.get(0)));
    }

    NeuronsLayers last(Consumer<NeuronLayer> consumer) {
        return ifNotEmpty(() -> consumer.accept(layers.get(layers.size() - 1)));
    }

    Iterator<NeuronLayer> iterator() {
        return layers.iterator();
    }

    void transmit() {
        iterator().forEachRemaining(NeuronLayer::transmit);
    }

    Snapshot toSnapshot() {
        return new Snapshot().layers(layers.stream().map(NeuronLayer::toSnapshot).toList());
    }

    @Data
    static class Snapshot {
        @NonNull
        private List<NeuronLayer.Snapshot> layers = new ArrayList<>();

        Stream<Link.Snapshot> links() {
            return layers.stream()
                    .flatMap(NeuronLayer.Snapshot::links);
        }

        Snapshot addLayer(@NonNull NeuronLayer.Snapshot layer) {
            layers.add(layer);
            return this;
        }
    }
}
