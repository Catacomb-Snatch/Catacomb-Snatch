package net.catacombsnatch.game.core.world.tile;

import net.catacombsnatch.game.core.scene.Scene;
import net.catacombsnatch.game.core.world.level.Level;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public abstract class StaticTile extends Tile {

	/** The sprite for this tile */
	protected Sprite sprite;
	
	
	protected StaticTile(Color color) {
		super(color);
		
		sprite = new Sprite();
	}
	
	protected void setTexture(TextureRegion region) {
		sprite.setRegion(region);
		sprite.setSize(region.getRegionWidth(), region.getRegionHeight());
	}
	
	protected void setRandomTexture(TextureRegion[] source) {
		setTexture(source[level.getGenerator().randomizer().nextInt(source.length)]);
	}
	
	/** @return The sprite for this tile, holding coordinates and texture. */
	public Sprite getSprite() {
		return sprite;
	}
	
	@Override
	public void init(Level level, int x, int y) {
		super.init(level, x, y);
		
		sprite.setPosition(x * WIDTH, y * HEIGHT);
	}

	@Override
	public void render(Scene scene) {
		sprite.draw(scene.getSpriteBatch());
	}

	@Override
	public void tick(float delta) {
		// This is static, do nothing
	}

}
