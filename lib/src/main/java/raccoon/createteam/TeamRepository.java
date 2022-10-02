package raccoon.createteam;

interface TeamRepository {
    boolean existByName(String name);

    TeamRepository add(Team team);
}
