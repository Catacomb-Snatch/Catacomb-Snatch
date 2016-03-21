package net.catacombsnatch.game.entity;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import net.catacombsnatch.game.entity.components.BoundingBox;
import net.catacombsnatch.game.entity.components.Destroyable;
import net.catacombsnatch.game.entity.components.Health;
import net.catacombsnatch.game.entity.components.MiniMapObject;
import net.catacombsnatch.game.entity.components.Position;
import net.catacombsnatch.game.entity.components.Rotation;
import net.catacombsnatch.game.entity.components.Sprite;
import net.catacombsnatch.game.entity.components.Velocity;
import net.catacombsnatch.game.world.level.Level;
import net.catacombsnatch.game.world.tiles.Tiles;

public final class Entities {
    public static final ComponentMapper<Destroyable>   destroyable = ComponentMapper.getFor(Destroyable.class);
    public static final ComponentMapper<Health>        health      = ComponentMapper.getFor(Health.class);
    public static final ComponentMapper<MiniMapObject> miniMap     = ComponentMapper.getFor(MiniMapObject.class);
    public static final ComponentMapper<Position>      position    = ComponentMapper.getFor(Position.class);
    public static final ComponentMapper<Rotation>      rotation    = ComponentMapper.getFor(Rotation.class);
    public static final ComponentMapper<Sprite>        sprite      = ComponentMapper.getFor(Sprite.class);
    public static final ComponentMapper<Velocity>      velocity    = ComponentMapper.getFor(Velocity.class);

    public static final Family players = Family
            .all(Position.class, Velocity.class, Health.class, Rotation.class, Sprite.class)
            .get();


    private Entities() {}

    // The following are factory methods to create new entities with component presets

    public static Entity createPlayer(Level level, TextureRegion[][] spriteSheet) {
        final Entity player = level.createEntity();
        player.add(level.createComponent(Position.class).set(level.getNextSpawnLocation())); // TODO: This can return null!
        player.add(level.createComponent(Velocity.class).reset());
        player.add(level.createComponent(Health.class).reset(20)); // TODO change default based on selected character
        player.add(level.createComponent(Sprite.class).set(spriteSheet[0][0]));

        return player;
    }

    public static Entity createWithPosition(Level level, int x, int y) {
        final Entity tile = level.createEntity();
        tile.add(level.createComponent(Position.class).set(x, y));

        return tile;
    }

    private static Entity createBaseTile(Level level, int x, int y, Color color, boolean isWall) {
        x *= Tiles.SIZE;
        y *= Tiles.SIZE;

        final Entity tile = createWithPosition(level, x, y);
        tile.add(level.createComponent(MiniMapObject.class).setColor(color));

        if (isWall) {
            tile.add(new BoundingBox().set(x, y, Tiles.SIZE));
        }

        return tile;
    }

    public static Entity createTile(Level level, int x, int y, Color color, TextureRegion texture, boolean isWall) {
        final Entity tile = createBaseTile(level, x, y, color, isWall);
        tile.add(level.createComponent(Sprite.class).set(texture));
        return tile;
    }

    public static Entity createTile(Level level, int x, int y, Color color, TextureRegion[] textures, boolean isWall) {
        return createTile(level, x, y, color, textures[level.generator.random.nextInt(textures.length)], isWall);
    }

}
