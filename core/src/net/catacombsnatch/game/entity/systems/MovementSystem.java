package net.catacombsnatch.game.entity.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import net.catacombsnatch.game.entity.components.Position;
import net.catacombsnatch.game.entity.components.Rotation;
import net.catacombsnatch.game.entity.components.Velocity;
import net.catacombsnatch.game.world.Direction;

public class MovementSystem extends EntityProcessingSystem {
	
	@Wire protected ComponentMapper<Position> posMapper;
	@Wire protected ComponentMapper<Velocity> velMapper;

	@Wire protected ComponentMapper<Rotation> rotMapper;
	
	@SuppressWarnings("unchecked")
	public MovementSystem() {
		super(Aspect.all(Position.class, Velocity.class));
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
