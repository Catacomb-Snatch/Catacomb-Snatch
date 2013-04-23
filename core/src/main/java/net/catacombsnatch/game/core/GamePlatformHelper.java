package net.catacombsnatch.game.core;

public interface GamePlatformHelper {
	
	//Default: Do nothing. 
	//Android: Default
	//Desktop: Replace cursor
	//Android (OUYA): Replace cursor in future (if possible) 
	//BTW: (Android has got a cursor, too!)
	public void initCursor();
	
}
