package raccoon.tag;

import raccoon.shared.TaskId;

interface TaskRepository {
    boolean doesExist(TaskId id);
}
