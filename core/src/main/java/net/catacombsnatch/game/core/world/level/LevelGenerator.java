package net.catacombsnatch.game.core.world.level;

import java.util.Random;

public abstract class LevelGenerator {
	protected Random random;
	
	
	public LevelGenerator() {
		this(new Random());
	}
	
	public LevelGenerator(Random r) {
		random = r;
	}
	
	
	public abstract Level generate();
	
	
	/** @return The random number generator for this level. */
	public Random randomizer() {
		return random;
	}
	
}
