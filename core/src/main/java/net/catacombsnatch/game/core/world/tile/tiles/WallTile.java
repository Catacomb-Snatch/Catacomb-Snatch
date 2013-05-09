package net.catacombsnatch.game.core.world.tile.tiles;

import java.util.Arrays;

import net.catacombsnatch.game.core.resource.Art;
import net.catacombsnatch.game.core.world.level.Level;
import net.catacombsnatch.game.core.world.tile.StaticTile;

public class WallTile extends StaticTile {

	public WallTile() {
		super(getColor(Art.tiles_walls[1]));
	}
	
	@Override
	public void init(Level level, int x, int y) {
		this.level = level;
		setRandomTexture(Arrays.copyOfRange(Art.tiles_walls, 1, Art.tiles_walls.length));
		
		super.init(level, x, y);
	}

	@Override
	public boolean canPass(long entity) {
		return false;
	}

	@Override
	public void update() {
		// Nothing to do ... yet
	}

}
