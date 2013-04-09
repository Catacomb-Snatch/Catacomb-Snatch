package net.catacombsnatch.game.core.world.tile.tiles;

import java.util.Arrays;

import net.catacombsnatch.game.core.entity.Entity;
import net.catacombsnatch.game.core.resource.Art;
import net.catacombsnatch.game.core.world.level.Level;
import net.catacombsnatch.game.core.world.tile.StaticTile;

public class WallTile extends StaticTile {

	public WallTile() {
		super(getColor(Art.tiles_walls[1]));
	}
	
	@Override
	public void init(Level level, int x, int y) {
		super.init(level, x, y);
		
		setRandomTexture(Arrays.copyOfRange(Art.tiles_walls, 1, Art.tiles_walls.length));
	}

	@Override
	public boolean canPass(Entity entity) {
		return false;
	}

	@Override
	public void update() {
		// Nothing to do ... yet
	}

}
