package net.catacombsnatch.game.core.world.level.generator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import net.catacombsnatch.game.core.world.level.Level;
import net.catacombsnatch.game.core.world.level.generator.options.GeneratorOption;

public abstract class LevelGenerator {
	protected Random random;
	protected List<GeneratorOption<?>> options;
	
	
	public LevelGenerator() {
		this(new Random());
	}
	
	public LevelGenerator(Random r) {
		random = r;
		
		options = new ArrayList<GeneratorOption<?>>();
	}
	
	
	public abstract Level generate();
	
	
	/** @return The random number generator for this level. */
	public Random randomizer() {
		return random;
	}
	
	public Collection<String> getOptions() {
		List<String> keys = new ArrayList<String>();
		
		for(GeneratorOption<?> option : options) {
			keys.add(option.getName());
		}
		
		return Collections.unmodifiableCollection(keys);
	}
	
	public GeneratorOption<?> getOption(String name) {
		for(GeneratorOption<?> option : options) {
			if(!option.getName().equalsIgnoreCase(name)) continue;
			
			return option;
		}
		
		return null;
	}
	
}
