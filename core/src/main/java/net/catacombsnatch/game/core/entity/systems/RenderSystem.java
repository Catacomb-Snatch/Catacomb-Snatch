package net.catacombsnatch.game.core.entity.systems;

import net.catacombsnatch.game.core.entity.components.Render;
import net.catacombsnatch.game.core.entity.components.Transform;
import net.catacombsnatch.game.core.world.level.View;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntityProcessingSystem;
import com.badlogic.gdx.math.Rectangle;

public class RenderSystem extends EntityProcessingSystem {
	protected ComponentMapper<Render> renderMapper;
	protected ComponentMapper<Transform> positionMapper;
	
	protected View view;
	
	@SuppressWarnings("unchecked")
	public RenderSystem(View view) {
		super(Render.class, Transform.class);
		
		this.view = view;
	}
	
	@Override
	public void initialize() {
		renderMapper = new ComponentMapper<Render>(Render.class, world.getEntityManager());
		positionMapper = new ComponentMapper<Transform>(Transform.class, world.getEntityManager());
	}

	@Override
	protected void process(Entity e) {
		Transform pos = positionMapper.get(e);
		Rectangle rect = view.getViewportOffset();
		
		if (pos.getX() >= rect.x && pos.getY() >= rect.y &&
			pos.getX() < rect.width && pos.getY() < rect.width) {
			
			renderMapper.get(e).getRenderer().render(null); // TODO ?
		}
	}

}
