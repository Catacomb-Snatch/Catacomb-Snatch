package net.catacombsnatch.game.core.world.tile;

import net.catacombsnatch.game.core.world.Direction;
import net.catacombsnatch.game.core.world.level.Level;
import net.catacombsnatch.game.core.world.level.View;
import net.catacombsnatch.game.core.world.tile.tiles.HoleTile;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public abstract class StaticTile extends Tile {

	/** The texture(region) for this tile */
	protected TextureRegion region;
	
	
	protected StaticTile(Color color) {
		super(color);
	}
	
	protected void setTexture(TextureRegion region) {
		this.region = region;
	}
	
	protected void setRandomTexture(TextureRegion[] source) {
		this.region = source[level.getGenerator().randomizer().nextInt(source.length)];
	}
	
	/** @return The {@link TextureRegion} for this tile. */
	public TextureRegion getTextureRegion() {
		return region;
	}
	
	@Override
	public void init(Level level, int x, int y) {
		super.init(level, x, y);
	}

	@Override
	public void render(SpriteBatch graphics, View view) {
		renderTile(graphics, view, region);
	}

	@Override
	public void tick(float delta) {
		// This is static, do nothing
	}
	
	@Override
	public void update() {
		if(position.y + 1 < level.getHeight() && getRelative(Direction.SOUTH) != null) {
			HoleTile tile = new HoleTile();
			tile.init(level, (int) position.x, (int) position.y + 1);
			
			level.setTile(tile, (int) position.x, (int) position.y + 1);
		}
	}
	
	@Override
	public Rectangle getBounds() {
		return new Rectangle(position.x * WIDTH, position.y * HEIGHT, region.getRegionHeight(), region.getRegionWidth());
	}
	
	@Override
	public boolean shouldRender(View view) {
		return (view.getOffset().x / WIDTH < position.x && view.getOffset().y / HEIGHT < position.y);
	}
	
	protected void renderTile(SpriteBatch graphics, View view, TextureRegion tile) {
		graphics.draw(tile, (position.x * WIDTH) - view.getOffset().x, (position.y * HEIGHT) - view.getOffset().y);
	}
	
}
