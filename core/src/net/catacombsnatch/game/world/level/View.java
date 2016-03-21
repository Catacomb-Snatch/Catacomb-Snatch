package net.catacombsnatch.game.world.level;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import net.catacombsnatch.game.Monitoring;
import net.catacombsnatch.game.entity.systems.RenderSystem;
import net.catacombsnatch.game.resource.Art;
import net.catacombsnatch.game.scene.Scene;
import net.catacombsnatch.game.screen.Renderable;
import net.catacombsnatch.game.screen.Updateable;

import static net.catacombsnatch.game.world.tiles.Tiles.SIZE;

public class View implements Renderable, Updateable {
    public static final String DEBUG_TILES_RENDERED = "view_tiles"; // TODO make this work with multiple views

    private final Level level;
    private Rectangle viewport;

    protected Vector2 offset;
    protected Vector2 target;
    protected RenderSystem renderer;
    protected Sprite panel;
    protected MiniMap miniMap;


    public View(Level level) {
        this.level = level;

        offset = new Vector2();
        panel = new Sprite(Art.skin.getAtlas().findRegion("player-panel"));

        miniMap = new MiniMap(this.level, this);
    }

    @Override
    public void render(Scene scene) {
        if (viewport == null) return;

        if (target != null) {
            // "Camera" movement
            offset.lerp(target, 0.5f);

            // Draw tiles
            int rendered = 0;

            for(float y = (viewport.y - viewport.height - offset.y) / SIZE - 2; y <= -(viewport.y + offset.y) / SIZE + 4; y++) {
                for(float x = (viewport.x + offset.x) / SIZE - 2; x < (viewport.x + viewport.width + offset.x) / SIZE + 2; x++) {
                    Entity tile = level.getTile((int) x, (int) y);

//                    if (tile != null && tile.shouldRender(this)) {
//                        tile.render((SpriteBatch) scene.getBatch(), this);
                        rendered++;
//                    }
                }
            }

            Monitoring.update(DEBUG_TILES_RENDERED, rendered);

            // Render entities
//            renderer.setGraphics((SpriteBatch) scene.getBatch());
//            renderer.process();
        }

        // Draw overlays
        panel.draw(scene.getBatch());

//        miniMap.render(scene);
    }

    @Override
    public void update(boolean resize) {
        if (viewport == null || !resize) return;

        viewport.x -= viewport.width / 2f;
        viewport.y -= viewport.height / 2f;

        panel.setPosition((viewport.getWidth() - panel.getWidth()) / 2, viewport.getHeight() - panel.getHeight());
        miniMap.update(true);
    }

    /**
     * Moves the view offset.
     *
     * @param target The new target vector
     */
    public void setTarget(Vector2 target) {
        this.target = target;
    }

    public void setViewport(Rectangle view) {
        viewport = view;
    }

    public void setViewport(float x, float y, float w, float h) {
        if (viewport == null) {
            viewport = new Rectangle(x, y, w, h);
        } else {
            viewport.set(x, y, w, h);
        }
    }

    /**
     * @return The current view offset.
     */
    public Vector2 getOffset() {
        return offset;
    }

    /**
     * @return The current view port.
     */
    public Rectangle getViewport() {
        return viewport;
    }

    private Vector2 viewportOffset = new Vector2();

    /**
     * @return The current view port + offset.
     */
    public Vector2 getViewportOffset() {
        return viewportOffset.set(offset).add(viewport.x, viewport.y);
    }

}
