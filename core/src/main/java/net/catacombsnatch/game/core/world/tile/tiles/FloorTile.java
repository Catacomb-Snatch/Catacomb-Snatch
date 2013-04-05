package net.catacombsnatch.game.core.world.tile.tiles;

import net.catacombsnatch.game.core.entity.Entity;
import net.catacombsnatch.game.core.resources.Art;
import net.catacombsnatch.game.core.world.level.Level;
import net.catacombsnatch.game.core.world.tile.StaticTile;

import com.badlogic.gdx.graphics.Color;

public class FloorTile extends StaticTile {

	public FloorTile() {
		super(Color.WHITE);
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
