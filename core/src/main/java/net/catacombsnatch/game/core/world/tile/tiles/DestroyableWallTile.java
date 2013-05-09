package net.catacombsnatch.game.core.world.tile.tiles;

import net.catacombsnatch.game.core.resource.Art;
import net.catacombsnatch.game.core.world.level.Level;
import net.catacombsnatch.game.core.world.tile.StaticTile;

public class DestroyableWallTile extends StaticTile {

	public DestroyableWallTile() {
		super(getColor(Art.tiles_walls[0]));
	}
	
	@Override
	public void init(Level level, int x, int y) {
		this.level = level;
		setTexture(Art.tiles_walls[0]);
		
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

	@Override
	public Class<FloorTile> destroy() {
		return FloorTile.class;
	}
	
}
