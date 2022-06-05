package raccoon.team.create;

interface TeamRepository {
    boolean existByName(String name);

    TeamRepository add(Team team);
}
