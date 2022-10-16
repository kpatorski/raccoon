package raccoon.tag;

import com.sparrow.control.Result;
import raccoon.shared.TaskId;
import raccoon.shared.UserId;
import raccoon.shared.event.Event;
import raccoon.shared.event.EventsPublisher;
import raccoon.shared.result.Failure;

import java.time.Instant;

import static java.lang.String.format;

public class AddTagToTask {

    private final TaskRepository repository;
    private final EventsPublisher eventsPublisher;

    AddTagToTask(TaskRepository repository, EventsPublisher eventsPublisher) {
        this.repository = repository;
        this.eventsPublisher = eventsPublisher;
    }

    public Result<TaskWasTagged, Failure> addTag(Command command) {
        if (!repository.doesExist(command.task)) {
            return Result.failure(new TaskDoesNotExitFailure(command.task));
        }
        var event = new TaskWasTagged(command.task, command.tag.value(), command.user);
        eventsPublisher.publish(event);
        return Result.success(event);
    }

    public record Command(TaskId task, Tag tag, UserId user) {
    }

    record TaskWasTagged(TaskId task, String tag, UserId userId, Instant when) implements Event {
        private TaskWasTagged(TaskId task, String tag, UserId userId) {
            this(task, tag, userId, Instant.now());
        }

        @Override
        public Instant timestamp() {
            return when;
        }
    }

    record TaskDoesNotExitFailure(TaskId task) implements Failure {

        @Override
        public String reason() {
            return format("Task[%s] does not exist", task);
        }
    }
}
