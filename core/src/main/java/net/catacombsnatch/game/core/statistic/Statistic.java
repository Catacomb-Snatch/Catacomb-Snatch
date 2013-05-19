package net.catacombsnatch.game.core.statistic;

import java.util.HashMap;
import java.util.Map;

import net.catacombsnatch.game.core.player.Player;

public class Statistic {
	
	protected final Map<Player, Integer> values;
	protected final String name;
	
	public Statistic(String name) {
		this.name = name;
		
		values = new HashMap<Player, Integer>();
	}	
	
	public int get(Player player) {
		return values.get(player);
	}
	
	public void increment(Player player) {
		values.put(player, get(player) + 1);
	}
	
	public void clear(Player player) {
		values.remove(player);
	}
}
