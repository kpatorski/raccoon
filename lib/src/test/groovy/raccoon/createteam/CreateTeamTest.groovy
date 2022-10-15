package raccoon.createteam

import raccoon.shared.TeamId
import raccoon.shared.UserId
import raccoon.shared.event.EventsPublisher
import spock.lang.Specification

import static CreateTeam.TeamCreated
import static CreateTeam.TeamNameAlreadyTaken

class CreateTeamTest extends Specification {
    def eventsPublisher = Mock(EventsPublisher)
    def repository = new InMemoryTeamRepository()
    def createTeam = new CreateTeam(repository, eventsPublisher)

    def "should create a team"() {
        given:
        def userId = "6a142d89-7b67-4853-b9b1-691434a254d4"
        def command = new CreateTeamCommand(userId, "any name")

        when:
        def result = createTeam.createTeam(command)

        then: "team is created"
        def event = result.success()
        event.byWho() == new UserId(userId)

        and: "event is published"
        1 * eventsPublisher.publish(_ as TeamCreated)

        and: "team is persisted"
        with(repository.byUuid(event.teamId().value())) {
            it.name() == "any name"
        }
    }

    def "should not create a team with the same name already exist"() {
        given: "a team created once"
        def command = new CreateTeamCommand("6a142d89-7b67-4853-b9b1-691434a254d4", "anyName")
        createTeam.createTeam(command)

        when: "create another team with the same name"
        def result = createTeam.createTeam(command)

        then:
        result.failure() == new TeamNameAlreadyTaken("anyName")
    }

    private static class InMemoryTeamRepository implements TeamRepository {
        private final Collection<Team> teams = new ArrayList<>()

        private Team byUuid(UUID uuid) {
            teams.stream()
                    .filter(team -> team.id() == new TeamId(uuid))
                    .findFirst()
                    .orElse(null)
        }

        @Override
        boolean existByName(String name) {
            teams.stream()
                    .anyMatch(team -> team.name() == name)
        }

        @Override
        TeamRepository add(Team team) {
            this.teams.add(team)
            this
        }
    }
}
