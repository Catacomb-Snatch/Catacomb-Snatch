package net.catacombsnatch.game.core.world.level;

import net.catacombsnatch.game.core.resources.Art;
import net.catacombsnatch.game.core.scene.Scene;
import net.catacombsnatch.game.core.screen.Renderable;
import net.catacombsnatch.game.core.screen.Screen;
import net.catacombsnatch.game.core.world.tile.Tile;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Minimap implements Renderable {
	protected Level level;
	protected Sprite sprite;
	
	protected Texture map;
	protected Pixmap pm;
	
	public Minimap(Level level) {
		this.level = level;
		
		sprite = new Sprite(Art.skin.getAtlas().findRegion("minimap-frame"));
		
		map = new Texture(new Pixmap( 40, 40, Pixmap.Format.RGBA8888 ), true);
		pm = new Pixmap(40, 40, Pixmap.Format.RGBA8888);
	}

	@Override
	public void render(Scene scene) {
		sprite.draw(scene.getSpriteBatch());
		
		for (Tile tile : level.getTiles()) {
			pm.drawPixel((int) tile.getBounds().x, (int) tile.getBounds().y, Color.rgba8888(tile.getMinimapColor()));
		}
		
		// TODO add entity icons
		
		map.draw(pm, 0, 0);
		scene.getSpriteBatch().draw(map, sprite.getX() + 6, sprite.getY() + 5, 80, 80);
	}
	
	public void update(boolean resize) {
		if(resize) {
			sprite.setPosition(Screen.getWidth() - sprite.getWidth() - 2, Screen.getHeight() - sprite.getHeight());
		}
	}
	
}
