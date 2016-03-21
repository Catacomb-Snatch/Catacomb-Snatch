package net.catacombsnatch.game.entity.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import net.catacombsnatch.game.world.level.Level;

import java.util.LinkedList;
import java.util.List;

/**
 * Represents an updatable entity.
 * Other systems can call the update method to change appearance or trigger some other effects.
 *
 * This component is not fit for being reused by a component pool!
 *
 * @author spaceemotion
 * @version 1.0
 */
public class Updatable implements Component {
    private final List<Callback> callbacks = new LinkedList<>();


    public Updatable add(Callback callback) {
        callbacks.add(callback);
        return this;
    }

    public void update(Level level, Entity entity) {
        for (Callback callback : callbacks) {
            callback.onUpdate(level, entity);
        }
    }

    public interface Callback {
        void onUpdate(Level level, Entity entity);
    }
}
