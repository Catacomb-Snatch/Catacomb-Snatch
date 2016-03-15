package net.catacombsnatch.game.resource.options;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

public abstract class Options extends OptionGroup {
    protected Array<String> changedList;

    public Options(String name) {
        this(name, null);
    }

    public Options(String name, OptionGroup parent) {
        this(name, parent, null);
    }

    public Options(String name, OptionGroup parent, ObjectMap<String, Object> defaults) {
        super(null, name, parent, defaults);

        options = this;
    }

    protected void addChange(String key) {
        if(changedList == null) changedList = new Array<String>();
        if(!changedList.contains(key, false)) changedList.add(key);
    }

    public abstract void save();

    public abstract void reload();

}
