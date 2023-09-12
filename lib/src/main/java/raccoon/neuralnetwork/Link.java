package raccoon.neuralnetwork;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import static java.util.UUID.randomUUID;

class Link {
    private Signal incommingSignal = Signal.zero();
    private Signal outgoingSignal = incommingSignal;
    private Weight weight;

    private Link(Weight weight) {
        this.weight = weight;
    }

    static Link ofWeight(Weight weight) {
        return new Link(weight);
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

    Snapshot toSnapshot() {
        return new Snapshot(weight.value);
    }

    static List<Snapshot> toSnapshot(Collection<Link> links) {
        return links.stream().map(Link::toSnapshot).toList();
    }

    record Weight(double value) {
        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }

    record Snapshot(UUID id, double weight) {
        Snapshot(double weight) {
            this(randomUUID(), weight);
        }
    }
}
