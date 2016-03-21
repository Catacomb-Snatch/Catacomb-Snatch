package net.catacombsnatch.game.entity.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import net.catacombsnatch.game.entity.Entities;
import net.catacombsnatch.game.entity.components.Health;

public class HealSystem extends IteratingSystem {

    public HealSystem() {
        super(Family.all(Health.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        Health health = Entities.health.get(entity);

        if (health.canHeal) {
            if (health.healTick < health.healSpeed) {
                health.healTick++;
            } else {
                health.heal(health.healAmount);
            }
        }
    }

}
