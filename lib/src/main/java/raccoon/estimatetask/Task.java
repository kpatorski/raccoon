package raccoon.estimatetask;

import com.sparrow.control.Result;
import raccoon.shared.DeckId;
import raccoon.shared.EstimationValueId;
import raccoon.shared.TaskId;
import raccoon.shared.UserId;
import raccoon.shared.event.Event;
import raccoon.shared.result.Failure;

import java.time.Instant;

import static java.lang.String.format;

class Task {
    private final TaskId id;
    private final DeckId deckId;

    Task(TaskId id, DeckId deckId) {
        this.id = id;
        this.deckId = deckId;
    }

    boolean isOfId(TaskId id) {
        return this.id == id;
    }

    Result<TaskEstimated, Failure> estimate(EstimationValue estimation, UserId user) {
        if (estimation.belongsToDeck(deckId)) {
            return Result.success(new TaskEstimated(id, estimation.id(), user));
        }
        return Result.failure(new EstimationDoesNotBelongsToDeck(estimation.id(), deckId));
    }

    record TaskEstimated(TaskId task, EstimationValueId value, UserId byWho, Instant when) implements Event {
        private TaskEstimated(TaskId task, EstimationValueId value, UserId byWho) {
            this(new TaskId(task.value()), value, byWho, Instant.now());
        }

        @Override
        public Instant timestamp() {
            return when;
        }
    }

    record EstimationDoesNotBelongsToDeck(EstimationValueId valueId, DeckId deckId) implements Failure {

        @Override
        public String reason() {
            return format("Estimation[%s] does not belongs to task's deck[%s]", valueId, deckId);
        }
    }
}
