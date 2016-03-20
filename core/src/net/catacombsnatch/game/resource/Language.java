package net.catacombsnatch.game.resource;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import gnu.trove.map.hash.THashMap;
import net.catacombsnatch.game.util.FileUtil;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

public class Language {
    private final static String TAG = "[Language]";
    private final static String DIRECTORY = "lang/";
    private final static Locale FALLBACK = Locale.ENGLISH;

    private final static Map<Locale, LanguageEntry> LANGUAGES;

    private static LanguageEntry last;
    private static Locale language;

    static {
        LANGUAGES = new THashMap<>();

        try {
            Gdx.app.debug(TAG, "Reading default languages");

            // Read all language files listed inside the index
            FileHandle file;
            for (String s : FileUtil.readSimpleFile(Gdx.files.internal(DIRECTORY + "index"))) {
                file = Gdx.files.internal(DIRECTORY + s);

                if (file == null) {
                    Gdx.app.error(TAG, "Could not find file for: " + s);
                } else {
                    loadFile(file);
                }
            }

            // Set to system language by default TODO make this read the configuration instead
            set(Locale.getDefault());

        } catch (Exception ex) {
            Gdx.app.error(TAG, "Error reading default languages", ex);
        }
    }

    /**
     * Loads language files from a {@link FileHandle}.
     * This allows loading languages outside of the internal file system.
     * <p>
     * Valid language files have a '.lang' extension.
     *
     * @param files The {@link FileHandle} of the directory
     * @return True if successful, otherwise false
     */
    public static boolean load(FileHandle files) {
        if (!files.isDirectory()) return loadFile(files);

        boolean outcome = true;

        for (FileHandle file : files.list()) {
            if (!loadFile(file)) outcome = false;
        }

        return outcome;
    }

    /**
     * Sets the current language.
     *
     * @param locale The language to set (e.g. 'en').
     */
    public static void set(Locale locale) {
        language = locale;
        last = LANGUAGES.get(language);
    }

    /**
     * Returns a language string by property key.
     *
     * @param property The property key
     * @return The language string
     */
    public static String get(String property, Object... args) {
        // We are keeping a reference to the current language
        // to reduce lookup time (searching the map).
        if (last == null) {
            last = LANGUAGES.get(language);
        }

        MessageFormat format;
        if (last != null) {
            format = last.getFormat(property);
            if (format != null) return format.format(args);
        }

        final LanguageEntry fallback = LANGUAGES.get(FALLBACK);
        if (fallback != null) {
            format = fallback.getFormat(property);
            if (format != null) return format.format(args);
        }

        return "{" + property + "}";
    }

    /**
     * Loads a language from a {@link FileHandle}
     *
     * @param file The {@link FileHandle} of the language file to load
     * @return True on success, otherwise false
     */
    public static boolean loadFile(FileHandle file) {
        if (file == null) {
            return false;
        }

        if (!file.isDirectory() && file.extension().equalsIgnoreCase("lang")) {
            try {
                Locale locale = new Locale(file.nameWithoutExtension());
                LanguageEntry lang = LANGUAGES.get(locale);

                if (lang == null) {
                    lang = new LanguageEntry(locale);
                    LANGUAGES.put(locale, lang);
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


    private static class LanguageEntry {
        private final Properties strings;
        private final Map<String, MessageFormat> formatCache;
        private final Locale locale;

        private LanguageEntry(Locale locale) {
            this.locale = locale;

            strings = new Properties();
            formatCache = new THashMap<>();
        }

        private void addProperties(FileHandle file) throws IOException {
            strings.load(file.read());
        }

        private MessageFormat getFormat(String key) {
            MessageFormat format = formatCache.get(key);

            if (format == null) {
                final String content = strings.getProperty(key);
                if (content == null) {
                    return null;
                }

                format = new MessageFormat(content, locale);
                formatCache.put(key, format);
            }

            return format;
        }

    }

}
