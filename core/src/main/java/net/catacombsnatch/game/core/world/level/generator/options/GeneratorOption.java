package net.catacombsnatch.game.core.world.level.generator.options;

public abstract class GeneratorOption<T> {
	protected final String name;
	protected T value;
	
	public GeneratorOption(String name) {
		this(name, null);
	}
	
	public GeneratorOption(String name, T defaultValue) {
		this.name = name;
		this.value = defaultValue;
	}
	
	public String getName() {
		return name;
	}
	
	public void setValue(T value) {
		this.value = value;
	}
	
	public T getValue() {
		return value;
	}
	
	public abstract T parseOption(String option);
	
}
