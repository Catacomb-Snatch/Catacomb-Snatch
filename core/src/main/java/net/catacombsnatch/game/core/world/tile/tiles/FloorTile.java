package net.catacombsnatch.game.core.world.tile.tiles;

import net.catacombsnatch.game.core.entity.Entity;
import net.catacombsnatch.game.core.resource.Art;
import net.catacombsnatch.game.core.scene.Scene;
import net.catacombsnatch.game.core.world.level.Level;
import net.catacombsnatch.game.core.world.tile.StaticTile;
import net.catacombsnatch.game.core.world.tile.Tile;

import com.badlogic.gdx.graphics.g2d.Sprite;

public class FloorTile extends StaticTile {
	protected Sprite overlay;
	
	public FloorTile() {
		super(getColor(Art.tiles_floor[0]));
	}
	
	@Override
	public void init(Level level, int x, int y) {
		super.init(level, x, y);
		
		setRandomTexture(Art.tiles_floor);
	}

	@Override
	public boolean canPass(Entity entity) {
		return true;
	}
	
	@Override
	public void render(Scene scene) {
		super.render(scene);
		
		if(overlay != null) overlay.draw(scene.getSpriteBatch());
	}

	@Override
	public void update() {
		byte mask = 0x0;
		
		for(Side side : Side.values()) {
			if(side.isEdge()) continue; // TODO
			
			Tile tile = getRelative(side);
			if(tile == null) continue;
			
			if(tile instanceof WallTile || tile instanceof DestroyableWallTile) {
				mask += side.getWeight();
			}
		}
		
		if(mask > 0) {
			if(overlay == null) {
				overlay = new Sprite();
				sprite.setBounds(sprite.getX(), sprite.getY(), WIDTH, HEIGHT);
			}
			
			overlay.setRegion(Art.tiles_sand[mask]);
			
		} else {
			overlay = null;
		}
	}

}
