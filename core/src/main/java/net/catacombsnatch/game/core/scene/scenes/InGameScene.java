package net.catacombsnatch.game.core.scene.scenes;

import java.util.ArrayList;
import java.util.List;

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
	protected Minimap minimap;
	
	public InGameScene() {
		super();
	}
	
	public void init(Level level) {
		world = new World(Difficulty.EASY, MapRotation.ONCE);
		world.getLevels().add(level);
		
		views = new ArrayList<View>();
		views.add(new View(level));
		
		minimap = new Minimap(level);
		
		initialized = true;
		update(true);
	}
	
	@Override
	public void render(float delta) {
		if(!initialized) return;
		
		// Just some overlays
		super.draw();
		getSpriteBatch().begin();
		
		// Tick, tock - the world is just a clock...
		world.tick(delta);
		
		// Open the windows to actually see the outside!
		for(View view : views) {
			view.render(this);
		}
		
		// Draw the minimap
		minimap.render(this);
	}
	
	@Override
	public void update(boolean resize) {
		if(!initialized) return;
		
		if(resize) {
			minimap.update(true);
			
			for(View view : views) {
				view.setViewport(new Rectangle(0, 0, Screen.getWidth(), Screen.getHeight()));
				view.resize();
			}
		}
	}
}
