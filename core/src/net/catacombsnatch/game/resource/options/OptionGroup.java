package net.catacombsnatch.game.resource.options;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

/**
 * Represents an option section
 */
public class OptionGroup {
    protected Options options;

    protected OptionGroup parent;
    protected String name;

    protected ObjectMap<String, Object> map;


    protected OptionGroup(Options options, String name) {
        this(options, name, null);
    }

    protected OptionGroup(Options options, String name, OptionGroup parent) {
        this(options, name, parent, null);
    }

    protected OptionGroup(Options options, String name, OptionGroup parent, ObjectMap<String, Object> defaults) {
        this.options = options;
        this.name = name;
        this.parent = parent;

        map = defaults == null ? new ObjectMap<String, Object>() : defaults;
    }

	
	/* ------------ Grouping related functions ------------ */

    public OptionGroup getGroup(String name) {
        OptionGroup current = this, group = null;

        String[] split = name.split(".");
        for (String s : split) {
            group = current.getGroup(s);
            if (group == null) return null;
        }

        return group;
    }

    public OptionGroup createGroup(String name) {
        return createGroup(name, new ObjectMap<String, Object>());
    }

    public OptionGroup createGroup(String name, ObjectMap<String, Object> defaults) {
        OptionGroup group = new OptionGroup(options, name, this, defaults);
        group.map.put(name, group);

        return group;
    }

    public OptionGroup getParent() {
        return parent;
    }

    /**
     * @return The topmost {@link OptionGroup}
     */
    public OptionGroup getRoot() {
        OptionGroup root = this;

        while (true) {
            if (root.getParent() == null) break;

            root = root.getParent();
        }

        return root;
    }

    /**
     * Gets the path from this current {@link OptionGroup} up to its root.
     * <p>
     * The groups are separated by a <code>'.'</code> character.
     * Example: <code>root.child.child</code>
     *
     * @return A string representation of the path
     */
    public String getCurrentPath() {
        String path = getName();

        OptionGroup og = this;
        if (og == getRoot()) return "";

        while (og != null) {
            og = og.getParent();

            if (og != null) path += ".";
            path += og.getName() != null ? og.getName() : "";
        }

        return path;
    }

    public String getPath(String key) {
        String path = getCurrentPath();
        return path.isEmpty() ? key : path + "." + key;
    }

    /**
     * Gets the name of this individual {@link OptionGroup}.
     * If this is a root element, this returns null.
     *
     * @return The group name
     */
    public String getName() {
        return name;
    }

	
	/* ------------ Value / Key related functions ------------ */

    public Array<String> getKeys() {
        return getKeys(true);
    }

    public Array<String> getKeys(boolean deep) {
        Array<String> keys = new Array<String>();

        for (String key : map.keys()) {
            keys.add(key);
        }

        if(deep) for(ObjectMap.Entry<String, Object> e: map.entries()) {
            if(!(e.value instanceof OptionGroup)) continue;

            keys.addAll(((OptionGroup) e.value).getKeys(true));
        }

        return keys;
    }

    public boolean isSet(String key) {
        KeyPair pair = getPair(key);
        return pair != null && pair.group.map.containsKey(pair.key);
    }

    public Object get(String key) {
        return get(key, Object.class);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String key, Class<T> type) {
        KeyPair pair = getPair(key);

        if (pair != null) {
            Object obj = pair.group.map.get(pair.key);
            if (obj != null) return (T) obj;
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String key, T def) {
        return (T) get(key, def.getClass());
    }

    public boolean set(String key, Object value) {
        KeyPair pair = getPair(key);
        if (pair == null) return false;

        pair.group.map.put(pair.key, value);
        options.addChange(getPath(key));

        return true;
    }

    public boolean setDefault(String key, Object value) {
        KeyPair pair = getPair(key);
        if (pair == null) return false;

        if (!pair.group.map.containsKey(pair.key)) {
            pair.group.map.put(pair.key, value);
        }

        return true;
    }

	
	/* ------------ Helper functions ------------ */

    protected KeyPair getPair(String key) {
        OptionGroup group = this;
        String[] split = key.split(".");
        String str = key;

        if (split.length > 1) for (int s = 0; s < split.length - 1; s++) {
            group = group.getGroup(split[s]);
            if (group == null) return null;
        }

        return new KeyPair(group, str);
    }

    protected class KeyPair {
        public final OptionGroup group;
        public final String key;

        public KeyPair(OptionGroup g, String k) {
            group = g;
            key = k;
        }
    }

}
