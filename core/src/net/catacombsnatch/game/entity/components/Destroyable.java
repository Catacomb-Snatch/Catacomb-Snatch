package net.catacombsnatch.game.entity.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import net.catacombsnatch.game.world.level.Level;

import java.util.LinkedList;
import java.util.List;

/**
 * Represents a destroyable entity that has a callback in case it gets destroyed.
 * This component is not fit for being reused by a component pool!
 *
 * @author spaceemotion
 * @version 1.0
 */
public class Destroyable implements Component {
    private final List<Callback> callbacks = new LinkedList<>();


    public Destroyable add(Callback callback) {
        callbacks.add(callback);
        return this;
    }

    public void call(Level level, Entity entity) {
        for (Callback callback : callbacks) {
            callback.onEntityDestroyed(level, entity);
        }
    }

    public interface Callback {
        void onEntityDestroyed(Level level, Entity entity);
    }

}
