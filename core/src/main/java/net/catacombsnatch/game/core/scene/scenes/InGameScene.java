package net.catacombsnatch.game.core.scene.scenes;

import java.util.ArrayList;
import java.util.List;

import net.catacombsnatch.game.core.scene.Scene;
import net.catacombsnatch.game.core.world.Difficulty;
import net.catacombsnatch.game.core.world.World;
import net.catacombsnatch.game.core.world.World.MapRotation;
import net.catacombsnatch.game.core.world.level.View;

public class InGameScene extends Scene {
	
	/** The world we are playing in */
	protected World world;
	
	protected List<View> views;
	
	public InGameScene() {
		super();

		world = new World(Difficulty.EASY, MapRotation.ONCE);
		
		views = new ArrayList<View>();
		views.add(new View(world.getCurrentLevel()));
	}
	
	@Override
	public void render() {
		// Tick, tock - the world is just a clock...
		world.tick();
		
		// Open the windows to actually see the outside!
		for(View view : views) {
			view.render(this);
		}
		
		// Just some overlays
		super.render();
	}
}
