package raccoon.estimatetask;

import raccoon.shared.DeckId;
import raccoon.shared.EstimationValueId;

record EstimationValue(DeckId deck, EstimationValueId id) {
    boolean isOfId(EstimationValueId id) {
        return this.id == id;
    }

    boolean belongsToDeck(DeckId deckId) {
        return deck == deckId;
    }
}
