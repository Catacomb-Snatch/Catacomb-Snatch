package net.catacombsnatch.game.core.entity.systems;

import net.catacombsnatch.game.core.entity.components.Position;
import net.catacombsnatch.game.core.entity.components.Rotation;
import net.catacombsnatch.game.core.entity.components.Animations;
import net.catacombsnatch.game.core.world.level.View;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class RenderSystem extends EntityProcessingSystem {
	
	@Mapper protected ComponentMapper<Animations> aniMapper;
	@Mapper protected ComponentMapper<Position> posMapper;
	@Mapper protected ComponentMapper<Rotation> rotMapper;
	
	protected View view;
	protected SpriteBatch graphics;
	
	@SuppressWarnings("unchecked")
	public RenderSystem() {
		super(Aspect.getAspectForAll(Animations.class, Position.class, Rotation.class));
	}
	
	@Override
	public boolean checkProcessing() {
		return view != null && graphics != null;
	}

	@Override
	protected void process(Entity e) {
		Animations a = aniMapper.get(e);
		Position p = posMapper.get(e);
		int face = rotMapper.get(e).getDirection().getFace();
		Rectangle rect = view.getViewport();
		
		a.updateStateTime(Gdx.graphics.getDeltaTime());
		
		if (p.getX() >= rect.x && p.getY() >= rect.y &&
			p.getX() < rect.width && p.getY() < rect.width) {
			
			graphics.draw(a.getKeyFrame(face), p.getX(), p.getY());
		}
	}
	
	@Override
	public void end() {
		view = null;
		graphics = null;
	}
	
	public void setGraphics(SpriteBatch graphics) {
		this.graphics = graphics;
	}

	public void setView(View view) {
		this.view = view;
	}
}