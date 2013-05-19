package net.catacombsnatch.game.core.entity.renderers;

import com.artemis.Entity;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class TileRenderer extends Renderer {
	/** The texture(region) for this tile */
	protected TextureRegion region;
	
	public TileRenderer(Entity entity) {
		super(entity);
	}

	@Override
	public void render(SpriteBatch graphics) {
		
	}
	
	/**
	 * Sets the texture to render
	 * 
	 * @param texture The new {@link TextureRegion}
	 */
	public void setTexture(TextureRegion texture) {
		region = texture;
	}
	
	/** @return The {@link TextureRegion} used for rendering */
	public TextureRegion getTexture() {
		return region;
	}

}
