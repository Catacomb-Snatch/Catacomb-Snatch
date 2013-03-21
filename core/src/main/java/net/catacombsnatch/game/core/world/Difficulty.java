package net.catacombsnatch.game.core.world;

import net.catacombsnatch.game.core.Game;

public enum Difficulty {
	EASY(Game.language.get( "difficulty.easy" ), .5f, .5f, 1.5f, .5f, false, 25, 3, 30),
	NORMAL(Game.language.get( "difficulty.normal" ), 1, 1, 1, 1, false, 25, 7, 20),
	HARD(Game.language.get( "difficulty.hard" ), 3, 3, .5f, 1.5f, true, 25, 12, 15),
	NIGHTMARE(Game.language.get( "difficulty.nightmare" ), 6, 5, .25f, 2.5f, true, 15, 100000, 10);

	private String name;

	private final float mobHealthModifier;
	private final float mobStrengthModifier;
	private final float mobSpawnModifier;
	private final float shopCostsModifier;

	private final int regenerationInterval;
	private final int allowedMobDensity;
	private final int coinLifespan;

	private boolean mobRegenerationAllowed;

	private Difficulty( String name, float mobHealthModifier, float mobStrengthModifier, float mobSpawnModifier, float shopCostsModifier, boolean mobRegeneration, int regenerationInterval, int allowedMobDensity, int coinLifespan ) {
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
	 * Returns the name of this {@link Difficulty}.
	 * 
	 * @return The name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the calculated health in this {@link Difficulty}.
	 * 
	 * @param baseHealth The base health
	 * @return The calculated health
	 */
	public float getHealth( float baseHealth ) {
		return baseHealth * mobHealthModifier;
	}

	/**
	 * Returns the calculated strength in this {@link Difficulty}.
	 * 
	 * @param baseStrength The base strength
	 * @return The calculated strength
	 */
	public float getStrength( float baseStrength ) {
		return baseStrength * mobStrengthModifier;
	}

	/**
	 * Returns the calculated spawn frequency in this {@link Difficulty}.
	 * 
	 * @param baseFrequency The base frequency
	 * @return The calculated spawn frequency
	 */
	public float getSpawnFrequency( float baseFrequency ) {
		return baseFrequency * mobSpawnModifier;
	}

	/**
	 * Returns the calculated shop costs in this {@link Difficulty}.
	 * 
	 * @param baseFrequency The base costs
	 * @return The calculated shop costs
	 */
	public float getShopCosts( float baseCosts ) {
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
	 * Look up a Difficulty by the ordinal number
	 * 
	 * @param ordinal The number to look up
	 * @return The found {@link Difficulty}
	 */
	public static Difficulty getByInt( int ordinal ) {
		return Difficulty.values()[ordinal];
	}

	@Override
	public String toString() {
		return name;
	}
}
