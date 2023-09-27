package raccoon.neuralnetwork;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import static java.util.UUID.randomUUID;

class Link {
    private final UUID id;
    private Signal incommingSignal = Signal.zero();
    private Signal outgoingSignal = incommingSignal;
    private Weight weight;

    private Link(UUID id, Weight weight) {
        this.id = id;
        this.weight = weight;
    }

    static Link ofWeight(Weight weight) {
        return new Link(randomUUID(), weight);
    }

    void transmit(Signal signal) {
        incommingSignal = signal;
        outgoingSignal = multiply(weight, incommingSignal);
    }

    private static Signal multiply(Weight weight, Signal signal) {
        return signal.multiply(weight.value());
    }

    Signal outgoingSignal() {
        return outgoingSignal;
    }

    Link setWeight(Weight weight) {
        this.weight = weight;
        return this;
    }

    Weight weight() {
        return weight;
    }

    static List<Snapshot> toSnapshot(Collection<Link> links) {
        return links.stream().map(Link::toSnapshot).toList();
    }

    Snapshot toSnapshot() {
        return new Snapshot(id, weight.value);
    }

    static Link fromSnapshot(Snapshot snapshot) {
        return new Link(snapshot.id, new Weight(snapshot.weight));
    }

    record Weight(double value) {
        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }

    record Snapshot(UUID id, double weight) {
        @Override
        public String toString() {
            return "Link[" +
                    "id=" + id +
                    ", weight=" + weight +
                    ']';
        }
    }
}
