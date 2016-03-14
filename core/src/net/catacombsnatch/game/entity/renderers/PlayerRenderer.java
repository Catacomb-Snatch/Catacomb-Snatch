package net.catacombsnatch.game.entity.renderers;

import net.catacombsnatch.game.entity.components.Position;
import net.catacombsnatch.game.entity.components.Rotation;
import net.catacombsnatch.game.resource.Art;
import net.catacombsnatch.game.world.Direction;

import com.artemis.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class PlayerRenderer extends Renderer {
    protected Animation[] animations;

    public PlayerRenderer(Entity entity) {
        super(entity);

        animations = new Animation[Direction.count];

        for (Direction direction : Direction.values()) {
            int face = direction.getFace();
            animations[face] = new Animation(1f, new Array<TextureRegion>(Art.lordLard[face]), Animation.PlayMode.LOOP);
        }
    }

    @Override
    public void render(SpriteBatch graphics) {
        if (graphics == null) return;

        Animation ani = animations[entity.getComponent(Rotation.class).getDirection().getFace()];

        if (ani != null) {
            Position pos = entity.getComponent(Position.class);
            graphics.draw(ani.getKeyFrame(Gdx.graphics.getDeltaTime()), pos.getPosition().x, pos.getPosition().y);
        }
    }

}
