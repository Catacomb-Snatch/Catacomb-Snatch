package net.catacombsnatch.game.core.world.tile;

import net.catacombsnatch.game.core.world.level.Level;
import net.catacombsnatch.game.core.world.level.View;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public abstract class StaticTile extends Tile {

	/** The texture(region) for this tile */
	protected TextureRegion region;
	
	
	protected StaticTile(int color) {
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
		this.position = new Vector2(x, y);
		this.bb = new Rectangle(x * WIDTH, y * HEIGHT, region.getRegionHeight(), region.getRegionWidth());
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
		// Do nothing in here...
	}
	
	@Override
	public Rectangle getBounds() {
		return bb;
	}
	
	@Override
	public boolean shouldRender(View view) {
		return view.getViewportOffset().overlaps(new Rectangle(
				getBounds().x, 
				getBounds().y - region.getRegionHeight(),
				getBounds().width,
				region.getRegionHeight()*2));
	}
	
	protected void renderTile(SpriteBatch graphics, View view, TextureRegion tile) {
		graphics.draw(tile, bb.x - view.getOffset().x, view.getViewport().height - (bb.y + view.getOffset().y));
	}
	
}
