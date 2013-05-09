package net.catacombsnatch.game.core.world.tile.tiles;

import net.catacombsnatch.game.core.entity.components.Physics;
import net.catacombsnatch.game.core.resource.Art;
import net.catacombsnatch.game.core.world.level.Level;
import net.catacombsnatch.game.core.world.tile.StaticTile;

import com.badlogic.gdx.graphics.Color;

public class HoleTile extends StaticTile {
	
	public HoleTile() {
		super(Color.BLACK);
	}

	@Override
	public void init(Level level, int x, int y) {
		super.init(level, x, y);
		
		setRandomTexture(Art.tiles_hole);
	}
	
	@Override
	public void update() {}

	@Override
	public boolean canPass(long entity) {
		Physics p = level.getEntityManager().getComponent(entity, Physics.class);
		if(p != null && p.isWithin(getBounds())) {
			// TODO Kill entity by "falling"
		}
		
		return true;
	}

}
