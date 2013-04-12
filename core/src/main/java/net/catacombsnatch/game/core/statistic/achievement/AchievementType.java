package net.catacombsnatch.game.core.statistic.achievement;

import net.catacombsnatch.game.core.resource.Language;

public enum AchievementType {
	DEFAULT("achievement.type.default"),
	HIDDEN("achievement.type.hidden");
	
	private final String name;
	
	private AchievementType(String name) {
		this.name = name;
	}
	
	/** @return The language-specific formatted name for this type */
	public String getName() {
		return Language.get(name);
	}
	
}
