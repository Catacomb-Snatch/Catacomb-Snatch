package net.catacombsnatch.game.core.scene.scenes;

import java.util.ArrayList;
import java.util.List;

import net.catacombsnatch.game.core.event.input.InputManager;
import net.catacombsnatch.game.core.event.input.Key;
import net.catacombsnatch.game.core.scene.Scene;
import net.catacombsnatch.game.core.screen.Screen;
import net.catacombsnatch.game.core.world.Difficulty;
import net.catacombsnatch.game.core.world.World;
import net.catacombsnatch.game.core.world.World.MapRotation;
import net.catacombsnatch.game.core.world.level.Level;
import net.catacombsnatch.game.core.world.level.Minimap;
import net.catacombsnatch.game.core.world.level.View;

import com.badlogic.gdx.math.Rectangle;

public class InGameScene extends Scene {
	protected boolean initialized = false;
	
	/** The world we are playing in */
	protected World world;
	
	protected List<View> views;
	
	public InGameScene() {
		super();
	}
	
	public void init(Level level) {
		world = new World(Difficulty.EASY, MapRotation.ONCE);
		world.getLevels().add(level);
		
		views = new ArrayList<View>();
		views.add(new View(level));
		
		initialized = true;
		update(true);
	}
	
	@Override
	public void render(float delta) {
		if(!initialized) return;
		
		// Check keyboard inputs
		int mx = 0, my = 0;
		
		if(InputManager.isPressed(Key.MOVE_LEFT)) mx--;
		if(InputManager.isPressed(Key.MOVE_RIGHT)) mx++;
		if(InputManager.isPressed(Key.MOVE_UP)) my++;
		if(InputManager.isPressed(Key.MOVE_DOWN)) my--;

		for(View view : views) {
			view.move(mx, my);
		}
		
		// Just some overlays
		super.draw();
		getSpriteBatch().begin();
		
		// Tick, tock - the world is just a clock...
		world.tick(delta);
		
		// Open the windows to actually see the outside!
		for(View view : views) {
			view.render(this);
		}
	}
	
	@Override
	public void update(boolean resize) {
		if(!initialized) return;
		
		if(resize) {
			for(View view : views) {
				view.setViewport(new Rectangle(0, 0, Screen.getWidth(), Screen.getHeight()));
				view.resize();
			}
		}
	}
}
