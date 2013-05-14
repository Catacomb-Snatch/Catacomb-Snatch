package net.catacombsnatch.game.core.entity.components;

import com.artemis.Component;

public class Health extends Component {
	protected boolean regenerate;
	
	protected int speed;
	protected int amount;
	protected int health;
	protected int maxHealth;

	public int tick;

	public Health() {
		regenerate = true;

		speed = 60;
		tick = 0;
		
		amount = 1;
		health = 20;
		maxHealth = 20;
	}

	public boolean canRegenerate() {
		return regenerate;
	}
	
	public void setRegenerate(boolean regen) {
		regenerate = regen;
	}
	
	public int getRegenerationSpeed() {
		return speed;
	}

	public void setRegenerationSpeed(int ticks) {
		speed = ticks;
	}
	
	public int getHealAmount() {
		return amount;
	}
	
	public void setHealAmount(int amnt) {
		amount = amnt;
	}
	
	public void heal(int hp) {
		health += hp;
		
		if (health > maxHealth) health = maxHealth;
	}

	public void damage(int dmg) {
		health -= dmg;
		
		if (health < 0) health = 0;
	}

	public boolean isAlive() {
		return health > 0;
	}

	public float getHealthPercentage() {
		return (float) (health) / (float) (maxHealth);
	}

}
