package net.catacombsnatch.game.core.entity.components;

import net.catacombsnatch.game.core.entity.EntityComponent;
import net.catacombsnatch.game.core.entity.EntityManager;
import net.catacombsnatch.game.core.scene.Scene;
import net.catacombsnatch.game.core.screen.Renderable;

import com.badlogic.gdx.graphics.g2d.Sprite;

public class Render extends EntityComponent implements Renderable {
	protected final Sprite sprite;

	public Render( EntityManager manager, long id, Sprite sprite ) {
		super(manager, id);
		
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
