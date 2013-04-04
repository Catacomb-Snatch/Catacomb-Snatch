package net.catacombsnatch.game.core.scene.scenes;

import java.util.ArrayList;
import java.util.List;

import net.catacombsnatch.game.core.scene.Scene;
import net.catacombsnatch.game.core.world.level.View;

public class InGameScene extends Scene {
	protected List<View> views;
	
	public InGameScene() {
		super();
		
		views = new ArrayList<View>();
	}
	
	@Override
	public void render() {
		super.render();
		
		for(View view : views) {
			view.render(this);
		}
	}
}
