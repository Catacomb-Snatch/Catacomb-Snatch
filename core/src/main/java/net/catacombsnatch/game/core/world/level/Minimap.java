package net.catacombsnatch.game.core.world.level;

import net.catacombsnatch.game.core.resources.Art;
import net.catacombsnatch.game.core.scene.Scene;
import net.catacombsnatch.game.core.screen.Renderable;
import net.catacombsnatch.game.core.screen.Screen;

import com.badlogic.gdx.graphics.g2d.Sprite;

public class Minimap implements Renderable {
	protected Level level;
	protected Sprite sprite;
	
	public Minimap(Level level) {
		this.level = level;
		
		sprite = new Sprite(Art.skin.getAtlas().findRegion("minimap-frame"));
	}

	@Override
	public void render(Scene scene) {
		sprite.draw(scene.getSpriteBatch());
	}
	
	public void update(boolean resize) {
		if(resize) {
			sprite.setPosition(Screen.getWidth() - sprite.getWidth() - 2, Screen.getHeight() - sprite.getHeight());
		}
	}
	
}
