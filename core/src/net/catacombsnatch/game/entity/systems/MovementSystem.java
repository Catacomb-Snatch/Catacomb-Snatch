package net.catacombsnatch.game.entity.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import net.catacombsnatch.game.entity.Entities;
import net.catacombsnatch.game.entity.components.Position;
import net.catacombsnatch.game.entity.components.Velocity;

public class MovementSystem extends IteratingSystem {

    public MovementSystem() {
        super(Family.all(Position.class, Velocity.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        final Position position = Entities.position.get(entity);
        final Velocity velocity = Entities.velocity.get(entity);

        position.addX(velocity.x() * deltaTime);
        position.addY(velocity.y() * deltaTime);
    }

//    @Wire
//    protected ComponentMapper<Position> posMapper;
//    @Wire
//    protected ComponentMapper<Velocity> velMapper;
//
//    @Wire
//    protected ComponentMapper<Rotation> rotMapper;
//
//    @SuppressWarnings("unchecked")
//    public MovementSystem() {
//        super(Aspect.all(Position.class, Velocity.class));
//    }
//
//    @Override
//    protected void process(Entity e) {
//        Position p = posMapper.get(e);
//        Velocity v = velMapper.get(e);
//
//        v.normalize();
//
//        p.addX(v.getVelocityX());
//        p.addY(v.getVelocityY());
//
//        Rotation r = rotMapper.get(e);
//        if (r != null) r.setDirection(Direction.getDirectionFor(v.getVelocityX(), v.getVelocityY()));
//
//        v.reset();
//
//        p.setDirection(Direction.getDirectionFor(v.getVelocity()));
//    }

}
