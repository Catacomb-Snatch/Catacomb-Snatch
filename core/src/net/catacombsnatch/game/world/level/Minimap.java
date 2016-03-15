package net.catacombsnatch.game.world.level;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import net.catacombsnatch.game.resource.Art;
import net.catacombsnatch.game.scene.Scene;
import net.catacombsnatch.game.screen.Renderable;
import net.catacombsnatch.game.screen.Screen;
import net.catacombsnatch.game.world.tile.Tile;

public class Minimap implements Renderable {
    protected Level level;
    protected View view;
    protected Sprite sprite;

    protected static TextureRegion pixel;

    public Minimap(Level level, View view) {
        this.level = level;
        this.view = view;

        sprite = new Sprite(Art.skin.getAtlas().findRegion("minimap-frame"));

        if (pixel == null) {
            pixel = Art.skin.getAtlas().findRegion("white");
        }
    }

    protected Rectangle vp = new Rectangle();

    @Override
    public void render(Scene scene) {
        sprite.draw(scene.getBatch());

        vp.set(view.viewport);
        vp.x += view.offset.x;
        vp.y += view.offset.y;

        for (Tile tile : level.getTiles()) {
            if (tile == null) continue;

            float px = sprite.getX() + 7f;
            px += 2 * (int)((tile.getPosition().x - (vp.x / Tile.WIDTH) + (vp.width / 2f / Tile.WIDTH)));
            float py = sprite.getY() + 7f;
            py += 2 * (int)((-tile.getPosition().y + (vp.height / Tile.HEIGHT) - (vp.y / Tile.HEIGHT) - (vp.height / 2f / Tile.HEIGHT)));

            if (px >= sprite.getX() + 7f && py >= sprite.getY() + 7f &&
                    px < sprite.getX() + 82f && py < sprite.getY() + 82f) {
                int color = tile.getMinimapColor();
                scene.getSpriteBatch().setColor(
                        ((color & 0xff000000) >>> 24) / 255f,
                        ((color & 0x00ff0000) >>> 16) / 255f,
                        ((color & 0x0000ff00) >>> 8) / 255f,
                        1f);
                scene.getSpriteBatch().draw(pixel, px, py, 2f, 2f);
            }
        }

        // TODO add entity icons

        scene.getSpriteBatch().setColor(1f, 1f, 1f, 1f);
    }

    public void update(boolean resize) {
        if (resize) {
            sprite.setPosition(Screen.getWidth() - sprite.getWidth() - 2, Screen.getHeight() - sprite.getHeight());
        }
    }

}
