package net.catacombsnatch.game.core.world.tile.tiles;

import net.catacombsnatch.game.core.entity.Entity;
import net.catacombsnatch.game.core.resource.Art;
import net.catacombsnatch.game.core.world.level.Level;
import net.catacombsnatch.game.core.world.tile.StaticTile;

public class FloorTile extends StaticTile {

	public FloorTile() {
		super(getColor(Art.tiles_floor[0]));
	}
	
	@Override
	public void init(Level level, int x, int y) {
		super.init(level, x, y);
		
		setRandomTexture(Art.tiles_floor);
	}

	@Override
	public boolean canPass(Entity entity) {
		return true;
	}

}
