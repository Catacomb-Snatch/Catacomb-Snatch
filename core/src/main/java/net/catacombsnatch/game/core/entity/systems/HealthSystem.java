package net.catacombsnatch.game.core.entity.systems;

import net.catacombsnatch.game.core.entity.components.Health;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntityProcessingSystem;

public class HealthSystem extends EntityProcessingSystem {
	protected ComponentMapper<Health> heathMapper;
	
	@SuppressWarnings("unchecked")
	public HealthSystem() {
		super(Health.class);
	}

	@Override
	protected void initialize() {		
		heathMapper = new ComponentMapper<Health>(Health.class, world.getEntityManager());
	}
	
	@Override
	protected void process(Entity e) {
		// Get health component
		Health health = heathMapper.get(e);
		
		// Update the entitie's health
		if(health.canRegenerate()) {
			if(health.tick < health.getRegenerationSpeed()) {
				health.tick++;
				return;
			}
			
			health.heal(health.getHealAmount());
		}
	}

}
