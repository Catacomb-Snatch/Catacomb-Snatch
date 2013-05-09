package net.catacombsnatch.game.core.world.level;

import net.catacombsnatch.game.core.resource.Art;
import net.catacombsnatch.game.core.scene.Scene;
import net.catacombsnatch.game.core.screen.Renderable;
import net.catacombsnatch.game.core.screen.Screen;
import net.catacombsnatch.game.core.world.tile.Tile;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;

public class Minimap implements Renderable {
	protected Level level;
	protected View view;
	protected Sprite sprite;
	
	protected Texture map;
	protected Pixmap pm;
	
	public Minimap(Level level, View view) {
		this.level = level;
		this.view = view;
		
		sprite = new Sprite(Art.skin.getAtlas().findRegion("minimap-frame"));
		
		map = new Texture(new Pixmap( 40, 40, Pixmap.Format.RGBA8888 ), true);
	}
	
	Rectangle vp = new Rectangle();
	
	@Override
	public void render(Scene scene) {
		sprite.draw(scene.getSpriteBatch());
		
		vp.set(view.viewport);
		vp.x += view.offset.x;
		vp.y += view.offset.y;
		
		vp.x -= Screen.getWidth()/2;
		vp.y -= Screen.getHeight()/2;
		
		pm = new Pixmap(40, 40, Pixmap.Format.RGBA8888);
		
		for (Tile tile : level.getTiles()) {
			if(tile == null) continue;
			
			pm.drawPixel((int) tile.getPosition().x - (int) (vp.x / Tile.WIDTH), (int) tile.getPosition().y - (int) (vp.y / Tile.HEIGHT), 
					tile.getMinimapColor());
		}
		
		// TODO add entity icons
		
		map.draw(pm, 0, 0);
		
		pm.dispose();
		
		scene.getSpriteBatch().draw(map, sprite.getX() + 6, sprite.getY() + 5 + 80, 80, -80);
	}
	
	public void update(boolean resize) {
		if(resize) {
			sprite.setPosition(Screen.getWidth() - sprite.getWidth() - 2, Screen.getHeight() - sprite.getHeight());
		}
	}
	
}
