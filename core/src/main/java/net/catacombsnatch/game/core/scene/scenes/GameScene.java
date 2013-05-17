package net.catacombsnatch.game.core.scene.scenes;

import java.util.ArrayList;
import java.util.List;

import net.catacombsnatch.game.core.Game;
import net.catacombsnatch.game.core.entity.components.Transform;
import net.catacombsnatch.game.core.entity.components.Velocity;
import net.catacombsnatch.game.core.event.EventHandler;
import net.catacombsnatch.game.core.event.input.InputManager;
import net.catacombsnatch.game.core.event.input.Key;
import net.catacombsnatch.game.core.event.input.events.KeyReleaseEvent;
import net.catacombsnatch.game.core.player.LocalPlayer;
import net.catacombsnatch.game.core.player.Player;
import net.catacombsnatch.game.core.scene.Scene;
import net.catacombsnatch.game.core.scene.SceneManager;
import net.catacombsnatch.game.core.screen.Screen;
import net.catacombsnatch.game.core.world.Campaign;
import net.catacombsnatch.game.core.world.Campaign.MapRotation;
import net.catacombsnatch.game.core.world.Difficulty;
import net.catacombsnatch.game.core.world.level.Level;
import net.catacombsnatch.game.core.world.level.View;

import com.badlogic.gdx.math.Rectangle;

public class GameScene extends Scene {
	protected boolean paused = false;
	
	/** The campaign we are playing in */
	protected Campaign campaign;
	
	protected List<View> views;
	
	public GameScene(Level level) {  // TODO complete
		super();
		
		campaign = new Campaign(Difficulty.EASY, new MapRotation.FIRST());
		
		for(Player player : Game.getLocalPlayers()) {
			if(player == null) continue;
			
			campaign.getPlayers().add(player);
		}
		
		campaign.getLevels().add(level);
	}
	
	@Override
	public void enter(Scene before) {
		super.enter(before);
		
		paused = false;
		SceneManager.setDrawAllEnabled(true);
	}
	
	@Override
	public void leave(Scene next) {
		super.leave(next);
		
		paused = true;
	}
	
	@Override
	public void tick(float delta) {
		if (!paused) {
			if(campaign.hasFinished()) {
				SceneManager.exit();
			}
			
			// Tick, tock - the campaign is just a clock...
			campaign.tick(delta);
			
			// Player movement
			int mx = 0, my = 0;
			
			if(InputManager.isPressed(Key.MOVE_LEFT)) mx--;
			if(InputManager.isPressed(Key.MOVE_RIGHT)) mx++;
			if(InputManager.isPressed(Key.MOVE_UP)) my++;
			if(InputManager.isPressed(Key.MOVE_DOWN)) my--;
			
			for(Player player : campaign.getPlayers()) {
				player.getLevelPlayer().getEntity().getComponent(Velocity.class).force(mx, my);
			}
		}
		
		// Open the windows to see what's happening!
		getSpriteBatch().begin();
		
		if(views == null) {
			views = new ArrayList<View>();
			
			for(Player player : campaign.getPlayers()) {
				if(!(player instanceof LocalPlayer)) continue;
				
				View view = new View(campaign.getCurrentLevel());
				view.setTarget(player.getLevelPlayer().getEntity().getComponent(Transform.class).getPosition());

				views.add(view);
			}
			
			update(true); // Update viewports
		}
		
		for(View view : views) {
			view.render(this);
		}
		
		getSpriteBatch().end();
		
		// Just some overlays
		super.tick(delta);
	}
	
	@Override
	public void exit() {
		campaign = null;
		views = null;
		
		SceneManager.setDrawAllEnabled(false);
		super.exit();
	}
	
	@Override
	public void update(boolean resize) {
		if(!resize || views == null) return;
		
		float w = Screen.getWidth(), h = Screen.getHeight();
		
		/*
		// This did not work: (Trying to split the screen here...)
		
		for(float p = 0, s = views.size(); p < s; p++) {
			boolean quarters = s > (s / 2);
			float n = p / s * w, m = w / s;
			
			View view = views.get((int) p);
			
			view.setViewport(new Rectangle(n, quarters ? h / 2 : 0, m, quarters ? h / 2 : h ));
			view.update(resize);
		}*/
		
		for(View view : views) {
			view.setViewport(new Rectangle(0, 0, w, h));
			view.update(resize);
		}
	}
	
	@EventHandler
	public void keyRelease(KeyReleaseEvent event) {
		switch(event.getKey()) {
			case BACK:
				SceneManager.switchTo(PauseScreen.class);
				break;
			
			default: // Do nothing ...
		}
	}
}
