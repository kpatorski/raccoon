package raccoon.estimatetask;

import raccoon.shared.TaskId;

import java.util.Optional;

interface TaskRepository {
    Optional<Task> findById(TaskId id);
}
