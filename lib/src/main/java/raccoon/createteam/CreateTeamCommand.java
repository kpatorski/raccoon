package raccoon.createteam;

import raccoon.shared.UserId;

public record CreateTeamCommand(UserId userId, String name) {
    public CreateTeamCommand(String userUuid, String name) {
        this(new UserId(userUuid), name);
    }
}
