package net.catacombsnatch.game.core.world.level;

import net.catacombsnatch.game.core.entity.systems.HealthSystem;
import net.catacombsnatch.game.core.screen.Tickable;
import net.catacombsnatch.game.core.util.Finishable;
import net.catacombsnatch.game.core.world.level.generator.LevelGenerator;
import net.catacombsnatch.game.core.world.tile.Tile;

import com.artemis.EntitySystem;
import com.artemis.World;

public class Level extends World implements Tickable, Finishable {
	// Entity management
	protected EntitySystem healthSystem;
	protected EntitySystem renderSystem;
	
	// Tiles
	protected Tile[] tiles;
	
	// General level information
	protected LevelGenerator generator;
	
	protected int width, height;
	
	protected boolean debug = false;
	protected boolean finished = false;

	public Level(LevelGenerator generator, int width, int height) {
		super();

		// Entity management
		healthSystem = new HealthSystem();
		renderSystem = new HealthSystem();
		
		// Tiles
		this.tiles = new Tile[width * height];
		
		// General level information
		this.generator = generator;
		
		this.width = width;
		this.height = height;
	}
	
	public void initialize() {		
		// Add systems
		getSystemManager().setSystem(healthSystem);
		getSystemManager().setSystem(renderSystem);
		
		getSystemManager().initializeAll();
	}
	
	@Override
	public void tick(float delta) {
		// Tick through tiles (for animations, ...)
		for(Tile tile : tiles) {
			if(tile == null) continue;
			
			tile.tick(delta);
		}
		
		// Update entities
		loopStart();
		
		setDelta((int) delta);
		
		// Process systems
		healthSystem.process();
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
	
	@Override
	public void setFinished(boolean finished) {
		this.finished = finished;
	}

	@Override
	public boolean hasFinished() {
		return finished;
	}
	
	/** @param debug True if debug mode should be activated. */
	public void setDebugMode(boolean debug) {
		this.debug = debug;
	}
	
	/** @return True if debug mode is activated, otherwise false. */
	public boolean isDebugModeActive() {
		return debug;
	}
	
	/** @return The level width <b>in tiles</b> */
	public int getWidth() {
		return width;
	}
	
	/** @return The level height <b>in tiles</b> */
	public int getHeight() {
		return height;
	}
	
	/** @return The {@link LevelGenerator} used to generate this level */
	public LevelGenerator getGenerator() {
		return generator;
	}
	
}
