package net.catacombsnatch.game.core.entity.components;

public class Health implements EntityComponent {
	private static final long serialVersionUID = 1L;

	public int health;
	public int maxHealth;

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
		return health / maxHealth;
	}
}
