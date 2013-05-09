package net.catacombsnatch.game.core.entity.components;

import net.catacombsnatch.game.core.entity.EntityComponent;
import net.catacombsnatch.game.core.screen.Tickable;

public class Health extends EntityComponent implements Tickable {
	protected int tick;
	protected boolean regenerate;
	protected int speed;
	
	public int health;
	public int maxHealth;

	public Health(long id) {
		super(id);
		
		tick = 0;
		regenerate = false;
		speed = 60;
	}
	
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
