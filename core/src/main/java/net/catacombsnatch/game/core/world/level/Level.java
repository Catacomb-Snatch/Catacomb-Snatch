package net.catacombsnatch.game.core.world.level;

import net.catacombsnatch.game.core.entity.Entity;
import net.catacombsnatch.game.core.entity.EntityComponent;
import net.catacombsnatch.game.core.entity.EntityManager;
import net.catacombsnatch.game.core.screen.Tickable;
import net.catacombsnatch.game.core.world.level.generator.LevelGenerator;
import net.catacombsnatch.game.core.world.tile.Tile;

public class Level implements Tickable {
	protected EntityManager entityManager;
	protected Tile[] tiles;
	
	protected LevelGenerator generator;
	
	protected int width, height;
	
	protected boolean debug = false;
	protected boolean finished = false;

	public Level(LevelGenerator generator, int width, int height) {
		this.generator = generator;
		
		entityManager = new EntityManager();
		tiles = new Tile[width * height];
		
		this.width = width;
		this.height = height;
	}
	
	@Override
	public void tick(float delta) {
		// Tick through tiles (for animations, ...)
		for(Tile tile : tiles) {
			if(tile == null) continue;
			
			tile.tick(delta);
		}
		
		// Tick through entities (regenerate, physics, ...)
		for(Entity entity : entityManager.getEntities()) {
			for(EntityComponent component : entity.getComponents()) {
				if(!(component instanceof Tickable)) continue;
				
				((Tickable) component).tick(delta);
			}
		}
	}

	/** @param debug True if debug information should be shown. */
	public void showDebug(boolean debug) {
		this.debug = debug;
	}
	
	/** @return True if debug information should be shown, otherwise false. */
	public boolean isDebug() {
		return debug;
	}
	
	/** @return An array of all stored tiles (size = level width * level height). */
	public Tile[] getTiles() {
		return tiles;
	}
	
	/**
	 * Gets a tile at the given x and y coordinate.
	 * If x and / or y are out of the level boundaries, null is getting returned.
	 * 
	 * @param x The x position
	 * @param y The y position
	 * @return The tile, or null when x and / or y are out of level boundaries.
	 */
	public Tile getTile(int x, int y) {
		return (x < 0 || y < 0 || x >= width || y >= height) ? null : tiles[x + y * width];
	}
	
	/**
	 * Sets a tile at the given x and y coordinate.
	 * If x and / or y are out of the level boundaries, nothing is being set.
	 * 
	 * @param Tile The tile object to set
	 * @param x The x position
	 * @param y The y position
	 */
	public void setTile(Tile tile, int x, int y) {
		if(x < 0 || y < 0 || x >= width || y >= height) return;
		
		tiles[x + y * width] = tile;
	}
	
	/** @return The level width <b>in tiles</b> */
	public int getWidth() {
		return width;
	}
	
	/** @return The level height <b>in tiles</b> */
	public int getHeight() {
		return height;
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
	
	/** @return The {@link EntityManager} for this level */
	public EntityManager getEntityManager() {
		return entityManager;
	}
	
	/** @return The {@link LevelGenerator} used to generate this level */
	public LevelGenerator getGenerator() {
		return generator;
	}
	
}
