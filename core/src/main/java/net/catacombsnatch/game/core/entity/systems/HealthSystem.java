package net.catacombsnatch.game.core.entity.systems;

import net.catacombsnatch.game.core.entity.components.Health;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;

public class HealthSystem extends EntityProcessingSystem {
	@Mapper protected ComponentMapper<Health> heathMapper;
	
	@SuppressWarnings("unchecked")
	public HealthSystem() {
		super(Aspect.getAspectForAll(Health.class));
	}

	@Override
	protected void process(Entity e) {
		// Get health component
		Health health = heathMapper.get(e);
		
		// Update the entity's health
		if(health.canRegenerate()) {
			if(health.tick < health.getRegenerationSpeed()) {
				health.tick++;
				return;
			}
			
			health.heal(health.getHealAmount());
		}
	}

}
