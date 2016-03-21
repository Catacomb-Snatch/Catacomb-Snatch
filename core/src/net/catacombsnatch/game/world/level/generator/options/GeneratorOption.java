package net.catacombsnatch.game.world.level.generator.options;

public abstract class GeneratorOption<T> {
    public final String name;
    public T value;


    public GeneratorOption(String name) {
        this(name, null);
    }

    public GeneratorOption(String name, T defaultValue) {
        this.name = name;
        this.value = defaultValue;
    }

    public abstract T parseOption(String option);

}
