package net.catacombsnatch.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import gnu.trove.map.hash.THashMap;
import net.catacombsnatch.game.resource.Art;

import java.util.Map;

/**
 * Represents a utility to show a GUI Debug overlay
 * for visual representation of variable values over time.
 *
 * @author spaceemotion
 * @version 1.0
 */
public class Monitoring {
    private static final int SAMPLES = 128;
    private static final Map<String, Property> PROPERTIES = new THashMap<>();
    private static final Color[] COLORS = {
            new Color(0x8E8E93),
            new Color(0xFF3555),
            new Color(0xFF4031),
            new Color(0xFF950E),
            new Color(0xFFCA15),
            new Color(0x3DD666),
            new Color(0x44C9F9),
            new Color(0x00ABDB),
            new Color(0x007FFE),
            new Color(0x505CD5)
    };

    private static BitmapFont font;


    public static void update(String name, boolean value) {
        update(name, value ? 1 : 0);
    }

    public static void update(String name, float value) {
        Property property = PROPERTIES.get(name);
        if (property == null) {
            property = new Property();
            property.color = COLORS[PROPERTIES.size() % COLORS.length];

            PROPERTIES.put(name, property);
        }

        if (value > property.maximum) property.maximum = value;
        if (value < property.minimum) property.minimum = value;

        final int size = property.values.length - 1;
        System.arraycopy(property.values, 1, property.values, 0, size);
        property.values[size] = value;
    }

    public static float get(String name) {
        final Property property = PROPERTIES.get(name);
        return property != null ? property.values[SAMPLES - 1] : Float.NaN;
    }

    public static void remove(String name) {
        PROPERTIES.remove(name);
    }

    public static void render(Batch batch) {
        if (font == null) {
            font = Art.skin.get(Label.LabelStyle.class).font;
        }

        final ShapeRenderer renderer = new ShapeRenderer();
        renderer.begin(ShapeRenderer.ShapeType.Filled);

        int y = 1, base;
        for (Property property : PROPERTIES.values()) {
            renderer.setColor(property.color);

            base = y;
            for (int x = 64, i = 0; i < SAMPLES; i++) {
                renderer.rectLine(x, y, x += 2,
                        y = Math.max(base, base + Math.round(property.values[i] / property.maximum * 36)), 2);
            }

            y = base + 48;
        }

        renderer.end();

        batch.begin();
        y = 10;
        Property property;

        for (Map.Entry<String, Property> entry : PROPERTIES.entrySet()) {
            property = entry.getValue();

            font.draw(batch, entry.getKey(), 2, y + 30);
            font.draw(batch, "max:", 2, y + 20);
            font.draw(batch, Float.toString(property.maximum), 26, y + 20);
            font.draw(batch, "min:", 2, y + 10);
            font.draw(batch, Float.toString(property.minimum), 26, y + 10);
            font.draw(batch, "val:", 2, y);
            font.draw(batch, Float.toString(property.values[SAMPLES - 1]), 26, y);

            y += 48;
        }

        batch.end();
    }


    private static class Property {
        private final float[] values = new float[SAMPLES];
        private float maximum = Float.MIN_VALUE;
        private float minimum = Float.MAX_VALUE;
        private Color color;
    }

}
