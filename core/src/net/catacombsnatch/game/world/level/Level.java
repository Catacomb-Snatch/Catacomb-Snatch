package net.catacombsnatch.game.world.level;

import com.artemis.World;
import com.artemis.WorldConfiguration;
import com.artemis.managers.GroupManager;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import net.catacombsnatch.game.entity.systems.HealthSystem;
import net.catacombsnatch.game.entity.systems.MovementSystem;
import net.catacombsnatch.game.entity.systems.RenderSystem;
import net.catacombsnatch.game.player.LevelPlayer;
import net.catacombsnatch.game.player.Player;
import net.catacombsnatch.game.screen.Tickable;
import net.catacombsnatch.game.util.Finishable;
import net.catacombsnatch.game.world.Campaign;
import net.catacombsnatch.game.world.level.generator.LevelGenerator;
import net.catacombsnatch.game.world.tile.Tile;

public class Level extends World implements Tickable, Finishable {
    // Entity management
    protected Array<LevelPlayer> players;

    // Tiles
    protected Tile[] tiles;

    // General level information
    protected final Campaign campaign;
    protected final LevelGenerator generator;

    protected Array<Vector2> spawns;

    protected int width, height;

    protected boolean debug = false;
    protected boolean finished = false;
    protected boolean initialized = false;

    // todo move this (duh)
    protected static RenderSystem renderSystem = new RenderSystem();

    public Level(Campaign campaign, LevelGenerator generator, int width, int height) {
        super(new WorldConfiguration()
                .setSystem(new HealthSystem())
                .setSystem(new MovementSystem())
                .setSystem(renderSystem)

                .setSystem(new GroupManager()));

        // Entity management
        this.players = new Array<LevelPlayer>();

        // Tiles
        this.tiles = new Tile[width * height];

        // General level information
        this.campaign = campaign;
        this.generator = generator;

        this.spawns = generator.getSpawnLocations();

        this.width = width;
        this.height = height;
    }

    public void initialize() {
        // super.initialize();

        // Add players
        for (Player player : campaign.getPlayers()) {
            players.add(player.getLevelPlayer());
        }

        initialized = true;
    }

    @Override
    public void tick(float delta) {
        if (!initialized) return;

        // Tick through tiles (for animations, ...)
        for (Tile tile : tiles) {
            if (tile == null) continue;

            tile.tick(delta);
        }

        // Update entities and process systems
        setDelta(delta);
        process();
    }

    /**
     * @return An array of all stored tiles (size = level width * level height).
     */
    public Tile[] getTiles() {
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
    public Tile getTile(int x, int y) {
        return (x < 0 || y < 0 || x >= width || y >= height) ? null : tiles[x + y * width];
    }

    /**
     * Sets a tile at the given x and y coordinate.
     * If x and / or y are out of the level boundaries, nothing is being set.
     *
     * @param tile The tile object to set
     * @param x    The x position
     * @param y    The y position
     */
    public void setTile(Tile tile, int x, int y) {
        if (x < 0 || y < 0 || x >= width || y >= height) return;

        tiles[x + y * width] = tile;
    }

    @Override
    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    @Override
    public boolean hasFinished() {
        return finished;
    }

    /**
     * @param debug True if debug mode should be activated.
     */
    public void setDebugMode(boolean debug) {
        this.debug = debug;
    }

    /**
     * @return True if debug mode is activated, otherwise false.
     */
    public boolean isDebugModeActive() {
        return debug;
    }

    /**
     * @return The level width <b>in tiles</b>
     */
    public int getWidth() {
        return width;
    }

    /**
     * @return The level height <b>in tiles</b>
     */
    public int getHeight() {
        return height;
    }

    /**
     * @return The {@link Campaign} that started this level.
     */
    public Campaign getCampaign() {
        return campaign;
    }

    /**
     * @return The {@link LevelGenerator} used to generate this level
     */
    public LevelGenerator getGenerator() {
        return generator;
    }

    public Vector2 getNextSpawnLocation() {
        return (spawns.size == 0) ? null : spawns.removeIndex(spawns.size - 1);
    }

}
