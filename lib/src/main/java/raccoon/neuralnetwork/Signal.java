package raccoon.neuralnetwork;

import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public record Signal(double value) {

    public Signal add(Signal another) {
        return new Signal(this.value + another.value);
    }

    public Signal multiply(double multiplier) {
        return new Signal(this.value * multiplier);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public static Collector<Signal, Signal, Signal> sum() {
        return new Collector<>() {
            private Signal sum = new Signal(0);

            @Override
            public Supplier<Signal> supplier() {
                return () -> sum;
            }

            @Override
            public BiConsumer<Signal, Signal> accumulator() {
                return (signal, signal2) -> sum = sum.add(signal2);
            }

            @Override
            public BinaryOperator<Signal> combiner() {
                return (signal, signal2) -> sum = signal.add(signal2);
            }

            @Override
            public Function<Signal, Signal> finisher() {
                return signal -> sum.add(signal);
            }

            @Override
            public Set<Characteristics> characteristics() {
                return Set.of(Characteristics.UNORDERED);
            }
        };
    }
}
