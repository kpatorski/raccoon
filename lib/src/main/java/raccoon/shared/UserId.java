package raccoon.shared;

import java.util.UUID;

import static java.util.UUID.*;

public record UserId(UUID uuid) {
    public UserId(String uuid) {
        this(fromString(uuid));
    }
}
