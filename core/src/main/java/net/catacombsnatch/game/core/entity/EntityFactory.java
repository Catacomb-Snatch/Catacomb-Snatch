package net.catacombsnatch.game.core.entity;

import net.catacombsnatch.game.core.entity.components.Animations;
import net.catacombsnatch.game.core.entity.components.Health;
import net.catacombsnatch.game.core.entity.components.Position;
import net.catacombsnatch.game.core.entity.components.Sprite;
import net.catacombsnatch.game.core.entity.components.Velocity;
import net.catacombsnatch.game.core.resource.Art;
import net.catacombsnatch.game.core.world.level.Level;

import com.artemis.Entity;

public final class EntityFactory {

	public static Entity createPlayerEntity(Level level) {
		Entity player = level.createEntity();
		EntityHelper.addToGroup(player, "players");
		
		player.addComponent(new Health(20));
		player.addComponent(new Position(level.getNextSpawnLocation())); // TODO: This can return null!
		player.addComponent(new Velocity());
		player.addComponent(new Animations(Art.lordLard));
		
		player.addToWorld();
		
		return player;
	}
	
	public static Entity createTileEntity(Level level, int x, int y) {
		Entity tile = level.createEntity();
		EntityHelper.addToGroup(tile, "tiles");
		
		tile.addComponent(new Position(x, y));
		tile.addComponent(new Sprite(Art.tiles_floor[0])); //TODO choose the correct artwork for the tile or have it be set after the fact
		
		tile.addToWorld();
		
		return tile;
	}
}