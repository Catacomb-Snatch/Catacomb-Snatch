package net.catacombsnatch.game.core.world.level;

import net.catacombsnatch.game.core.entity.EntityManager;
import net.catacombsnatch.game.core.screen.Screen;

public class Level {
	protected EntityManager entityManager;

	public Level() {
		entityManager = new EntityManager();
	}

	public void tick() {
	}

	public void render( Screen screen ) {
	}
}
