package net.catacombsnatch.game.world;

import net.catacombsnatch.game.world.tile.Tile;

import com.badlogic.gdx.math.Vector2;

/**
 * Represents a direction or relative location.
 * <p>
 * <p>Linear sides: {@link #NORTH}, {@link #EAST}, {@link #SOUTH} and {@link #WEST}.</p>
 * <p>Edges: {@link #NORTH_EAST}, {@link #EAST_SOUTH}, {@link #SOUTH_WEST} and {@link #WEST_NORTH}.</p>
 */
public enum Direction {

    NORTH(4, 0, -1, 0x1),
    NORTH_EAST(5, 1, -1, 0x9),

    EAST(6, 1, 0, 0x2),
    EAST_SOUTH(7, 1, 1, 0x10),

    SOUTH(0, 0, 1, 0x4),
    SOUTH_WEST(1, -1, 1, 0x12),

    WEST(2, -1, 0, 0x8),
    WEST_NORTH(3, -1, -1, 0xF);

    public final static int count = values().length;

    private int face;
    private Vector2 vector;
    private byte weight;

    private Direction(int f, float x, float y, int b) {
        face = f;
        vector = new Vector2(x, y);
        weight = (byte) b;
    }

    public static Direction getDirectionFor(Vector2 vec) {
        return getDirectionFor(vec.x, vec.y);
    }

    public static Direction getDirectionFor(float x, float y) {
        Vector2 cmp = new Vector2((x < 0) ? -1 : (x == 0) ? 0 : 1, (y < 0) ? -1 : (x == 0) ? 0 : 1);
        Direction[] dirs = Direction.values();

        for (int i = 0; i < dirs.length; i++) {
            if (!dirs[i].vector.equals(cmp)) continue;

            return dirs[i];
        }

        return NORTH;
    }

    /**
     * @return A number used for facing entities (e.g. character images).
     */
    public int getFace() {
        return face;
    }

    public Vector2 getFor(Vector2 vec) {
        return vec.cpy().add(vector);
    }

    public Vector2 getFor(float x, float y) {
        return new Vector2(x, y).add(vector);
    }

    public float getX() {
        return vector.x;
    }

    public float getY() {
        return vector.y;
    }

    /**
     * @return A mask used in {@link Tile}s for drawing edges and corners.
     */
    public byte getMask() {
        return isEdge() ? (byte) (weight >> 3) : weight;
    }

    public boolean isEdge() {
        return weight > 0x8;
    }

}
