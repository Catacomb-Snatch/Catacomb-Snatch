package net.catacombsnatch.game.core.resources;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import java.io.BufferedReader;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Properties;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Language {
	public final static String TAG = "[Language]";
	public final static String DIRECTORY = "languages/";
	public final static String DEFAULT = "en";
	
	protected final static Map<String, Properties> languages;
	protected final static List<Locale> vanilla;
	protected static Properties last;
	protected static String language;
	
	static {
		languages = new HashMap<String, Properties>();
		vanilla = new ArrayList<Locale>();
		
		try {
			Gdx.app.debug(TAG, "Reading default languages");
			
			BufferedReader br = new BufferedReader(Gdx.files.internal(DIRECTORY + "index").reader());
			String line;
			
			while((line = br.readLine()) != null) {
				if(line.startsWith("#") || line.length() < 2) continue; // 2 because of '\n'
				
				FileHandle file = Gdx.files.internal(DIRECTORY + line);
				
				if(file != null) {
					vanilla.add(new Locale(file.name()));
					loadFile(file);
				} else throw new Exception("Could not find file for: " + line);
			}
			
		} catch (Exception ex) {
			Gdx.app.error(TAG, "Error reading default languages", ex);
		}
	}
	
	
	/**
	 * Loads language files from a {@link FileHandle}.
	 * This allows loading languages outside of the internal file system.
	 * 
	 * Valid language files have a '.lang' extension.
	 * 
	 * @param files The {@link FileHandle} of the directory
	 * @return True if successful, otherwise false
	 */
	public static boolean load( FileHandle files ) {
		if(!files.isDirectory()) return false;
		
		boolean outcome = true;
		
		for(FileHandle file : files.list()) {
			if(!loadFile(file)) outcome = false;
		}
		
		return outcome;
	}
	
	
	/**
	 * Sets the current language.
	 * 
	 * @param lang The language to set (e.g. 'en').
	 */
	public static void set( String lang ) {
		language = lang;
		last = languages.get(language);
	}
	
	
	/**
	 * Returns a language string by property key.
	 * 
	 * @param property The property key
	 * @return The language string
	 */
	public static String get( String property ) {
		if(last == null) {
			// We are keeping a reference to the current language
			// to reduce lookup time (searching the map).
			last = languages.get(language);
		}
		
		if ( last != null && last.containsKey( property ) ) {
			return last.getProperty( property );
		}
		
		Properties fallback = languages.get(DEFAULT);
		return fallback != null ? fallback.getProperty(property) : "{" + property + "}";
	}

	
	/**
	 * Returns a language string with arguments by property key.
	 * 
	 * @param property The property key
	 * @param args The arguments
	 * @return The formatted language string
	 */
	public static String getf( String property, Object... args ) {
		return MessageFormat.format( get( property ), args );
	}
	
	
	/**
	 * Loads a language from a {@link FileHandle}
	 * 
	 * @param file The {@link FileHandle} of the language file to load
	 * @return True on success, otherwise false
	 */
	protected static boolean loadFile( FileHandle file ) {
		if(!file.isDirectory() && file.extension().equalsIgnoreCase(".lang")) {
			try {
				Properties lang = languages.get(file.name());

				if(lang == null) {
					lang = new Properties();
					languages.put(file.name(), lang);
				}

				lang.load(file.read());

			} catch (Exception io) {
				Gdx.app.error(TAG, "Could not load language file with name " + file.nameWithoutExtension(), io);
				return false;
			}

		} else {
			Gdx.app.debug(TAG, "Ignored " + file.name() + " while loading language file");
		}
		
		return true;
	}
	
}
