package net.catacombsnatch.game.core.scene.scenes;

import java.util.ArrayList;
import java.util.List;

import net.catacombsnatch.game.core.scene.Scene;
import net.catacombsnatch.game.core.screen.Screen;
import net.catacombsnatch.game.core.world.level.View;

public class InGameScene extends Scene {
	protected List<View> views;
	
	public InGameScene() {
		super();
		
		views = new ArrayList<View>();
	}
	
	@Override
	public void render(Screen screen) {
		super.render(screen);
		
		for(View view : views) {
			view.render(screen);
		}
	}
}
