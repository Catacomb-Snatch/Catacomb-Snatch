package net.catacombsnatch.game.core.world.level.generator;

import net.catacombsnatch.game.core.world.level.Level;
import net.catacombsnatch.game.core.world.level.LevelGenerator;
import net.catacombsnatch.game.core.world.tile.Tile;
import net.catacombsnatch.game.core.world.tile.TileRegistry;

import com.badlogic.gdx.Gdx;

public class DebugLevelGenerator extends LevelGenerator {

	@Override
	public Level generate() {
		Level level = new Level(this, TileRegistry.getTypes().size(), 1);
		
		int i = 0;
		for(String type : TileRegistry.getTypes()) {
			try {
				Tile t = TileRegistry.getByName(type).newInstance();
				t.init(level, i, 0);
				
				level.getTiles()[i] = t;
				
			} catch (Exception e) {
				Gdx.app.error("DebugLevelGenerator", "Could not add tile with id: " + type, e);
			}
			
			i++;
		}
		
		return level;
	}

}
