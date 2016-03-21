package net.catacombsnatch.game.entity.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.SortedIteratingSystem;
import net.catacombsnatch.game.entity.Entities;
import net.catacombsnatch.game.entity.components.Position;
import net.catacombsnatch.game.entity.components.Sprite;
import net.catacombsnatch.game.scene.Scene;
import net.catacombsnatch.game.scene.SceneManager;

import java.util.Comparator;

public class RenderSystem extends SortedIteratingSystem {
    private Scene scene;


    public RenderSystem() {
        super(Family.all(Position.class, Sprite.class).get(), new Comparator<Entity>() {
            @Override
            public int compare(Entity a, Entity b) {
                return Float.compare(Entities.position.get(b).y(), Entities.position.get(a).y());
            }
        });
    }

    @Override
    public void update(float deltaTime) {
        scene = SceneManager.getCurrent();

        if (scene != null) {
            scene.getBatch().begin();
            super.update(deltaTime);
            scene.getBatch().end();
        }
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        final Position position = Entities.position.get(entity);
        scene.getBatch().draw(Entities.sprite.get(entity).texture, position.x(), position.y());
    }


    /*@Wire
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
    }     */

}
