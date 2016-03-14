package net.catacombsnatch.game.world.level;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
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

    protected Texture map;
    protected Pixmap pm;

    public Minimap(Level level, View view) {
        this.level = level;
        this.view = view;

        sprite = new Sprite(Art.skin.getAtlas().findRegion("minimap-frame"));

        map = new Texture(new Pixmap(40, 40, Pixmap.Format.RGBA8888), true);
    }

    Rectangle vp = new Rectangle();

    @Override
    public void render(Scene scene) {
        sprite.draw(scene.getBatch());

        vp.set(view.viewport);
        vp.x += view.offset.x;
        vp.y += view.offset.y;

        vp.x -= Screen.getWidth() / 2;
        vp.y += Screen.getHeight() / 2;

        pm = new Pixmap(40, 40, Pixmap.Format.RGBA8888);

        for (Tile tile : level.getTiles()) {
            if (tile == null) continue;

            pm.drawPixel((int) tile.getPosition().x - (int) (vp.x / Tile.WIDTH), (int) tile.getPosition().y - (int) (vp.height / Tile.HEIGHT) - (int) (vp.y / Tile.HEIGHT),
                    tile.getMinimapColor());
        }

        // TODO add entity icons

        map.draw(pm, 0, 0);

        pm.dispose();

        scene.getBatch().draw(map, sprite.getX() + 6, sprite.getY() + 5 + 80, 80, -80);
    }

    public void update(boolean resize) {
        if (resize) {
            sprite.setPosition(Screen.getWidth() - sprite.getWidth() - 2, Screen.getHeight() - sprite.getHeight());
        }
    }

}
