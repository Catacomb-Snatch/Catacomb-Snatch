package net.catacombsnatch.game.scene;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * This class is a wrapper to avoid Actions being removed after using {@link #act(float)}.
 */
public abstract class ReusableAction extends Action {

    protected Actor a;

    @Override
    public boolean act(float delta) {
        if (a == null) a = getActor();

        if (!use(delta)) {
            a.addAction(this);
        }
        return false;
    }

    /**
     * @see #act
     */
    public abstract boolean use(float delta);

}
