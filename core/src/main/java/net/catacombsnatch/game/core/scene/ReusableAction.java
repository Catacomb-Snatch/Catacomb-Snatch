package net.catacombsnatch.game.core.scene;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * This class is a workaround for Actions being removed after act()ed.
 */
public abstract class ReusableAction extends Action {
	
	protected Actor a;
	
	@Override
	public boolean act(float delta) {
		if (a == null) {
			a = getActor();
		}
		a.addAction(this);
		return act0(delta);
	}
	
	public abstract boolean act0(float delta);

}
