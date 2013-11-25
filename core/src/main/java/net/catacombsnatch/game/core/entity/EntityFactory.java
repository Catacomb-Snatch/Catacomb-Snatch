package net.catacombsnatch.game.core.entity;

import net.catacombsnatch.game.core.entity.components.Health;
import net.catacombsnatch.game.core.entity.components.Position;
import net.catacombsnatch.game.core.entity.components.Render;
import net.catacombsnatch.game.core.entity.components.Velocity;
import net.catacombsnatch.game.core.entity.renderers.PlayerRenderer;
import net.catacombsnatch.game.core.entity.renderers.TileRenderer;
import net.catacombsnatch.game.core.world.level.Level;

import com.artemis.Entity;

public final class EntityFactory {

	public static Entity createPlayerEntity(Level level) {
		Entity player = level.createEntity();
		EntityHelper.addToGroup(player, "players");
		
		player.addComponent(new Health(20));
		player.addComponent(new Position(level.getNextSpawnLocation())); // TODO: This can return null!
		player.addComponent(new Velocity());
		player.addComponent(new Render(new PlayerRenderer(player)));
		
		player.addToWorld();
		
		return player;
	}
	
	public static Entity createTileEntity(Level level, int x, int y) {
		Entity tile = level.createEntity();
		EntityHelper.addToGroup(tile, "tiles");
		
		tile.addComponent(new Position(x, y));
		tile.addComponent(new Render(new TileRenderer(tile)));
		
		tile.addToWorld();
		
		return tile;
	}
	
}
