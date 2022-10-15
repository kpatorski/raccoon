package raccoon.shared;

import java.util.UUID;

import static java.util.UUID.*;

public record UserId(UUID value) {
    public UserId(String uuid) {
        this(fromString(uuid));
    }

    public static UserId newId() {
        return new UserId(randomUUID());
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
