package raccoon.estimatetask

import raccoon.shared.DeckId
import raccoon.shared.EstimationValueId
import raccoon.shared.TaskId
import raccoon.shared.UserId
import raccoon.shared.event.EventsPublisher
import spock.lang.Specification

import static java.util.Optional.*
import static raccoon.estimatetask.EstimateTask.*

class EstimateTaskTest extends Specification {
    def taskRepository = new InMemoryTaskRepository()
    def deckRepository = new InMemoryDeckRepository()
    def eventPublisher = Mock(EventsPublisher)
    def estimateTask = new EstimateTask(taskRepository, deckRepository, eventPublisher)

    def "estimation is not accepted if doesn't belong to task's deck"() {
        given: "deck A"
        def deckIdA = DeckId.newId()
        def estimationValueA = EstimationValueId.newId()
        deckRepository.add(deckIdA, [estimationValueA])

        and: "deck A"
        def deckIdB = DeckId.newId()
        def estimationValueB = EstimationValueId.newId()
        deckRepository.add(deckIdB, [estimationValueB])

        and: "task with deck A"
        def taskId = TaskId.newId()
        def task = new Task(taskId, deckIdA)
        taskRepository.add(task)

        and: "user"
        def userId = UserId.newId()

        when: "user estimates task at value B"
        def command = new Command(taskId, estimationValueB, userId)
        def failure = estimateTask.estimate(command).failure()

        then: "task is not estimated"
        failure.reason() == "Estimation[$estimationValueB] does not belongs to task's deck[$deckIdA]"
        0 * eventPublisher.publish(_)
    }

    def "estimation is not accepted if it does not exist"() {
        given: "deck"
        def deckId = DeckId.newId()
        def nonExistingValue = EstimationValueId.newId()

        and: "task"
        def taskId = TaskId.newId()
        taskRepository.add(new Task(taskId, deckId))

        and: "user"
        def userId = UserId.newId()

        when: "user estimates task at non existing value"
        def command = new Command(taskId, nonExistingValue, userId)
        def failure = estimateTask.estimate(command).failure()

        then: "task is not estimated"
        failure.reason() == "Estimation value[$nonExistingValue] does not exist"
        0 * eventPublisher.publish(_)
    }

    def "estimation is not accepted if task does not exist"() {
        given: "deck"
        def deckId = DeckId.newId()
        def estimationValueA = EstimationValueId.newId()
        def estimationValueB = EstimationValueId.newId()
        deckRepository.add(deckId, [estimationValueA, estimationValueB])

        and: "non-existing task"
        def taskId = TaskId.newId()

        and: "user"
        def userId = UserId.newId()

        when: "user estimates task at value A"
        def command = new Command(taskId, estimationValueA, userId)
        def failure = estimateTask.estimate(command).failure()

        then: "task is not estimated"
        failure.reason() == "Task[$taskId] does not exist"
        0 * eventPublisher.publish(_)
    }

    def "estimation is accepted"() {
        given: "deck"
        def deckId = DeckId.newId()
        def estimationValueA = EstimationValueId.newId()
        def estimationValueB = EstimationValueId.newId()
        deckRepository.add(deckId, [estimationValueA, estimationValueB])

        and: "task"
        def taskId = TaskId.newId()
        def task = new Task(taskId, deckId)
        taskRepository.add(task)

        and: "user"
        def userId = UserId.newId()

        when: "user estimates task at value A"
        def command = new Command(taskId, estimationValueA, userId)
        def event = estimateTask.estimate(command).success()

        then: "task is estimated by user at value A"
        event.task() == taskId
        event.value() == estimationValueA
        1 * eventPublisher.publish(_ as Task.TaskEstimated)
    }

    private static class InMemoryTaskRepository implements TaskRepository {
        private final Collection<Task> tasks = []

        InMemoryTaskRepository add(Task task) {
            tasks.add(task)
            this
        }

        @Override
        Optional<Task> findById(TaskId id) {
            ofNullable(tasks.find { it.isOfId(id) })
        }
    }

    private static class InMemoryDeckRepository implements DeckRepository {
        private final Map<DeckId, Collection<EstimationValue>> estimationValuesByDeck = [:]

        InMemoryDeckRepository add(DeckId deckId, Collection<EstimationValueId> ids) {
            def values = ids.collect { id -> new EstimationValue(deckId, id) }
            def estimations = estimationValuesByDeck.getOrDefault(deckId, [])
            estimationValuesByDeck.put(deckId, estimations + values)
            this
        }

        @Override
        Optional<EstimationValue> findEstimationValueById(EstimationValueId id) {
            ofNullable(allValues().find { it -> it.isOfId(id) })
        }

        private Collection<EstimationValue> allValues() {
            estimationValuesByDeck.values().flatten() as Collection<EstimationValue>
        }
    }
}
