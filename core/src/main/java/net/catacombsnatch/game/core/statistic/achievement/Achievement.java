package net.catacombsnatch.game.core.statistic.achievement;

import net.catacombsnatch.game.core.player.Player;

import com.badlogic.gdx.graphics.Texture;

public abstract class Achievement {
	
	protected final AchievementType type;
	protected final Texture icon;
	protected final String name, description;
	
	protected Achievement(AchievementType type, Texture icon, String name, String description) {
		this.type = type;
		this.icon = icon;
		this.name = name;
		this.description = description;
	}
	
	/**
	 * Checks whether or not this achievement should be given to the {@link Player}.
	 * 
	 * @param player The {@link Player} to check.
	 * @return True if the player meets the criteria, otherwise false.
	 */
	public abstract boolean meetsCriteria(Player player);
	
	/** @return The {@link AchievementType} of this achievement */
	public AchievementType getType() {
		return type;
	}
	
	/** @return The icon displayed for this achievement */
	public Texture getIcon() {
		return icon;
	}
	
	/** @return The name used for this achievement */
	public String getName() {
		return name;
	}
	
	/** @return The description for this achievement */
	public String getDescription() {
		return description;
	}
}
