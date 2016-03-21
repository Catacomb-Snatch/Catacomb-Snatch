package net.catacombsnatch.game.entity.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Represents an object that can be represented on a {@link net.catacombsnatch.game.world.level.MiniMap}.
 *
 * @author spaceemotion
 * @version 1.0
 */
public class MiniMapObject implements Component {
    public Color color;


    public MiniMapObject setColor(Color color) {
        this.color = color;
        return this;
    }

    /**
     * Calculates the average color of a texture region.
     *
     * @param region The texture region
     * @return The average color
     */
    public static Color getAverageColorFor(TextureRegion region) {
        region.getTexture().getTextureData().prepare();
        final Pixmap pixmap = region.getTexture().getTextureData().consumePixmap();

        float total = region.getRegionHeight() * region.getRegionWidth();
        float r = 0, g = 0, b = 0;

        for (int y = region.getRegionY(), yMax = region.getRegionY() + region.getRegionHeight(); y < yMax; y++) {
            for (int x = region.getRegionX(), xMax = region.getRegionX() + region.getRegionWidth(); x < xMax; x++) {
                int pixel = pixmap.getPixel(x, y);

                // Ignore certain alpha colors
                if (((pixel & 0x000000ff)) / 255f < 0.0625f) {
                    total--;
                    continue;
                }

                r += ((pixel & 0xff000000) >>> 24) / 255f;
                g += ((pixel & 0x00ff0000) >>> 16) / 255f;
                b += ((pixel & 0x0000ff00) >>> 8) / 255f;
            }
        }

        if (region.getTexture().getTextureData().disposePixmap()) {
            pixmap.dispose();
        }

        return new Color(r / total, g / total, b / total, 1f);
    }

}
