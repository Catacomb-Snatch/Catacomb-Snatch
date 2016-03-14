package net.catacombsnatch.game.world;

import java.util.HashMap;
import java.util.Map;

import net.catacombsnatch.game.resource.Language;

public class Difficulty {
    public final static Difficulty EASY = new Difficulty("difficulty.easy", .5f, .5f, 1.5f, .5f, false, 25, 3, 30);
    public final static Difficulty NORMAL = new Difficulty("difficulty.normal", 1, 1, 1, 1, false, 25, 7, 20);
    public final static Difficulty HARD = new Difficulty("difficulty.hard", 3, 3, .5f, 1.5f, true, 25, 12, 15);
    public final static Difficulty NIGHTMARE = new Difficulty("difficulty.nightmare", 6, 5, .25f, 2.5f, true, 15, 100000, 10);

    private final static Map<String, Difficulty> registry;

    static {
        registry = new HashMap<String, Difficulty>();

        EASY.register("easy");
        NORMAL.register("normal");
        HARD.register("hard");
        NIGHTMARE.register("nightmare");
    }

    private final String name;

    private final float mobHealthModifier;
    private final float mobStrengthModifier;
    private final float mobSpawnModifier;
    private final float shopCostsModifier;

    private final int regenerationInterval;
    private final int allowedMobDensity;
    private final int coinLifespan;

    private final boolean mobRegenerationAllowed;

    public Difficulty(String name, float mobHealthModifier, float mobStrengthModifier, float mobSpawnModifier, float shopCostsModifier, boolean mobRegeneration, int regenerationInterval, int allowedMobDensity, int coinLifespan) {
        this.name = name;

        this.mobHealthModifier = mobHealthModifier;
        this.mobStrengthModifier = mobStrengthModifier;
        this.mobSpawnModifier = mobSpawnModifier;
        this.shopCostsModifier = shopCostsModifier;
        this.mobRegenerationAllowed = mobRegeneration;
        this.regenerationInterval = regenerationInterval;
        this.allowedMobDensity = allowedMobDensity;
        this.coinLifespan = coinLifespan;
    }

    /**
     * Registers a difficulty under a given name
     *
     * @param as The internal registration name
     */
    protected final void register(String as) {
        registry.put(as, this);
    }

    /**
     * Returns the name of this {@link Difficulty}.
     *
     * @return The name
     */
    public String getName() {
        return Language.get(name);
    }

    /**
     * Returns the calculated health in this {@link Difficulty}.
     *
     * @param baseHealth The base health
     * @return The calculated health
     */
    public float getHealth(float baseHealth) {
        return baseHealth * mobHealthModifier;
    }

    /**
     * Returns the calculated strength in this {@link Difficulty}.
     *
     * @param baseStrength The base strength
     * @return The calculated strength
     */
    public float getStrength(float baseStrength) {
        return baseStrength * mobStrengthModifier;
    }

    /**
     * Returns the calculated spawn frequency in this {@link Difficulty}.
     *
     * @param baseFrequency The base frequency
     * @return The calculated spawn frequency
     */
    public float getSpawnFrequency(float baseFrequency) {
        return baseFrequency * mobSpawnModifier;
    }

    /**
     * Returns the calculated shop costs in this {@link Difficulty}.
     *
     * @param baseFrequency The base costs
     * @return The calculated shop costs
     */
    public float getShopCosts(float baseCosts) {
        return baseCosts * shopCostsModifier;
    }

    /**
     * Returns the regeneration interval in this {@link Difficulty}.
     *
     * @return The regeneration interval
     */
    public int getRegenerationInterval() {
        return regenerationInterval;
    }

    /**
     * Returns the allowed mob density in this {@link Difficulty}.
     *
     * @return The allowed mob density
     */
    public int getAllowedMobDensity() {
        return allowedMobDensity;
    }

    /**
     * Returns the coin lifespan in this {@link Difficulty}.
     *
     * @return The coin lifespan
     */
    public int getCoinLifespan() {
        return coinLifespan;
    }

    /**
     * Returns if mob health regeneration is allowed in this {@link Difficulty}.
     *
     * @return True if allowed, otherwise false
     */
    public boolean isMobRegenerationAllowed() {
        return mobRegenerationAllowed;
    }

    /**
     * Look up a Difficulty by its registered name
     *
     * @param ordinal The name to look up for
     * @return The found {@link Difficulty}
     */
    public static Difficulty getByName(String name) {
        return registry.get(name);
    }

    @Override
    public String toString() {
        return name;
    }
}
