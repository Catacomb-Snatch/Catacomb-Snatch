package net.catacombsnatch.game.core.entity.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import net.catacombsnatch.game.core.entity.components.Position;
import net.catacombsnatch.game.core.entity.components.Rotation;
import net.catacombsnatch.game.core.entity.components.Velocity;
import net.catacombsnatch.game.core.world.Direction;

public class MovementSystem extends EntityProcessingSystem {
	
	@Mapper protected ComponentMapper<Position> posMapper;
	@Mapper protected ComponentMapper<Velocity> velMapper;

	@Mapper protected ComponentMapper<Rotation> rotMapper;
	
	@SuppressWarnings("unchecked")
	public MovementSystem() {
		super(Aspect.getAspectForAll(Position.class, Velocity.class));
	}

	@Override
	protected void process(Entity e) {
		Position p = posMapper.get(e);
		Velocity v = velMapper.get(e);
		
		v.normalize();
		
		p.addX(v.getVelocityX());
		p.addY(v.getVelocityY());
				
		Rotation r = rotMapper.get(e);
		if(r != null) r.setDirection(Direction.getDirectionFor(v.getVelocityX(), v.getVelocityY()));
		
		v.reset(); 

		p.setDirection(Direction.getDirectionFor(v.getVelocity()));
	}
	
}
