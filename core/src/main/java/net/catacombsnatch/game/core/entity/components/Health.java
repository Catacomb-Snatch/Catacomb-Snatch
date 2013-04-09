package net.catacombsnatch.game.core.entity.components;

import net.catacombsnatch.game.core.entity.EntityComponent;
import net.catacombsnatch.game.core.screen.Tickable;

public class Health implements EntityComponent, Tickable {
	protected int tick = 0;
	protected boolean regenerate = false;
	protected int speed = 60;
	
	public int health;
	public int maxHealth;

	@Override
	public void tick(float delta) {
		if(regenerate) {
			if(tick < speed) tick++;
			
			health++;
			tick = 0;
		}
	}
	
	public void regenerate(boolean regen) {
		regenerate = regen;
	}
	
	public void setRegenerationSpeed(int ticks) {
		speed = ticks;
	}
	
	public void heal( int hp ) {
		health += hp;
		if ( health > maxHealth ) health = maxHealth;
	}

	public void damage( int dmg ) {
		health -= dmg;
		if ( health < 0 ) health = 0;
	}

	public boolean isAlive() {
		return health > 0;
	}

	public float getHealthPercentage() {
		return (float) (health) / (float) (maxHealth);
	}

}
