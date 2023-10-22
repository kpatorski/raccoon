package raccoon.neuralnetwork;

import lombok.Getter;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import static java.util.UUID.randomUUID;

class Link {
    private final UUID id;
    private Signal incommingSignal = Signal.zero();
    private Signal outgoingSignal = incommingSignal;
    @Getter
    private final Weight weight;

    private Link(UUID id, Weight weight) {
        this.id = id;
        this.weight = weight;
    }

    static Link between(Emitter emitter, Receiver receiver, Weight weight) {
        Link link = Link.of(weight);
        emitter.linkWithReceiver(link);
        receiver.linkWithEmitter(link);
        return link;
    }

    static Link of(Weight weight) {
        return new Link(randomUUID(), weight);
    }

    Link transmit(Signal signal) {
        incommingSignal = signal;
        outgoingSignal = multiply(weight, incommingSignal);
        return this;
    }

    private static Signal multiply(Weight weight, Signal signal) {
        return signal.multiply(weight.value());
    }

    Signal potential() {
        return outgoingSignal;
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
