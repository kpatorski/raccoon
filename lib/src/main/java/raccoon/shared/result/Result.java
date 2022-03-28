package raccoon.shared.result;

import io.vavr.control.Option;

import java.util.function.Consumer;

public class Result<TSuccess, TFailure> {
    private final TSuccess success;
    private final TFailure failure;

    private Result(TSuccess success, TFailure failure) {
        this.success = success;
        this.failure = failure;
    }

    public static <TSuccess, TFailure> Result<TSuccess, TFailure> success(TSuccess success) {
        return new Result<>(success, null);
    }

    public static <TSuccess, TFailure> Result<TSuccess, TFailure> emptySuccess() {
        return new Result<>(null, null);
    }

    public static <TSuccess, TFailure> Result<TSuccess, TFailure> failure(TFailure failure) {
        return new Result<>(null, failure);
    }

    public boolean isSuccess() {
        return !isFailure();
    }

    public boolean isFailure() {
        return failure != null;
    }

    public Result<TSuccess, TFailure> onSuccess(Consumer<TSuccess> consumer) {
        if (isSuccess()) {
            consumer.accept(success);
        }
        return this;
    }

    public Result<TSuccess, TFailure> onFailure(Consumer<TFailure> consumer) {
        if (isFailure()) {
            consumer.accept(failure);
        }
        return this;
    }

    public Option<TSuccess> success() {
        return Option.of(success);
    }

    public Option<TFailure> failure() {
        return Option.of(failure);
    }
}
