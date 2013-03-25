package net.catacombsnatch.game.core.scene.scenes;

import net.catacombsnatch.game.core.Game;
import net.catacombsnatch.game.core.entity.Entity;
import net.catacombsnatch.game.core.entity.EntityManager;
import net.catacombsnatch.game.core.entity.components.Animated;
import net.catacombsnatch.game.core.event.EventHandler;
import net.catacombsnatch.game.core.event.EventManager;
import net.catacombsnatch.game.core.event.input.KeyPressedEvent;
import net.catacombsnatch.game.core.resources.Language;
import net.catacombsnatch.game.core.scene.Scene;
import net.catacombsnatch.game.core.screen.Art;
import net.catacombsnatch.game.core.screen.Screen;

import com.badlogic.gdx.scenes.scene2d.Actor;

public class TitleScreen extends Scene {
	private int index = 0;
	private final Entity charEntity;
	private final Animated charAnimation; // Reference for quick access

	public TitleScreen() {
		super();
		
		this.setBackground(Art.titleScreen);
		
		int w = 150, x = (Screen.getWidth() - w) / 2, i = 40;
		addTextButton(Language.get("scene.title.demo"), x, i * 4).setWidth(w);
		addTextButton(Language.get("scene.title.start"), x, i * 3).setWidth(w);
		addTextButton(Language.get("scene.title.options"), x, i * 2).setWidth(w);
		addTextButton(Language.get("scene.title.exit"), x, i).setWidth(w);
		
		charEntity = new EntityManager().createEntity();
		charAnimation = charEntity.addComponent( Animated.class, new Animated( Art.lordLard[0], 0.15f ) );

		EventManager.registerListener(this);
		update();
		
		Game.sound.startTitleMusic();
	}

	@Override
	public void exit() {
		Game.sound.stopTitleMusic();
	}

	@Override
	public void render( Screen screen ) {
		super.render(screen);
		
		charAnimation.render( screen );
	}
	
	@EventHandler
	public void keyPressed(KeyPressedEvent event) {
		index++;
		if(index >= actors.size) index = 0;
		
		update();
	}
	
	protected void update() {
		Actor actor = actors.get(index);
		charAnimation.setPosition((int) (actor.getX() - 32), (int) actor.getY());
	}
	
}
