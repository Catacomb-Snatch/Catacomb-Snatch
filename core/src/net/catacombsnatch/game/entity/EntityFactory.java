package net.catacombsnatch.game.entity;

import net.catacombsnatch.game.entity.components.Health;
import net.catacombsnatch.game.entity.components.Position;
import net.catacombsnatch.game.entity.components.Render;
import net.catacombsnatch.game.entity.components.Velocity;
import net.catacombsnatch.game.entity.renderers.PlayerRenderer;
import net.catacombsnatch.game.entity.renderers.TileRenderer;
import net.catacombsnatch.game.world.level.Level;

import com.artemis.Entity;

public final class EntityFactory {

	public static Entity createPlayerEntity(Level level) {
		Entity player = level.createEntity();
		EntityHelper.addToGroup(player, "players");
		
		player.edit().add(new Health(20));
		player.edit().add(new Position(level.getNextSpawnLocation())); // TODO: This can return null!
		player.edit().add(new Velocity());
		player.edit().add(new Render(new PlayerRenderer(player)));

		return player;
	}
	
	public static Entity createTileEntity(Level level, int x, int y) {
		Entity tile = level.createEntity();
		EntityHelper.addToGroup(tile, "tiles");
		
		tile.edit().add(new Position(x, y));
		tile.edit().add(new Render(new TileRenderer(tile)));
		
		return tile;
	}
	
}
