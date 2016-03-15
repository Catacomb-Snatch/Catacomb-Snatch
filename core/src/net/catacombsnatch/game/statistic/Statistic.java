package net.catacombsnatch.game.statistic;

import com.badlogic.gdx.utils.ObjectIntMap;
import net.catacombsnatch.game.player.Player;

public class Statistic {

    protected final ObjectIntMap<Player> values;
    protected final String name;

    public Statistic(String name) {
        this.name = name;

        values = new ObjectIntMap<Player>();
    }

    public int get(Player player) {
        return values.get(player, -1);
    }

    public void increment(Player player) {
        values.put(player, get(player) + 1);
    }

    public void clear(Player player) {
        values.remove(player, -1);
    }
}
