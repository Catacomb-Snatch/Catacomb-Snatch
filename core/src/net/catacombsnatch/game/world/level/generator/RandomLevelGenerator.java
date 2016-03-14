package net.catacombsnatch.game.world.level.generator;

import java.util.ArrayList;
import java.util.HashMap;

import net.catacombsnatch.game.world.Campaign;
import net.catacombsnatch.game.world.Direction;
import net.catacombsnatch.game.world.level.Level;
import net.catacombsnatch.game.world.level.generator.RandomLevelGenerator.Cell.Type;
import net.catacombsnatch.game.world.tile.Tile;
import net.catacombsnatch.game.world.tile.tiles.DestroyableWallTile;
import net.catacombsnatch.game.world.tile.tiles.FloorTile;
import net.catacombsnatch.game.world.tile.tiles.WallTile;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class RandomLevelGenerator extends LevelGenerator {
	
	public static class Cell {
		public static enum Type {
			BORDER, FLOOR, INNER;
		}
		
		public int x;
		public int y;
		public int width;
		public int height;
		public Type type;
		public ArrayList<Direction> connected = new ArrayList<Direction>();
		
		public Cell(int x, int y, int width, int height, Type type) {
			this.x = x;
			this.y = y;
			this.width = width;
			this.height = height;
			this.type = type;
		}
		
		public void generate(Level level, HashMap<Integer, Cell> cellMap) {
			for (int xx = x*width; xx < x*width+width; xx++) {
				for (int yy = y*height; yy < y*height+height; yy++) {
					Tile tile = level.getTile(xx, yy);
					if (tile == null) {
						//Fill gap if empty
						switch (type) {
						case BORDER:
							if (y == (level.getHeight()-1)/height-1) {
								if (yy == y*height) {
									tile = new WallTile();
								}
							}
							break;
						case FLOOR:
							if (y == 0 || y == (level.getHeight()-1)/height-1) {
								boolean south = true;
								if (y == 0) {
									south = false;
								}
								boolean strip = false;
								if ((!south && yy == y*height) || (south && yy == y*height+height-1)) {
									strip = true;
								}
								//Remove useless leftmost stripe
								Cell leftBottomCell = cellMap.get((x-1) + (y+1)*(level.getWidth()-1)/width);
								if (x-1 < 0) {
									leftBottomCell = null;
								}
								if (leftBottomCell != null && leftBottomCell.type == Type.INNER) {
									if (xx == x*width) {
										strip = true;
									}
								}
								Cell leftTopCell = cellMap.get((x-1) + (y-1)*(level.getWidth()-1)/width);
								if (x-1 < 0) {
									leftTopCell = null;
								}
								if (leftTopCell != null && leftTopCell.type == Type.INNER) {
									if (xx == x*width) {
										strip = true;
									}
								}
								if (!strip) {
									tile = new FloorTile();
								}
							} else {
								tile = new FloorTile();
							}
							break;
						case INNER:
							boolean isDoor = false;
							boolean isSemiDoor = false;
							for (int i = 0; i <= 1; i++) {
								if (xx == x*width+width/2+i || yy == y*width+width/2+i) {
									isDoor = true;
									isSemiDoor = true;
								}
								if (isDoor && yy == y*height && y == 1) {
									isDoor = false;
								}
								if (isSemiDoor && yy == y*height && y == (level.getHeight()-1)/height-2) {
									isSemiDoor = false;
								}
								if (isDoor && xx == x*width && x == 0) {
									isDoor = false;
								}
							}
							if (xx == x*width || yy == y*height) {
								if (isDoor) {
									tile = new DestroyableWallTile();
								} else {
									tile = new WallTile();
								}
							} else {
								tile = new FloorTile();
							}
							boolean genWallBreak = false;
							boolean genWallRight = false;
							boolean genWallBottom = false;
							boolean genWallBottomRight = false;
							//Generate additional walls on the right
							if (x == (level.getWidth()-1)/width-1) {
								genWallRight = true;
							}
							Cell rightCell = cellMap.get((x+1) + y*(level.getWidth()-1)/width);
							if (x+1 >= (level.getWidth()-1)/width) {
								rightCell = null;
							}
							if (rightCell != null && rightCell.type == Type.FLOOR) {
								genWallRight = true;
								genWallBreak = isSemiDoor;
							}
							if (genWallRight) {
								genWallRight = xx == x*width;
							}
							if (genWallRight) {
								if (genWallBreak) {
									genTileAt(level, new DestroyableWallTile(), xx+width, yy);
								} else {
									genTileAt(level, new WallTile(), xx+width, yy);
								}
							}
							//Generating additonal walls at the bottom and bottom-right corner
							Cell bottomCell = cellMap.get(x + (y+1)*(level.getWidth()-1)/width);
							if (bottomCell != null && bottomCell.type == Type.FLOOR) {
								genWallBottom = true;
								genWallBreak = isSemiDoor;
							}
							if (bottomCell != null && bottomCell.type == Type.BORDER) {
								genWallBottomRight = true;
							}
							if (genWallBottom) {
								genWallBottom = yy == y*height;
							}
							if (genWallBottomRight) {
								genWallBottomRight = xx == x*width && yy == y*height;
							}
							if (genWallBottom) {
								if (genWallBreak) {
									genTileAt(level, new DestroyableWallTile(), xx, yy+height);
								} else {
									genTileAt(level, new WallTile(), xx, yy+height);
								}
							}
							if (genWallBottomRight) {
								genTileAt(level, new WallTile(), xx+width, yy+height);
							}
							break;
						}
					}
					if (tile != null) {
						tile.init(level, xx, yy);
						level.setTile(tile, xx, yy);
					}
				}
			}
		}

		protected void genTileAt(Level level, Tile tile, int xx, int yy) {
			if (tile != null) {
				tile.init(level, xx, yy);
				level.setTile(tile, xx, yy);
			}
		}
	}
	
	public int segwidth = 7;
	public int segheight = 7;
	public int width = segwidth*5;
	public int height = segheight*7;
	public HashMap<Integer, Cell> cellMap;
	
	@Override
	public Level generate(Campaign campaign) {
		/*
		 * Some thoughts on generation:
		 * 
		 * - Divide up the level into a grid of cells (size is customizable).
		 * - Pick a center point in each cell.
		 * - Join each pair in the list of center points with corridors.
		 * - Build a room around each center point.
		 * - Fill each room with floor tiles and put wall tiles around it (maybe add some decoration?).
		 * - Where a room wall crosses a corridor replace with a destroyable wall tile.
		 * 
		 * This ensures the entire level will be connected.
		 */
		
		// TODO
		
		Level level = new Level(campaign, this, width+1, height+1);
		
		if (cellMap == null) {
			cellMap = new HashMap<Integer, Cell>();
		} else {
			cellMap.clear();
		}
		
		//Place cells with their type dependent of position
		for (int cx = 0; cx < width/segwidth; cx++) {
			for (int cy = 0; cy < height/segheight; cy++) {
				Type type = Type.INNER;
				
				if (cy == 0 || cy == 1 || cy == height/segheight - 2 || cy == height/segheight-1) {
					boolean isEntrance = false;
					for (int i = -1; i <= 1; i++) {
						if (cx == (width/segwidth)/2+i || cy == (height/segheight)/2+i) {
							isEntrance = true;
						}
					}
					if (cy == 0 || cy == (height/segheight)-1) {
						if (isEntrance) {
							type = Type.FLOOR;
						} else {
							type = Type.BORDER;
						}
					} else {
						if (isEntrance) {
							type = Type.FLOOR;
						}
					}
				}
				
				Cell cell = new Cell(cx, cy, segwidth, segheight, type);
				cellMap.put(cx + cy * width/segwidth, cell);
			}
		}
		
		//Pre-generate special areas, 
		//for example borders of inner map or
		//the loot in the middle of the map
		//TODO
		
		//Generate cells 
		for (Cell cell : cellMap.values()) {
			if (cell == null) {
				continue;
			}
			cell.generate(level, cellMap);
		}
		
		return level;
	}

	@Override
	public Array<Vector2> getSpawnLocations() {
		return new Array<Vector2>(new Vector2[] { new Vector2(0, 0) }); // TODO: @AngelDE!
	}

}
