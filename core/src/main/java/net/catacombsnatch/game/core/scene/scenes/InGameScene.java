package net.catacombsnatch.game.core.scene.scenes;

import java.util.ArrayList;
import java.util.List;

import net.catacombsnatch.game.core.entity.systems.RenderSystem;
import net.catacombsnatch.game.core.event.EventHandler;
import net.catacombsnatch.game.core.event.input.InputManager;
import net.catacombsnatch.game.core.event.input.Key;
import net.catacombsnatch.game.core.event.input.events.KeyReleaseEvent;
import net.catacombsnatch.game.core.player.LocalPlayer;
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
	protected boolean initialized = false;
	protected boolean paused = false;
	
	/** The world we are playing in */
	protected Campaign world;
	
	protected List<View> views;
	
	public InGameScene() {
		super();
	}
	
	public void init(Level level) {  // TODO
		world = new Campaign(Difficulty.EASY, MapRotation.ONCE);
		
		world.getPlayers().add(new LocalPlayer());
		world.getLevels().add(level);
		
		views = new ArrayList<View>();
		
		View view = new View(level);
		level.getSystemManager().setSystem(new RenderSystem(view));
		views.add(view);		
		
		initialized = true;
		update(true);
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
		if(!initialized) return;
		
		if (!paused) {
			// Check keyboard inputs
			int mx = 0, my = 0;
		
			if(InputManager.isPressed(Key.MOVE_LEFT)){
				mx = mx-10;
			}
			if(InputManager.isPressed(Key.MOVE_RIGHT)){
				mx = mx+10;
			}
			if(InputManager.isPressed(Key.MOVE_UP)){
				my = my+10;
			}
			if(InputManager.isPressed(Key.MOVE_DOWN)){
				my = my-10;
			}
			
			for(View view : views) {
				view.setTarget(view.getOffset().x + mx, view.getOffset().y + my);
			}

			// Tick, tock - the world is just a clock...
			world.tick(delta);
		}
		
		
		// Open the windows to see the world!
		getSpriteBatch().begin();
		
		for(View view : views) {
			view.render(this);
		}
		
		getSpriteBatch().end();
		
		// Just some overlays
		super.tick(delta);
	}
	
	@Override
	public void exit() {
		world = null;
		views = null;
		initialized = false;
		
		SceneManager.setDrawAllEnabled(false);
		super.exit();
	}
	
	@Override
	public void update(boolean resize) {
		if(!initialized || !resize) return;
		
		for(View view : views) {
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
