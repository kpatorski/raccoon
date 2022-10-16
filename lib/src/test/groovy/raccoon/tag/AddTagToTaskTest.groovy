package raccoon.tag

import raccoon.shared.TaskId
import raccoon.shared.UserId
import raccoon.shared.event.EventsPublisher
import spock.lang.Specification

import static raccoon.tag.AddTagToTask.*

class AddTagToTaskTest extends Specification {
    def taskRepository = new InMemoryTaskRepository()
    def eventPublisher = Mock(EventsPublisher)
    def addTagToTask = new AddTagToTask(taskRepository, eventPublisher)

    def "tag is added to task"() {
        given: "task"
        def taskId = TaskId.newId()
        taskRepository.add(taskId)

        and:
        def tag = new Tag("any")
        def userId = UserId.newId()

        when:
        def command = new Command(taskId, tag, userId)
        def event = addTagToTask.addTag(command).success()

        then:
        event.task() == taskId
        event.tag() == tag.value()
        event.userId() == userId
        1 * eventPublisher.publish(_ as TaskWasTagged)
    }

    def "tag should not be added if task does not exist"() {
        given: "non-existing task"
        def taskId = TaskId.newId()

        and:
        def tag = new Tag("any")
        def userId = UserId.newId()

        when:
        def command = new Command(taskId, tag, userId)
        def failure = addTagToTask.addTag(command).failure()

        then:
        failure.reason() == "Task[$taskId] does not exist"
        0 * eventPublisher.publish(_ as TaskWasTagged)
    }

    private static class InMemoryTaskRepository implements TaskRepository {
        private final Collection<TaskId> tasks = []

        InMemoryTaskRepository add(TaskId task) {
            tasks.add(task)
            this
        }

        @Override
        boolean doesExist(TaskId id) {
            tasks.any { it == id }
        }
    }
}
