package net.catacombsnatch.game.core.entity.components;

import net.catacombsnatch.game.core.entity.EntityComponent;
import net.catacombsnatch.game.core.scene.Scene;
import net.catacombsnatch.game.core.screen.Renderable;

import com.badlogic.gdx.graphics.g2d.Sprite;

public class Render implements EntityComponent, Renderable {
	protected final Sprite sprite;

	public Render( Sprite sprite ) {
		this.sprite = sprite;
	}

	@Override
	public void render( Scene scene ) {
		sprite.draw( scene.getSpriteBatch() );
	}

	public Sprite getSprite() {
		return this.sprite;
	}

}
