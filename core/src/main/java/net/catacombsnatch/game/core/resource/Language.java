package net.catacombsnatch.game.core.resource;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import net.catacombsnatch.game.core.util.FileUtil;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class Language {
	public final static String TAG = "[Language]";
	public final static String DIRECTORY = "lang/";
	public final static String DEFAULT = "en";
	
	protected final static Map<String, LanguageEntry> languages;
	protected final static List<Locale> vanilla;
	protected static LanguageEntry last;
	protected static String language;
	
	static {
		languages = new HashMap<String, LanguageEntry>();
		vanilla = new ArrayList<Locale>();
		
		try {
			Gdx.app.debug(TAG, "Reading default languages");
			
			List<String> langs = FileUtil.readSimpleFile(Gdx.files.internal(DIRECTORY + "index"));
			
			for(String s : langs) {
				FileHandle file = Gdx.files.internal(DIRECTORY + s);
				
				if(file != null) {
					vanilla.add(new Locale(file.name()));
					loadFile(file);
				} else throw new Exception("Could not find file for: " + s);
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
		if(!files.isDirectory()) return loadFile(files);
		
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
		
		if (last != null) {
			String msg = last.getMessage(property);
			if(msg != null) return msg;
		}
		
		LanguageEntry fallback = languages.get(DEFAULT);
		if(fallback != null) {
			String str = fallback.getMessage(property);
			if(str != null) return str;
		}
		
		return "{" + property + "}";
	}

	
	/**
	 * Returns a language string with arguments by property key.
	 * 
	 * @param property The property key
	 * @param args The arguments
	 * @return The formatted language string
	 */
	public static String getf( String property, Object... args ) {
		MessageFormat format = null;
		LanguageEntry lang = last;
		
		if(last == null) last = languages.get(language);
		if (lang == null) lang = languages.get(DEFAULT);
		
		format = lang.getFormat(property);
		
		return format != null ? format.format(args) : "{" + property + "}";
	}
	
	
	/**
	 * Loads a language from a {@link FileHandle}
	 * 
	 * @param file The {@link FileHandle} of the language file to load
	 * @return True on success, otherwise false
	 */
	protected static boolean loadFile( FileHandle file ) {
		if(!file.isDirectory() && file.extension().equalsIgnoreCase("lang")) {
			try {
				LanguageEntry lang = languages.get(file.name());

				if(lang == null) {
					lang = new LanguageEntry(new Properties());
					languages.put(file.nameWithoutExtension(), lang);
				}

				lang.addProperties(file);

			} catch (Exception io) {
				Gdx.app.error(TAG, "Could not load language file with name " + file.nameWithoutExtension(), io);
				return false;
			}

		} else {
			Gdx.app.debug(TAG, "Ignored " + file.name() + " while loading language file");
		}
		
		return true;
	}
	
	protected static class LanguageEntry {
		protected final Properties strings;
		protected final Map<String, MessageFormat> msgCache;
		
		public LanguageEntry(Properties properties) {
			strings = properties;
			msgCache = new HashMap<String, MessageFormat>();
		}
		
		public void addProperties(FileHandle file) throws IOException {
			strings.load(file.read());
		}
		
		public String getMessage(String key) {
			return strings.getProperty(key);
		}
		
		public MessageFormat getFormat(String key) {
			MessageFormat format = msgCache.get(key);
			
			if(format == null) {
				format = new MessageFormat(strings.getProperty(key));
				msgCache.put(key, format);
			}
			
			return format;
		}
		
	}
	
}
