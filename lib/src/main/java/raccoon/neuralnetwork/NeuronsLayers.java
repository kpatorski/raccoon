package raccoon.neuralnetwork;

import lombok.Data;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

class NeuronsLayers {
    private final List<NeuronLayer> layers = new ArrayList<>();

    void add(NeuronLayer layer) {
        layers.add(layer);
    }

    boolean isEmpty() {
        return layers.isEmpty();
    }

    NeuronLayer first() {
        return layers.get(0);
    }

    NeuronLayer last() {
        return layers.get(layers.size() - 1);
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
