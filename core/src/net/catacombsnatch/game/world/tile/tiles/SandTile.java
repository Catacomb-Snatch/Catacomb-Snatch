package net.catacombsnatch.game.world.tile.tiles;

import net.catacombsnatch.game.resource.Art;
import net.catacombsnatch.game.world.level.Level;
import net.catacombsnatch.game.world.tile.StaticTile;

public class SandTile extends StaticTile {

	public SandTile() {
		super(getColor(Art.tiles_sand[0]));
	}
	
	@Override
	public void init(Level level, int x, int y) {
		this.level = level;
		setTexture(Art.tiles_sand[0]);
		
		super.init(level, x, y);
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
