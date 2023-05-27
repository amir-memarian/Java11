package player;

import java.util.Objects;

public class Player {
    private final String name;
    private final Integer goal;

    public Player(String name, Integer goal) {
        this.name = name;
        this.goal = goal;
    }

    public String getName() {
        return name;
    }

    public Integer getGoal() {
        return goal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return Objects.equals(name, player.name) && Objects.equals(goal, player.goal);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, goal);
    }

    @Override
    public String toString() {
        return "Player{" +
                "name='" + name + '\'' +
                ", goal=" + goal +
                '}';
    }
}
