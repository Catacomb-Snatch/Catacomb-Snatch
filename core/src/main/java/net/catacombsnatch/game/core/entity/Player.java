package net.catacombsnatch.game.core.entity;

import java.util.UUID;

import net.catacombsnatch.game.core.Game;
import net.catacombsnatch.game.core.world.level.Level;

import com.artemis.Entity;

/**
 * Represents all human players connected either
 * locally or remotely with an in-game name, UUID
 * and the entity representing them locally.
 * 
 * @author Kyle Brodie
 */
public class Player {
	
	private UUID uuid;
	private String username;
	private Entity e;
	
	/** convenience boolean to test whether this player is local */
	//TODO implement how this gets set.
	public boolean local = true;
	
	/**
	 * Creates a new player with the provided
	 * username and generates a UUID
	 * 
	 * @param username
	 */
	/*
	 * TODO look-up username
	 * from our servers, or whatever platform
	 * we are using (e.g. Steam, Ouya) to have
	 * unique usernames. for now it is local
	 */
	public Player(String username) {
		this.username = username;
		
		if(Game.options.isSet(username)) {
			uuid = (UUID) Game.options.get(username);
		} else {
			uuid = UUID.randomUUID();
			Game.options.set(username, uuid);
		}
	}
	
	public void addToLevel(Level l) {
		e = EntityFactory.createPlayerEntity(l);
	}
	
	public String getUsername() {
		return username;
	}
	
	public UUID getUUID() {
		return uuid;
	}
	
	public Entity getEntity() {
		return e;
	}
}