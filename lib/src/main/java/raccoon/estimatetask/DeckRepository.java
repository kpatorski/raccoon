package raccoon.estimatetask;

import raccoon.shared.EstimationValueId;

import java.util.Optional;

interface DeckRepository {
    Optional<EstimationValue> findEstimationValueById(EstimationValueId id);
}
