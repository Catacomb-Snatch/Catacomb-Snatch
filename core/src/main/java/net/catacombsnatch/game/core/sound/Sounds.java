package net.catacombsnatch.game.core.sound;

public enum Sounds {
	/** The title theme track name */
	TITLE_THEME("ThemeTitle"),

	/** The end theme track name */
	END_THEME("ThemeEnd"),

	/** First background theme track name */
	BACKGROUND_TRACK_1("Background 1"),

	/** Second background theme track name */
	BACKGROUND_TRACK_2("Background 2"),

	/** Third background theme track name */
	BACKGROUND_TRACK_3("Background 3"),

	/** Fourth background theme track name */
	BACKGROUND_TRACK_4("Background 4"),

	/** The "Shot 1" sound name */
	SOUND_SHOT_1("Shot 1"),

	/** The Explosion sound name */
	SOUND_EXPLOSION("Explosion");

	public final String name;

	Sounds( String name ) {
		this.name = name;
	}
}
