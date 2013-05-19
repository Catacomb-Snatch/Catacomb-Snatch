package net.catacombsnatch.game.core.entity.systems;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.catacombsnatch.game.core.entity.components.Position;
import net.catacombsnatch.game.core.entity.components.Animations;
import net.catacombsnatch.game.core.entity.components.Sprite;
import net.catacombsnatch.game.core.world.level.View;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.annotations.Mapper;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class RenderSystem extends EntitySystem {
	
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
	protected void processEntities(ImmutableBag<Entity> entities) {
		for(Entity e : sortedEntities) {
			process(e);
		}
	}
	
	protected void process(Entity e) {
		Animations a = aniMapper.get(e);
		Position p = posMapper.get(e);
		int face = p.getDirection().getFace();
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

	public void setView(View view) {
		this.view = view;
	}
}