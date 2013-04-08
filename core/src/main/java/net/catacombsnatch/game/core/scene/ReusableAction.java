package net.catacombsnatch.game.core.scene;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;

/** This class is a wrapper for Actions being removed after {@link #act(float)}ed. */
public abstract class ReusableAction extends Action {
	
	protected Actor a;
	
	@Override
	public boolean act(float delta) {
		if (a == null) a = getActor();
		
		a.addAction(this);
		return use(delta);
	}
	
	/** @see #act */
	public abstract boolean use(float delta);

}
