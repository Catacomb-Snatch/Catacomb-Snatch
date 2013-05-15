package net.catacombsnatch.game.core.entity.systems;

import net.catacombsnatch.game.core.entity.components.Transform;
import net.catacombsnatch.game.core.entity.components.Velocity;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;

public class MovementSystem extends EntityProcessingSystem {
	@Mapper protected ComponentMapper<Transform> transformMapper;
	@Mapper protected ComponentMapper<Velocity> velocityMapper;
	
	@SuppressWarnings("unchecked")
	public MovementSystem() {
		super(Aspect.getAspectForAll(Transform.class, Velocity.class));
	}

	@Override
	protected void process(Entity e) {
		Transform t = transformMapper.get(e);
		Velocity v = velocityMapper.get(e);
		
		v.normalize();
		
		t.addX(v.getVelocityX());
		t.addY(v.getVelocityY());
		
		v.reset();
	}

}
