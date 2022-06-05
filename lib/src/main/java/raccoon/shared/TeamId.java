package raccoon.shared;

import java.util.UUID;

import static java.util.UUID.randomUUID;

public record TeamId(UUID uuid) {
    public static TeamId newId() {
        return new TeamId(randomUUID());
    }
}
