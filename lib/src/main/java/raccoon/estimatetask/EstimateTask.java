package raccoon.estimatetask;

import com.sparrow.control.Result;
import raccoon.shared.EstimationValueId;
import raccoon.shared.TaskId;
import raccoon.shared.UserId;
import raccoon.shared.event.EventsPublisher;
import raccoon.shared.result.Failure;

import static java.lang.String.format;

public class EstimateTask {
    private final TaskRepository taskRepository;
    private final DeckRepository deckRepository;
    private final EventsPublisher eventsPublisher;

    EstimateTask(TaskRepository taskRepository, DeckRepository deckRepository, EventsPublisher eventsPublisher) {
        this.taskRepository = taskRepository;
        this.deckRepository = deckRepository;
        this.eventsPublisher = eventsPublisher;
    }

    public Result<Task.TaskEstimated, Failure> estimate(Command command) {
        return findTask(command.task)
                .map(task -> estimate(task, command));
    }

    private Result<Task, Failure> findTask(TaskId taskId) {
        return taskRepository.findById(taskId)
                .map(Result::<Task, Failure>success)
                .orElse(Result.failure(new TaskDoesNotExitFailure(taskId)));
    }

    private Result<Task.TaskEstimated, Failure> estimate(Task task, Command command) {
        return findValue(command.estimation)
                .map(estimation -> task.estimate(estimation, command.user))
                .ifSuccess(eventsPublisher::publish);
    }

    private Result<EstimationValue, Failure> findValue(EstimationValueId estimationValueId) {
        return deckRepository.findEstimationValueById(estimationValueId)
                .map(Result::<EstimationValue, Failure>success)
                .orElse(Result.failure(new EstimationValueDoesNotExist(estimationValueId)));
    }

    public record Command(TaskId task, EstimationValueId estimation, UserId user) {
    }

    record TaskDoesNotExitFailure(TaskId task) implements Failure {

        @Override
        public String reason() {
            return format("Task[%s] does not exist", task);
        }
    }

    record EstimationValueDoesNotExist(EstimationValueId id) implements Failure {

        @Override
        public String reason() {
            return format("Estimation value[%s] does not exist", id);
        }
    }
}
