package raccoon.shared;

import java.util.UUID;

import static java.util.UUID.randomUUID;

public record EstimationValueId(UUID value) {
    public static EstimationValueId newId() {
        return new EstimationValueId(randomUUID());
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
