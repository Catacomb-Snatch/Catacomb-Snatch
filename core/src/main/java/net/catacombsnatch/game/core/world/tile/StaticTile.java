package net.catacombsnatch.game.core.world.tile;

import net.catacombsnatch.game.core.resource.Art;
import net.catacombsnatch.game.core.world.level.Level;
import net.catacombsnatch.game.core.world.level.View;
import net.catacombsnatch.game.core.world.tile.tiles.*;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
	public void render(SpriteBatch graphics, View view) {
		renderSprite(sprite, graphics, view);
	}

	@Override
	public void tick(float delta) {
		// This is static, do nothing
	}
	
	Sprite hole;
	protected void renderSprite(Sprite sprite, SpriteBatch graphics, View view) {
		graphics.draw(sprite.getTexture(),
			sprite.getX() - view.getOffset().x,
			sprite.getY() - view.getOffset().y,
			sprite.getRegionX(),
			sprite.getRegionY(),
			sprite.getRegionWidth(),
			sprite.getRegionHeight()
		);
		if (level.getTile((int)bb.x, (int)bb.y-1) == null) {
			if (hole == null) {
				if	(this instanceof FloorTile || this instanceof SandTile) {
					hole = sprite;
				}
				if (this instanceof DestroyableWallTile) {
					Class destroy = destroy();
					TextureRegion[] source = null;
					TextureRegion region = null;
					if (destroy.equals(FloorTile.class)) {
						source = Art.tiles_floor;
					}
					if (destroy.equals(SandTile.class)) {
						source = new TextureRegion[] {Art.tiles_sand[0]};
					}
					if (source != null) {
						region = source[level.getGenerator().randomizer().nextInt(source.length)];
						hole = new Sprite();
						hole.setRegion(region);
						hole.setSize(region.getRegionWidth(), region.getRegionHeight());
					}
				}
				if (this instanceof WallTile) {
					TextureRegion[] source = Art.tiles_floor;
					TextureRegion region = null;
					region = source[level.getGenerator().randomizer().nextInt(source.length)];
					hole = new Sprite();
					hole.setRegion(region);
					hole.setSize(region.getRegionWidth(), region.getRegionHeight());
				}
			}
			if (hole != null) {
				graphics.draw(hole.getTexture(),
					sprite.getX() - view.getOffset().x,
					sprite.getY() - view.getOffset().y - HEIGHT,
					hole.getRegionX(),
					hole.getRegionY(),
					hole.getRegionWidth(),
					hole.getRegionHeight()
				);
				graphics.draw(Art.tiles_hole,
					sprite.getX() - view.getOffset().x,
					sprite.getY() - view.getOffset().y - HEIGHT,
					0,
					0,
					WIDTH,
					HEIGHT
				);
			}
		}
	}
	
}
