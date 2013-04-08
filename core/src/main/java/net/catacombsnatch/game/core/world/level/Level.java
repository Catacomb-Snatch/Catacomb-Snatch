package net.catacombsnatch.game.core.world.level;

import net.catacombsnatch.game.core.entity.Entity;
import net.catacombsnatch.game.core.entity.EntityComponent;
import net.catacombsnatch.game.core.entity.EntityManager;
import net.catacombsnatch.game.core.screen.Tickable;
import net.catacombsnatch.game.core.world.tile.Tile;

import com.badlogic.gdx.utils.Array;

public class Level implements Tickable {
	protected EntityManager entityManager;
	protected Array<Tile> tiles;
	
	protected LevelGenerator generator;
	
	protected boolean debug = false;
	protected boolean finished = false;

	public Level(LevelGenerator generator) {
		this.generator = generator;
		
		entityManager = new EntityManager();
		tiles = new Array<Tile>();
	}
	
	@Override
	public void tick() {
		// Tick through tiles (for animations, ...)
		for(Tile tile : tiles) {
			tile.tick();
		}
		
		// Tick through entities (regenerate, physics, ...)
		for(Entity entity : entityManager.getEntities()) {
			for(EntityComponent component : entity.getComponents()) {
				if(!(component instanceof Tickable)) continue;
				
				((Tickable) component).tick();
			}
		}
	}

	/** @param debug True if debug information should be shown. */
	public void showDebug(boolean debug) {
		this.debug = debug;
	}
	
	/** @return A list of all stored level layers. */
	public Array<Tile> getTiles() {
		return tiles;
	}
	
	/**
	 * Sets whether or not the level is finished.
	 * 
	 * @param finished True if finished, false if not
	 */
	public void setFinished(boolean finished) {
		this.finished = finished;
	}
	
	/** @return True if the level is finished, otherwise false */
	public boolean hasFinished() {
		return finished;
	}
	
	
	/** @return The {@link LevelGenerator} used to generate this level */
	public LevelGenerator getGenerator() {
		return generator;
	}
	
}
