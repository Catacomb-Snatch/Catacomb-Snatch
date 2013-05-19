package net.catacombsnatch.game.core.statistic;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.catacombsnatch.game.core.entity.Player;

public class Statistic {
	
	protected final Map<UUID, Integer> values;
	protected final String name;
	
	public Statistic(String name) {
		this.name = name;
		
		values = new HashMap<UUID, Integer>();
	}	
	
	public int get(Player player) {
		return values.get(player.getUUID());
	}
	
	public void increment(Player player) {
		values.put(player.getUUID(), get(player) + 1);
	}
	
	public void clear(Player player) {
		values.remove(player);
	}
}