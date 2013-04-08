package net.catacombsnatch.game.core.scene.scenes;

import net.catacombsnatch.game.core.Game;
import net.catacombsnatch.game.core.entity.Entity;
import net.catacombsnatch.game.core.entity.EntityManager;
import net.catacombsnatch.game.core.entity.components.Animated;
import net.catacombsnatch.game.core.event.EventHandler;
import net.catacombsnatch.game.core.event.EventManager;
import net.catacombsnatch.game.core.event.input.events.KeyPressedEvent;
import net.catacombsnatch.game.core.resource.options.OptionGroup;
import net.catacombsnatch.game.core.resources.Art;
import net.catacombsnatch.game.core.resources.Language;
import net.catacombsnatch.game.core.scene.ReusableAction;
import net.catacombsnatch.game.core.scene.Scene;
import net.catacombsnatch.game.core.scene.SceneManager;
import net.catacombsnatch.game.core.screen.Screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class OptionsScene extends Scene {
	private int index = 0;
	private final Entity charEntity;
	private final Animated charAnimation; // Reference for quick access
	

	public OptionsScene() {
		super();
		
		this.setBackground(Art.pyramid);
		
		charEntity = new EntityManager().createEntity();
		charAnimation = charEntity.addComponent( Animated.class, new Animated( Art.lordLard[0], 0.15f ) );
		
	}
	
	public void initGroup(final OptionGroup group) {
		addTextButton(Language.get("scene.options.back"), 0, 0).addAction(new ReusableAction() {
			@Override
			public boolean use(float delta) {
				SceneManager.exit();
				return true;
			}
		});
		
		for (String k : group.getKeys()) {
			final String key = group.getCurrentPath() + k;
			final Object o = group.get(key);
			
			if (o instanceof OptionGroup) {
				addTextButton(Language.get("option." + key), 0, 0).addAction(new ReusableAction() {
					@Override
					public boolean use(float delta) {
						OptionsScene scene = SceneManager.switchTo(OptionsScene.class);
						scene.initGroup((OptionGroup) o);
						return true;
					}
				});
				
			} else if (o instanceof Boolean) {
				final boolean b = (Boolean) o;
				final TextButton button = addTextButton(Language.get("option." + key + (b ? ".on" : ".off")), 0, 0);
				
				button.addAction(new ReusableAction() {
					protected boolean down = b;
					
					@Override
					public boolean use(float delta) {
						down = !down;
						group.set(key, down);
						button.setText(Language.get("option." + key + (down ? ".on" : ".off")));
						
						return true;
					}
				});
			}
			
			// TODO add missing types
		}
		
		for(Actor actor : getActors()) {
			actor.setWidth(150);
		}
		
		index = getActors().size-1;
		
		update(true);
	}

	@Override
	public void enter() {
		EventManager.registerListener(this);
		Game.sound.startTitleMusic();
	}
	
	@Override
	public void leave() {
		EventManager.unregisterListener(this);
		Game.sound.stopTitleMusic();
	}

	@Override
	public void render() {
		super.render();
		
		getSpriteBatch().draw(Art.logo, (Screen.getWidth() - Art.logo.getWidth()) / 2, Screen.getHeight() - (int) (1.5f * Art.logo.getHeight()));
		
		charAnimation.tick();
		charAnimation.render( this );
	}
	
	@EventHandler
	public void keyPressed(KeyPressedEvent event) {
		switch(event.getKey()) {
			case MOVE_DOWN:
				index--;
				if(index < 0) index = getActors().size - 1;
				break;
				
			case MOVE_UP:
				index++;
				if(index >= getActors().size) index = 0;
				break;
			
			case USE:
				if (!canUse) break;
				canUse = false;
				Actor actor = getActors().get(index);
				if(actor != null) actor.act(Gdx.graphics.getDeltaTime());
				break;
				
			default:
				// Nothing to do here
		}
		
		update(false);
	}
	
	@EventHandler
	public void keyReleased(KeyPressedEvent event) {
		switch(event.getKey()) {
			case USE:
				canUse = true;
				break;
				
			default:
				// Nothing to do here
		}
		
		update(false);
	}
	
	@Override
	public void update(boolean resize) {
		if(resize) {
			int x = (Screen.getWidth() - 150) / 2, p = 40;
			for(int i = 0; i < getActors().size; i++) {
				getActors().get(i).setPosition(x, p + (p * i));
			}
		}
		
		Actor actor = getActors().get(index);
		charAnimation.setPosition((int) (actor.getX() - (Art.lordLard[0][0].getRegionWidth() / 2)), (int) actor.getY());
	}
}
