package net.catacombsnatch.game.core.entity.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.annotations.Mapper;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import net.catacombsnatch.game.core.entity.components.Animations;
import net.catacombsnatch.game.core.entity.components.Position;
import net.catacombsnatch.game.core.entity.components.Render;
import net.catacombsnatch.game.core.entity.components.Sprite;
import net.catacombsnatch.game.core.world.level.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class RenderSystem extends EntitySystem {
	@Mapper protected ComponentMapper<Render> renderMapper;
	@Mapper protected ComponentMapper<Sprite> sprMapper;
	@Mapper protected ComponentMapper<Animations> aniMapper;
	@Mapper protected ComponentMapper<Position> posMapper;
	
	private List<Entity> sortedEntities;
	protected View view;
	protected SpriteBatch graphics;
	
	@SuppressWarnings("unchecked")
	public RenderSystem() {
		super(Aspect.getAspectForAll(Position.class).one(Sprite.class, Animations.class));
	}
	
	@Override
	protected void initialize() {
		sortedEntities = new ArrayList<Entity>();
	}
	
	@Override
	public boolean checkProcessing() {
		return view != null && graphics != null;
	}

	@Override
	protected void processEntities(ImmutableBag <Entity> entities) {
		for(Entity e : sortedEntities) {
			process(e);
		}
	}
	
	protected void process(Entity e) {
		Render render = renderMapper.get(e);
		Position p = posMapper.get(e);

		int face = p.getDirection().getFace();
		
		aniMapper.get(e).updateStateTime(Gdx.graphics.getDeltaTime());

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

	@Override
	protected void inserted(Entity e) {
		sortedEntities.add(e);

		Collections.sort(sortedEntities, new Comparator<Entity>() {
			@Override
			public int compare(Entity e1, Entity e2) {
				Animations s1 = aniMapper.get(e1);
				Animations s2 = aniMapper.get(e2);
				return s1.layer.compareTo(s2.layer);
			}
		});
	}

	@Override
	protected void removed(Entity e) {
		sortedEntities.remove(e);
	}

	public void setGraphics(SpriteBatch graphics) {
		this.graphics = graphics;
	}

}
