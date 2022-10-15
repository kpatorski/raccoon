package raccoon.shared;

import java.util.UUID;

import static java.util.UUID.randomUUID;

public record DeckId(UUID value) {
    public static DeckId newId() {
        return new DeckId(randomUUID());
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
