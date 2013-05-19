package net.catacombsnatch.game.core.player;

import net.catacombsnatch.game.core.world.level.Level;

public interface Player {
	
	public LevelPlayer getLevelPlayer();
	
	public void prepareLevelPlayer(Level level);
	
}
