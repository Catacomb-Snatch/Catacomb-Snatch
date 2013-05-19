package net.catacombsnatch.game.core.world.level.generator;

import net.catacombsnatch.game.core.world.Campaign;
import net.catacombsnatch.game.core.world.level.Level;
import net.catacombsnatch.game.core.world.tile.Tile;
import net.catacombsnatch.game.core.world.tile.TileRegistry;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class DebugLevelGenerator extends LevelGenerator {

	@Override
	public Level generate(Campaign campaign) {
		Level level = new Level(campaign, this, TileRegistry.getTypes().size(), 1);
		
		int i = 0;
		for(String type : TileRegistry.getTypes()) {
			Tile tile = TileRegistry.createFor(type, level, i, 0);
			if(tile != null) level.getTiles()[i] = tile;
			
			i++;
		}
		
		return level;
	}

	@Override
	public Array<Vector2> getSpawnLocations() {
		return new Array<Vector2>(new Vector2[] { new Vector2(0, 0) });
	}

}
