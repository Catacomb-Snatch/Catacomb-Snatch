package net.catacombsnatch.game.core.world.level.generator;

import net.catacombsnatch.game.core.world.level.Level;
import net.catacombsnatch.game.core.world.tile.Tile;
import net.catacombsnatch.game.core.world.tile.TileRegistry;

public class DebugLevelGenerator extends LevelGenerator {

	@Override
	public Level generate() {
		Level level = new Level(this, TileRegistry.getTypes().size(), 1);
		
		int i = 0;
		for(String type : TileRegistry.getTypes()) {
			Tile tile = TileRegistry.createFor(type, level, i, 0);
			if(tile != null) level.getTiles()[i] = tile;
			
			i++;
		}
		
		return level;
	}

}
