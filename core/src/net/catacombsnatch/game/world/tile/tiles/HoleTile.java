package net.catacombsnatch.game.world.tile.tiles;

import net.catacombsnatch.game.resource.Art;
import net.catacombsnatch.game.world.Direction;
import net.catacombsnatch.game.world.level.Level;
import net.catacombsnatch.game.world.level.View;
import net.catacombsnatch.game.world.tile.StaticTile;
import net.catacombsnatch.game.world.tile.Tile;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

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
		// TODO Kill entity by "falling"
		
		return true;
	}

}
