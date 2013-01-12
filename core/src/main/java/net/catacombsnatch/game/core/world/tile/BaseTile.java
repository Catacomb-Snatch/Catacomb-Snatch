package net.catacombsnatch.game.core.world.tile;

import net.catacombsnatch.game.core.screen.Screen;
import net.catacombsnatch.game.core.world.level.Level;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class BaseTile extends Tile {

	public BaseTile( Level level, Texture tex ) {
		super( level, new Sprite( tex ) );
	}

	public void tick() {
	}

	public void render( Screen screen ) {
		getSprite().draw( screen.getGraphics() );
	}

}
