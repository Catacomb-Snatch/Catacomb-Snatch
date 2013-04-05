package net.catacombsnatch.game.core.world.tile.tiles;

import java.util.Arrays;

import net.catacombsnatch.game.core.entity.Entity;
import net.catacombsnatch.game.core.resources.Art;
import net.catacombsnatch.game.core.world.level.Level;
import net.catacombsnatch.game.core.world.tile.StaticTile;

import com.badlogic.gdx.graphics.Color;

public class WallTile extends StaticTile {

	public WallTile() {
		super(Color.BLACK);
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

}
