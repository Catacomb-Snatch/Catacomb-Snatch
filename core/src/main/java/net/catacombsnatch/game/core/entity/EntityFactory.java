package net.catacombsnatch.game.core.entity;

import net.catacombsnatch.game.core.entity.components.Health;
import net.catacombsnatch.game.core.entity.components.Render;
import net.catacombsnatch.game.core.entity.components.Transform;
import net.catacombsnatch.game.core.entity.components.Velocity;
import net.catacombsnatch.game.core.entity.renderers.PlayerRenderer;
import net.catacombsnatch.game.core.world.level.Level;

import com.artemis.Entity;

public class EntityFactory {

	public static Entity createPlayerEntity(Level level) {
		Entity player = level.createEntity();
		EntityHelper.addToGroup(player, "players");
		
		player.addComponent(new Health(20));
		player.addComponent(new Transform(level.getNextSpawnLocation())); // TODO: This can return null!
		player.addComponent(new Velocity());
		player.addComponent(new Render(new PlayerRenderer(level, player)));
		
		player.addToWorld();
		
		return player;
	}
	
}
