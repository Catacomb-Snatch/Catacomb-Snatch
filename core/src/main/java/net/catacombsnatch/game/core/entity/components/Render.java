package net.catacombsnatch.game.core.entity.components;

import net.catacombsnatch.game.core.screen.Renderable;
import net.catacombsnatch.game.core.screen.Screen;

import com.badlogic.gdx.graphics.g2d.Sprite;

public class Render implements EntityComponent, Renderable {
	private static final long serialVersionUID = 1L;

	protected final Sprite sprite;

	public Render( Sprite sprite ) {
		this.sprite = sprite;
	}

	@Override
	public void render( Screen screen ) {
		sprite.draw( screen.getGraphics() );
	}

	public Sprite getSprite() {
		return this.sprite;
	}

}
