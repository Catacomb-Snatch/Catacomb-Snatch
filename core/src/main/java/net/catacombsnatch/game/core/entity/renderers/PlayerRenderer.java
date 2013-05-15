package net.catacombsnatch.game.core.entity.renderers;

import net.catacombsnatch.game.core.entity.components.Transform;
import net.catacombsnatch.game.core.resource.Art;
import net.catacombsnatch.game.core.world.Direction;
import net.catacombsnatch.game.core.world.level.Level;

import com.artemis.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class PlayerRenderer extends Renderer {
	protected Animation[] animations;
	
	public PlayerRenderer(Level level, Entity entity) {
		super(level, entity);

		animations = new Animation[Direction.count];
		
		for(Direction direction : Direction.values()) {
			int face = direction.getFace();
			animations[face] = new Animation(1f, new Array<TextureRegion>(Art.lordLard[face]), Animation.LOOP);
		}
	}

	@Override
	public void render(SpriteBatch graphics) {
		if(graphics == null) return;
		
		Transform t = entity.getComponent(Transform.class);
		Animation ani = animations[t.getDirection().getFace()];
		
		if(ani != null) graphics.draw(ani.getKeyFrame(Gdx.graphics.getDeltaTime()), t.getX(), t.getY());
	}

}
