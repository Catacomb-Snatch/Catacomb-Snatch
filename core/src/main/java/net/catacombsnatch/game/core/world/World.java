package net.catacombsnatch.game.core.world;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import net.catacombsnatch.game.core.entity.entities.Player;
import net.catacombsnatch.game.core.world.level.Level;

public class World {
	protected List<Level> levels;
	protected List<Player> players;

	protected Difficulty difficulty;

	public World( Difficulty diff ) {
		levels = new LinkedList<Level>();
		players = new ArrayList<Player>();

		difficulty = diff;
	}

	/**
	 * Returns a list of levels inside this world.
	 * 
	 * @return A list of levels in this world
	 */
	public List<Level> getLevels() {
		return levels;
	}

	/**
	 * Returns a list of players inside this world
	 * 
	 * @return A list of players in this world
	 */
	public List<Player> getPlayers() {
		return players;
	}

	/**
	 * Returns the world's difficulty.
	 * 
	 * @return The {@link Difficulty}
	 */
	public Difficulty getDifficulty() {
		return difficulty;
	}
}
