package raccoon.shared;

import java.util.UUID;

import static java.util.UUID.randomUUID;

public record TaskId(UUID value) {
    public static TaskId newId() {
        return new TaskId(randomUUID());
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
