package raccoon.createteam;

import raccoon.shared.TeamId;

class Team {
    private final String name;
    private final TeamId id = TeamId.newId();

    private Team(String name) {
        this.name = name;
    }

    static Team newTeam(String name) {
        return new Team(name);
    }

    String name() {
        return name;
    }

    TeamId id() {
        return id;
    }
}
