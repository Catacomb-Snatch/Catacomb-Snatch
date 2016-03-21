package net.catacombsnatch.game.world.level;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import net.catacombsnatch.game.entity.Entities;
import net.catacombsnatch.game.entity.components.Destroyable;
import net.catacombsnatch.game.entity.systems.HealSystem;
import net.catacombsnatch.game.entity.systems.MovementSystem;
import net.catacombsnatch.game.entity.systems.RenderSystem;
import net.catacombsnatch.game.screen.Tickable;
import net.catacombsnatch.game.util.Finishable;
import net.catacombsnatch.game.world.Campaign;
import net.catacombsnatch.game.world.level.generator.LevelGenerator;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Level extends PooledEngine implements Tickable, Finishable {
    private final Entity[] tiles;
    private final List<Entity> playerEntities;

    private boolean finished = false;

    /** The {@link Campaign} that started this level. */
    public final Campaign campaign;

    /** The {@link LevelGenerator} used to generate this level */
    public final LevelGenerator generator;

    /** An array of all possible spawn locations */
    public final Array<Vector2> spawns;

    /** An auto-updating, unmodifiable collection to get all player entities within this level */
    public final List<Entity> players;

    /** The level width <b>in tiles</b> */
    public final int width;

    /** The level height <b>in tiles</b> */
    public final int height;


    public Level(Campaign campaign, LevelGenerator generator, int width, int height) {
        this.tiles = new Entity[width * height];

        this.campaign = campaign;
        this.generator = generator;
        this.width = width;
        this.height = height;

        this.spawns = generator.getSpawnLocations();

        this.playerEntities = new LinkedList<>();
        this.players = Collections.unmodifiableList(playerEntities);

        // Add Entity systems
        addSystem(new HealSystem());
        addSystem(new MovementSystem());
        addSystem(new RenderSystem());

        // Add listener for easier player access
        addEntityListener(Entities.players, new EntityListener() {
            public void entityAdded(Entity entity)   { playerEntities.add(entity); }
            public void entityRemoved(Entity entity) { playerEntities.remove(entity); }
        });

        // Call destroyable callback in case an entity got removed
        addEntityListener(Family.all(Destroyable.class).get(), new EntityListener() {
            public void entityAdded(Entity entity)   {}
            public void entityRemoved(Entity entity) { Entities.destroyable.get(entity).call(Level.this, entity); }
        });
    }

    @Override
    public void tick(float delta) {
        update(delta);
    }

    /**
     * @return An array of all stored tiles (size = level width * level height).
     */
    public Entity[] getTiles() {
        return tiles;
    }

    /**
     * Gets a tile at the given x and y coordinate.
     * If x and / or y are out of the level boundaries, null is getting returned.
     *
     * @param x The x position
     * @param y The y position
     * @return The tile, or null when x and / or y are out of level boundaries.
     */
    public Entity getTile(int x, int y) {
        return (x < 0 || y < 0 || x >= width || y >= height) ? null : tiles[x + y * width];
    }

    /**
     * Sets a tile at the given x and y coordinate.
     * If x and / or y are out of the level boundaries, nothing is being set.
     *
     * This should only be used for tiles that do not move,
     * any tiles that have a {@link net.catacombsnatch.game.entity.components.Velocity} component on them
     * will throw an {@link IllegalArgumentException}.
     *
     * @param tile The tile object to set
     * @param x    The x position
     * @param y    The y position
     */
    public void setTile(Entity tile, int x, int y) {
        if (x < 0 || y < 0 || x >= width || y >= height) return;

        if (Entities.velocity.get(tile) != null) {
            throw new IllegalArgumentException("Trying to add static tile with velocity to level at " + x + ", " + y);
        }

        try {
            final int index = x + y * width;
            final Entity previous = tiles[index];
            if (previous != null) {
                removeEntity(previous);
            }

            tiles[index] = tile;
            addEntity(tile);

        } catch (IllegalArgumentException ignored) {}
    }

    public Vector2 getNextSpawnLocation() {
        return spawns.size > 0 ? spawns.removeIndex(spawns.size - 1) : null;
    }

    @Override
    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    @Override
    public boolean hasFinished() {
        return finished;
    }

}
