package net.catacombsnatch.game.player;

import net.catacombsnatch.game.world.level.Level;

public interface Player {
	
	public LevelPlayer getLevelPlayer();
	
	public void prepareLevelPlayer(Level level);
	
}
