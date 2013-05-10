package net.catacombsnatch.game.core.world.tile.tiles;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

import net.catacombsnatch.game.core.entity.components.Physics;
import net.catacombsnatch.game.core.resource.Art;
import net.catacombsnatch.game.core.world.Direction;
import net.catacombsnatch.game.core.world.level.Level;
import net.catacombsnatch.game.core.world.level.View;
import net.catacombsnatch.game.core.world.tile.StaticTile;
import net.catacombsnatch.game.core.world.tile.Tile;

public class HoleTile extends StaticTile {
	
	public HoleTile() {
		super(0xFF000000); // Black
	}

	@Override
	public void init(Level level, int x, int y) {
		this.level = level;
		setRandomTexture(Art.tiles_hole);
		
		super.init(level, x, y);
	}
	
	@Override
	public void update() {}
	
	protected Tile tmptile;
	
	@Override
	public void render(SpriteBatch graphics, View view) {
		Tile northtile = getRelative(Direction.NORTH);
		if (northtile != null && (northtile instanceof WallTile || northtile instanceof DestroyableWallTile)) {
			if (tmptile == null) {
				if (northtile instanceof WallTile) {
					tmptile = new FloorTile();
				}
				if (northtile instanceof DestroyableWallTile) {
					Class<? extends Tile> tileclass = ((DestroyableWallTile)northtile).destroy();
					if (tileclass != null) {
						try {
							tmptile = tileclass.newInstance();
						} catch (Exception e) {
						}
					}
				}
				if (tmptile != null) {
					tmptile.init(level, (int)bb.x/WIDTH, (int)bb.y/HEIGHT-1);
				}
			}
			northtile = tmptile;
		}
		if (northtile != null && !(northtile instanceof HoleTile)) {
			Rectangle origbb = new Rectangle(northtile.getBounds());
			northtile.getBounds().y+=HEIGHT;
			northtile.render(graphics, view);
			northtile.getBounds().set(origbb);
		}
		renderTile(graphics, view, region);
	}

	@Override
	public boolean canPass(long entity) {
		Physics p = level.getEntityManager().getComponent(entity, Physics.class);
		if(p != null && p.isWithin(getBounds())) {
			// TODO Kill entity by "falling"
		}
		
		return true;
	}

}
