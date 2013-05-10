package net.catacombsnatch.game.core.world.level.generator;

import java.util.ArrayList;
import java.util.HashMap;

import net.catacombsnatch.game.core.world.Direction;
import net.catacombsnatch.game.core.world.level.Level;
import net.catacombsnatch.game.core.world.tile.Tile;
import net.catacombsnatch.game.core.world.tile.tiles.DestroyableWallTile;
import net.catacombsnatch.game.core.world.tile.tiles.FloorTile;
import net.catacombsnatch.game.core.world.tile.tiles.WallTile;

public class RandomLevelGenerator extends LevelGenerator {
	
	@Override
	public Level generate() {
		/*
		 * Some thoughts on generation:
		 * 
		 * - Divide up the level into a grid of cells (size is customizable).
		 * - Pick a center point in each cell.
		 * - Join each pair in the list of center points with corridors.
		 * - Build a room around each center point.
		 * - Fill each room with floor tiles and put wall tiles around it (maybe add some decoration?).
		 * - Where a room wall crosses a corridor replace with a destroyable wall tile.
		 * 
		 * This ensures the entire level will be connected.
		 */
		
		// TODO
		
		return null;
	}

}
