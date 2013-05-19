package net.catacombsnatch.game.core.entity.systems;

import net.catacombsnatch.game.core.entity.components.Position;
import net.catacombsnatch.game.core.entity.components.Render;
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
	@Mapper protected ComponentMapper<Position> posMapper;
	
	protected final View view;
	protected SpriteBatch graphics;
	
	@SuppressWarnings("unchecked")
	public RenderSystem(View view) {
		super(Aspect.getAspectForAll(Render.class, Position.class));
		
		this.view = view;
	}
	
	@Override
	public boolean checkProcessing() {
		return view != null && graphics != null;
	}

	@Override
	protected void process(Entity e) {
		Render render = renderMapper.get(e);
		Position p = posMapper.get(e);
		
		Rectangle rect = view.getViewport();
		if (p.getX() >= rect.x && p.getY() >= rect.y &&
			p.getX() < rect.width && p.getY() < rect.width) {
			
			render.getRenderer().render(graphics);
		}
	}
	
	@Override
	public void end() {
		graphics = null;
	}
	
	public void setGraphics(SpriteBatch graphics) {
		this.graphics = graphics;
	}

}
