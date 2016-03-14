package net.catacombsnatch.game.entity.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import net.catacombsnatch.game.entity.components.Animations;
import net.catacombsnatch.game.entity.components.Position;
import net.catacombsnatch.game.entity.components.Render;
import net.catacombsnatch.game.entity.components.Sprite;
import net.catacombsnatch.game.world.level.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class RenderSystem extends EntityProcessingSystem {
    @Wire
    protected ComponentMapper<Render> renderMapper;
    @Wire
    protected ComponentMapper<Sprite> sprMapper;
    @Wire
    protected ComponentMapper<Animations> aniMapper;
    @Wire
    protected ComponentMapper<Position> posMapper;

    private List<Entity> sortedEntities;
    protected View view;
    protected SpriteBatch graphics;

    @SuppressWarnings("unchecked")
    public RenderSystem() {
        super(Aspect.all(Position.class).one(Sprite.class, Animations.class));
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
    public void inserted(Entity e) {
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
    public void removed(Entity e) {
        sortedEntities.remove(e);
    }

    public void setGraphics(SpriteBatch graphics) {
        this.graphics = graphics;
    }

}
