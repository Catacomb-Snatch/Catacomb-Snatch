package net.catacombsnatch.game.core.world.tile.tiles;

import net.catacombsnatch.game.core.entity.Entity;
import net.catacombsnatch.game.core.resource.Art;
import net.catacombsnatch.game.core.world.level.Level;
import net.catacombsnatch.game.core.world.level.View;
import net.catacombsnatch.game.core.world.tile.StaticTile;
import net.catacombsnatch.game.core.world.tile.Tile;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

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
	public void render(SpriteBatch graphics, View view) {
		super.render(graphics, view);
		
		if(overlay != null) renderSprite(overlay, graphics, view);
	}

	@Override
	public void update() {
		byte mask = 0x0;
		
		for(Side side : Side.values()) {
			if(side.isEdge()) continue;
			
			Tile tile = getRelative(side);
			if(tile == null) continue;
			
			if(tile instanceof SandTile || tile instanceof WallTile || tile instanceof DestroyableWallTile) {
				mask += side.getMask();
			}
		}
		
		if(mask > 0) {
			if(overlay == null) overlay = new Sprite(sprite);
			
			overlay.setRegion(Art.tiles_shadows[mask - 1]);
		} else {
			overlay = null;
		}
	}

}
