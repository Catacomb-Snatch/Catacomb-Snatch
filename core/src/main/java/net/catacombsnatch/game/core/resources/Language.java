package net.catacombsnatch.game.core.resources;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.Properties;

import com.badlogic.gdx.Gdx;

public class Language {
	protected final Properties language;
	protected final Properties fallback;

	public Language( Locale locale ) {
		language = new Properties();
		fallback = new Properties();

		String lang = locale.getLanguage();

		if ( lang != "en" ) {
			try {
				language.load( Gdx.files.internal( "languages/" + lang + ".lang" ).read() );
			} catch ( Exception e ) {
				System.err.println( "Could not load language file for language '" + lang + "', using fallback language 'en'" );
			}
		}

		try {
			fallback.load( Gdx.files.internal( "languages/en.lang" ).read() );
		} catch ( Exception e ) {
			System.err.println( "Could not load default language file!" );
			e.printStackTrace();
		}
	}

	/**
	 * Returns a language string by property key.
	 * 
	 * @param property The property key
	 * @return The language string
	 */
	public String get( String property ) {
		if ( language != null && language.containsKey( property ) )
			return language.getProperty( property );
		else if ( fallback != null && fallback.containsKey( property ) )
			return fallback.getProperty( property );
		else return "{" + property + "}";
	}

	/**
	 * Returns a language string with arguments by property key.
	 * 
	 * @param property The property key
	 * @param args The arguments
	 * @return The formatted language string
	 */
	public String getf( String property, String... args ) {
		return MessageFormat.format( get( property ), (Object[]) args );
	}
}
