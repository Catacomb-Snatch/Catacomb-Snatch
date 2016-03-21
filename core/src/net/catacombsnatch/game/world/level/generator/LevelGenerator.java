package net.catacombsnatch.game.world.level.generator;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import gnu.trove.map.hash.THashMap;
import net.catacombsnatch.game.world.Campaign;
import net.catacombsnatch.game.world.level.Level;
import net.catacombsnatch.game.world.level.generator.options.GeneratorOption;

import java.util.Collections;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public abstract class LevelGenerator {
    private final Map<String, GeneratorOption<?>> options;

    /** The random number generator for this level. */
    public final Random random;


    public LevelGenerator() {
        this(new Random());
    }

    public LevelGenerator(Random random) {
        this.random = random;
        options = new THashMap<>();
    }

    /**
     * Generates a new Level for the given {@link net.catacombsnatch.game.world.Campaign}
     *
     * @param campaign The campaign triggered this generation
     * @return The newly generated level
     */
    public abstract Level generate(Campaign campaign);

    /**
     * A list of all possible spawn points <b>for players</b>.
     *
     * @return An array containing all possible spawn point vectors.
     */
    public abstract Array<Vector2> getSpawnLocations();

    public void addOption(GeneratorOption value) {
        options.put(value.name.toLowerCase(), value);
    }

    public Set<String> getOptions() {
        return Collections.unmodifiableSet(options.keySet());
    }

    public GeneratorOption<?> getOption(String name) {
        return options.get(name.toLowerCase());
    }

}
