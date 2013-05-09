package net.catacombsnatch.game.core.world.tile.tiles;

import net.catacombsnatch.game.core.resource.Art;
import net.catacombsnatch.game.core.world.level.Level;
import net.catacombsnatch.game.core.world.tile.StaticTile;

public class SandTile extends StaticTile {

	public SandTile() {
		super(getColor(Art.tiles_sand[0]));
	}
	
	@Override
	public void init(Level level, int x, int y) {
		super.init(level, x, y);
		
		setTexture(Art.tiles_sand[0]);
	}

	@Override
	public void update() {
		// Nothing to do ... yet
	}

	@Override
	public boolean canPass(long entity) {
		return true;
	}

}
