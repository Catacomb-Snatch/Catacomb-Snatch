package net.catacombsnatch.game.resource.options;

import net.catacombsnatch.game.CatacombSnatch;

import com.badlogic.gdx.Gdx;

public enum DefaultOptions {
    DEBUG("debugMode", true),
    VSYNC("vSyncEnabled", true, new Runnable() {
        @Override
        public void run() {
            Gdx.graphics.setVSync((Boolean) DefaultOptions.VSYNC.get());
        }
    });

    private final String key;
    private final Object value;
    private final Runnable runnable;

    DefaultOptions(String key, Object value) {
        this(key, value, null);
    }

    DefaultOptions(String key, Object value, Runnable runnable) {
        this.key = key;
        this.value = value;
        this.runnable = runnable;
    }

    @SuppressWarnings("unchecked")
    public <T> T get() {
        return (T) CatacombSnatch.options.get(key, value);
    }

    public void set(Object val) {
        CatacombSnatch.options.set(key, val);

        if (runnable != null) runnable.run();
    }

    public static void setDefaults() {
        for (DefaultOptions option : values()) {
            CatacombSnatch.options.setDefault(option.key, option.value);
        }
    }

    public static DefaultOptions getOption(String key) {
        for (DefaultOptions option : values()) {
            if (option.key.equalsIgnoreCase(key)) return option;
        }

        return null;
    }
}
