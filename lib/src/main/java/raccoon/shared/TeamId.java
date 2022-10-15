package raccoon.shared;

import java.util.UUID;

import static java.util.UUID.randomUUID;

public record TeamId(UUID value) {
    public static TeamId newId() {
        return new TeamId(randomUUID());
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
