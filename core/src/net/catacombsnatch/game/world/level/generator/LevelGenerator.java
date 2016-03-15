package net.catacombsnatch.game.world.level.generator;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import net.catacombsnatch.game.world.Campaign;
import net.catacombsnatch.game.world.level.Level;
import net.catacombsnatch.game.world.level.generator.options.GeneratorOption;

import java.util.Random;

public abstract class LevelGenerator {
    protected Random random;
    protected Array<GeneratorOption<?>> options;


    public LevelGenerator() {
        this(new Random());
    }

    public LevelGenerator(Random r) {
        random = r;

        options = new Array<GeneratorOption<?>>();
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

    /**
     * @return The random number generator for this level.
     */
    public Random randomizer() {
        return random;
    }

    public Array<String> getOptions() {
        Array<String> keys = new Array<String>();

        for (GeneratorOption<?> option : options) {
            keys.add(option.getName());
        }

        return keys;
    }

    public GeneratorOption<?> getOption(String name) {
        for (GeneratorOption<?> option : options) {
            if (!option.getName().equalsIgnoreCase(name)) continue;

            return option;
        }

        return null;
    }

}
