package net.catacombsnatch.game.core.entity.systems;

import net.catacombsnatch.game.core.entity.components.Render;
import net.catacombsnatch.game.core.entity.components.Transform;
import net.catacombsnatch.game.core.world.level.View;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class RenderSystem extends EntityProcessingSystem {
	@Mapper protected ComponentMapper<Render> renderMapper;
	@Mapper protected ComponentMapper<Transform> positionMapper;
	
	protected final View view;
	protected SpriteBatch graphics;
	
	@SuppressWarnings("unchecked")
	public RenderSystem(View view) {
		super(Aspect.getAspectForAll(Render.class, Transform.class));
		
		this.view = view;
	}

	@Override
	protected void process(Entity e) {
		Transform pos = positionMapper.get(e);
		Rectangle rect = view.getViewportOffset();
		
		if (pos.getX() >= rect.x && pos.getY() >= rect.y &&
			pos.getX() < rect.width && pos.getY() < rect.width) {
			
			renderMapper.get(e).getRenderer().render(graphics);
		}
	}
	
	public void setGraphics(SpriteBatch graphics) {
		this.graphics = graphics;
	}

}
