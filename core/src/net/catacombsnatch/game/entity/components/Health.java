package net.catacombsnatch.game.entity.components;

import com.badlogic.ashley.core.Component;

public class Health implements Component {
    public boolean canHeal;
    public int healSpeed;
    public int healTick;
    public int healAmount;
    public int health;
    public int maxHealth;

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

    public Health reset(int health) {
        maxHealth = health;
        health = maxHealth;
        healSpeed = 60;
        healTick = 0;
        healAmount = 1;
        canHeal = true;

        return this;
    }

}
