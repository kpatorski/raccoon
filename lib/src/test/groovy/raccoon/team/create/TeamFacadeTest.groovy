package raccoon.team.create

import raccoon.shared.TeamId
import raccoon.shared.UserId
import raccoon.shared.event.EventsPublisher
import spock.lang.Specification

import static raccoon.team.create.TeamFacade.TeamCreated
import static raccoon.team.create.TeamFacade.TeamNameAlreadyTaken

class TeamFacadeTest extends Specification {
    def eventsPublisher = Mock(EventsPublisher)
    def repository = new InMemoryTeamRepository()
    def teamFacade = new TeamFacade(repository, eventsPublisher)

    def "should create a team"() {
        given:
        def userId = "6a142d89-7b67-4853-b9b1-691434a254d4"
        def command = new CreateTeamCommand(userId, "any name")

        when:
        def result = teamFacade.createTeam(command)

        then: "team is created"
        def event = result.success().get()
        event.byWho() == new UserId(userId)

        and: "event is published"
        1 * eventsPublisher.publish(_ as TeamCreated)

        and: "team is persisted"
        with(repository.byUuid(event.teamId().uuid())) {
            it.name() == "any name"
        }
    }

    def "should not create a team with the same name already exist"() {
        given: "a team created once"
        def command = new CreateTeamCommand("6a142d89-7b67-4853-b9b1-691434a254d4", "anyName")
        teamFacade.createTeam(command)

        when: "create another team with the same name"
        def result = teamFacade.createTeam(command)

        then:
        result.failure().get() == new TeamNameAlreadyTaken("anyName")
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
