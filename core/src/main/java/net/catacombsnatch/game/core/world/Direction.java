package net.catacombsnatch.game.core.world;

import net.catacombsnatch.game.core.world.tile.Tile;

import com.badlogic.gdx.math.Vector2;

public enum Direction {
	// Linear sides
	NORTH(0, 0, 1, 0x1), EAST(2, 1, 0, 0x2), SOUTH(4, 0, -1, 0x4), WEST(6, -1, 0, 0x8),
	
	// Edges
	NORTH_EAST(1, 1, 1, 0x9), EAST_SOUTH(3, 1, -1, 0x10), SOUTH_WEST(5, -1, -1, 0x12), WEST_NORTH(7, -1, 1, 0xF);
	
	private int face;
	private Vector2 vector;
	private byte weight;
	
	private Direction(int f, float x, float y, int b) {
		face = f;
		vector = new Vector2(x, y);
		weight = (byte) b;
	}
	
	/** @return A number used for facing entities (e.g. character images). */
	public int getFace() {
		return face;
	}
	
	public Vector2 getFor(float x, float y) {
		return new Vector2(x, y).add(vector);
	}
	
	/** @return A mask used in {@link Tile}s for drawing edges and corners. */
	public byte getMask() {
		return isEdge() ? (byte) (weight >> 3) : weight;
	}
	
	public boolean isEdge() {
		return weight > 0x8;
	}
	
}
