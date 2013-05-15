package net.catacombsnatch.game.core.scene.scenes;

import java.util.ArrayList;
import java.util.List;

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

public class InGameScene extends Scene {
	protected boolean paused = false;
	
	/** The world we are playing in */
	protected Campaign campaign;
	
	protected List<View> views;
	
	public InGameScene(Level level) {  // TODO
		super();
		
		campaign = new Campaign(Difficulty.EASY, MapRotation.ONCE);
		
		campaign.getPlayers().add(new LocalPlayer());
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
				View view = new View(campaign.getCurrentLevel());
				view.setViewport(new Rectangle(0, 0, Screen.getWidth(), Screen.getHeight()));
				view.update(true);
				
				Transform t = player.getLevelPlayer().getEntity().getComponent(Transform.class);
				view.setTarget(t.getPosition());
				
				views.add(view);
			}
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
		if(!resize) return;
		
		if(views != null) for(View view : views) {
			view.setViewport(new Rectangle(0, 0, Screen.getWidth(), Screen.getHeight()));
			view.update(true);
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
