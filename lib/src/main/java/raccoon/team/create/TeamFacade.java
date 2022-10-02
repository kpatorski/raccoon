package raccoon.team.create;

import com.sparrow.control.Result;
import raccoon.shared.TeamId;
import raccoon.shared.UserId;
import raccoon.shared.event.Event;
import raccoon.shared.event.EventsPublisher;
import raccoon.shared.result.Failure;

import java.time.Instant;

import static com.sparrow.control.Result.failure;
import static com.sparrow.control.Result.success;
import static java.lang.String.format;

public class TeamFacade {
    private final TeamRepository teamRepository;
    private final EventsPublisher eventsPublisher;

    TeamFacade(TeamRepository teamRepository,
               EventsPublisher eventsPublisher) {
        this.teamRepository = teamRepository;
        this.eventsPublisher = eventsPublisher;
    }

    public Result<TeamCreated, Failure> createTeam(CreateTeamCommand command) {
        if (teamRepository.existByName(command.name())) {
            return failure(new TeamNameAlreadyTaken(command.name()));
        }

        TeamCreated event = newTeam(command);
        eventsPublisher.publish(event);
        return success(event);
    }

    private TeamCreated newTeam(CreateTeamCommand command) {
        var team = Team.newTeam(command.name());
        teamRepository.add(team);
        return new TeamCreated(team.id(), command.userId());
    }

    record TeamNameAlreadyTaken(String teamName) implements Failure {
        @Override
        public String reason() {
            return format("Team name[%s] already taken", teamName);
        }
    }

    record TeamCreated(TeamId teamId, UserId byWho, Instant when) implements Event {
        private TeamCreated(TeamId teamUuid, UserId byWho) {
            this(new TeamId(teamUuid.uuid()), byWho, Instant.now());
        }

        @Override
        public Instant timestamp() {
            return when;
        }
    }
}
