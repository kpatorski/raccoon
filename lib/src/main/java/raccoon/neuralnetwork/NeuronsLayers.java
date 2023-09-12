package raccoon.neuralnetwork;

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
        return new Snapshot(layers.stream().map(NeuronLayer::toSnapshot).toList());
    }

    record Snapshot(List<NeuronLayer.Snapshot> layers) {
        Stream<Link.Snapshot> links() {
            return layers.stream()
                    .flatMap(NeuronLayer.Snapshot::links);
        }
    }
}
