package net.catacombsnatch.game.core.resource;

/** Represents build information added during maven compilation */
public final class Version {

	/** The current build number */
	public static final String BUILD_NUMBER = "${BUILD_NUMBER}";

	/** The defined pom version (e.g. 1.0-SNAPSHOT) */
	public static final String POM_VERSION = "${pom.version}";

}