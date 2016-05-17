package edu.ub.pis2016.pis16.strikecom.engine.game;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;

import edu.ub.pis2016.pis16.strikecom.engine.math.MathUtils;
import edu.ub.pis2016.pis16.strikecom.engine.math.Vector2;
import edu.ub.pis2016.pis16.strikecom.engine.opengl.AnimatedSprite;
import edu.ub.pis2016.pis16.strikecom.engine.opengl.SpriteBatch;
import edu.ub.pis2016.pis16.strikecom.engine.opengl.Sprite;
import edu.ub.pis2016.pis16.strikecom.engine.opengl.Texture;
import edu.ub.pis2016.pis16.strikecom.engine.opengl.TextureRegion;
import edu.ub.pis2016.pis16.strikecom.engine.physics.Physics2D;
import edu.ub.pis2016.pis16.strikecom.engine.util.Assets;
import edu.ub.pis2016.pis16.strikecom.engine.util.Perlin2D;

/**
 * Created by Alexander Bevzenko on 10/05/16.
 * supposed to contain everything related to map
 */
public class GameMap {

	private Perlin2D perlin;
	private float[][] pTable;
	private boolean[][] discoveredTable; // to show discovered terrain on minimap
	private Sprite[][] tTable;

	private ArrayList<AnimatedSprite> animatedTiles = new ArrayList<>();

	private Physics2D physics2D;
	private int drawDistance;
	private int tileSize;
	private int width, height;

	Sprite grass;
	Sprite dry;
	Sprite[] sand;
	Sprite water;

	Sprite[] gray;

	HashMap<String, Sprite> dryToGrass = new HashMap<>();
	HashMap<String, Sprite> sandToDry = new HashMap<>();

	ArrayList<Sprite> allSprites = new ArrayList<>();

	/**
	 * This generates map of desired size and adds borders to physics engine
	 *
	 * @param physics2D   - link to game's physicsphysics
	 * @param tileSize    - size of tiles
	 * @param seed        - seed to genetare perlin noise
	 * @param squareSize  - size of a region where interpolation goes
	 * @param octaves     - octaves of perlin noise
	 * @param persistence - noise persistence
	 */
	public GameMap(Physics2D physics2D, int tileSize, long seed, int squareSize, int octaves, float persistence) {
		perlin = new Perlin2D(seed);
		this.tileSize = tileSize;
		this.physics2D = physics2D;

		width = physics2D.getWorldWidth();
		height = physics2D.getWorldHeight();

		pTable = new float[width][height];
		discoveredTable = new boolean[width][height]; // minimap purposes
		tTable = new Sprite[width][height];
		pTable = perlin.perlinMap(width, height, squareSize, octaves, persistence);

		gray = new Sprite[8];
		for (int i = 0; i < gray.length; i++) {
			gray[i] = new Sprite(Assets.SPRITE_ATLAS.getRegion("gray", i));
			allSprites.add(gray[i]);
		}

		grass = new Sprite(Assets.SPRITE_ATLAS.getRegion("grass", 0));
		allSprites.add(grass);

		dry = new Sprite(Assets.SPRITE_ATLAS.getRegion("dry", 0));
		allSprites.add(dry);

		water = new AnimatedSprite(Assets.SPRITE_ATLAS.getRegions("water"), 0.60f);
		allSprites.add(water);

		sand = new Sprite[2];
		sand[0] = new Sprite(Assets.SPRITE_ATLAS.getRegion("sand", 0));
		sand[1] = new Sprite(Assets.SPRITE_ATLAS.getRegion("sand", 1));
		allSprites.add(sand[0]);
		allSprites.add(sand[1]);

		dryToGrass.put("dry_grass_nw", new Sprite(Assets.SPRITE_ATLAS.getRegion("dry_grass_nw")));
		dryToGrass.put("dry_grass_n", new Sprite(Assets.SPRITE_ATLAS.getRegion("dry_grass_n")));
		dryToGrass.put("dry_grass_ne", new Sprite(Assets.SPRITE_ATLAS.getRegion("dry_grass_ne")));
		dryToGrass.put("dry_grass_e", new Sprite(Assets.SPRITE_ATLAS.getRegion("dry_grass_e")));
		dryToGrass.put("dry_grass_se", new Sprite(Assets.SPRITE_ATLAS.getRegion("dry_grass_se")));
		dryToGrass.put("dry_grass_s", new Sprite(Assets.SPRITE_ATLAS.getRegion("dry_grass_s")));
		dryToGrass.put("dry_grass_sw", new Sprite(Assets.SPRITE_ATLAS.getRegion("dry_grass_sw")));
		dryToGrass.put("dry_grass_w", new Sprite(Assets.SPRITE_ATLAS.getRegion("dry_grass_w")));
		allSprites.addAll(dryToGrass.values());

//		sandToDry.put("sand_dry_nw", new Sprite(Assets.SPRITE_ATLAS.getRegion("sand_dry_nw")));
//		sandToDry.put("sand_dry_n", new Sprite(Assets.SPRITE_ATLAS.getRegion("sand_dry_n")));
//		sandToDry.put("sand_dry_ne", new Sprite(Assets.SPRITE_ATLAS.getRegion("sand_dry_ne")));
//		sandToDry.put("sand_dry_e", new Sprite(Assets.SPRITE_ATLAS.getRegion("sand_dry_e")));
//		sandToDry.put("sand_dry_se", new Sprite(Assets.SPRITE_ATLAS.getRegion("sand_dry_se")));
//		sandToDry.put("sand_dry_s", new Sprite(Assets.SPRITE_ATLAS.getRegion("sand_dry_s")));
//		sandToDry.put("sand_dry_sw", new Sprite(Assets.SPRITE_ATLAS.getRegion("sand_dry_sw")));
//		sandToDry.put("sand_dry_w", new Sprite(Assets.SPRITE_ATLAS.getRegion("sand_dry_w")));
//		allSprites.addAll(sandToDry.values());

		// Scale all sprites to same dimensions
		for (Sprite sp : allSprites) {
			sp.setSize(tileSize);
			if (sp instanceof AnimatedSprite)
				animatedTiles.add((AnimatedSprite) sp);
//			float texSize = tp.getRegion().width;
//			tp.setScale(1.1f * (tileSize / texSize));
		}

		// Gen map
		int row, col;
		for (row = 0; row < height; row++) {
			for (col = 0; col < width; col++) {
				if (row == 0 || row == height - 1 || col == 0 || col == width - 1) {
					tTable[row][col] = gray[7];
					continue;
				}

				tTable[row][col] = getTile(pTable[row][col]);

				Sprite center = getTile(pTable[row][col]);

				Sprite east = getTile(pTable[row][col + 1]);
				Sprite west = getTile(pTable[row][col - 1]);
				Sprite north = getTile(pTable[row + 1][col]);
				Sprite south = getTile(pTable[row - 1][col]);

				if (center == dry) {
					if (north == grass)
						if (east == grass)
							tTable[row][col] = dryToGrass.get("dry_grass_ne");
						else if (west == grass)
							tTable[row][col] = dryToGrass.get("dry_grass_nw");
						else
							tTable[row][col] = dryToGrass.get("dry_grass_n");
					else if (south == grass)
						if (east == grass)
							tTable[row][col] = dryToGrass.get("dry_grass_se");
						else if (west == grass)
							tTable[row][col] = dryToGrass.get("dry_grass_sw");
						else
							tTable[row][col] = dryToGrass.get("dry_grass_s");
					else if (east == grass)
						if (north == grass)
							tTable[row][col] = dryToGrass.get("dry_grass_ne");
						else if (south == grass)
							tTable[row][col] = dryToGrass.get("dry_grass_se");
						else
							tTable[row][col] = dryToGrass.get("dry_grass_e");
					else if (west == grass)
						if (north == grass)
							tTable[row][col] = dryToGrass.get("dry_grass_nw");
						else if (south == grass)
							tTable[row][col] = dryToGrass.get("dry_grass_sw");
						else
							tTable[row][col] = dryToGrass.get("dry_grass_w");
				}
			}
		}
	}

	/** Set the draw distance IN TILES from the center to the edge of a square */
	public void setDrawDistance(int drawDistance) {
		this.drawDistance = drawDistance;
	}

	private Sprite getTile(float value) {
//		if (true)
//			return gray[(int) (MathUtils.lerp(0, 8, value))];
		if (value > 0.6f)
			return grass;
		else if (value > 0.5f)
			return dry;
		else if (value > 0.4f)
			return sand[MathUtils.random(0, 1)];
		else
			return water;
	}

	public float[][] getPTable() {
		return this.pTable;
	}

	/** Update all animated tiles */
	public void update(float delta) {
		for (AnimatedSprite aSpr : animatedTiles)
			aSpr.update(delta);
	}

	/** Draw the map around the given position, by default 8 tiles in each direction */
	public void draw(SpriteBatch batch, Vector2 center) {
		int row, col;
		// Change this to increase view distance
		int squareRad = drawDistance * tileSize;

		// Test all map positions to draw a tile
		// TODO optimize
		for (row = 0; row < height; row++) {
			for (col = 0; col < width; col++) {

				// Calculate x,y coordinates
				float x = tileSize / 2f + col * tileSize;
				float y = tileSize / 2f + row * tileSize;

				if (x < center.x - squareRad || x > center.x + squareRad || y < center.y - squareRad || y > center.y + squareRad)
					continue;

				discoveredTable[row][col] = true; // mark this point as visited
				tTable[row][col].draw(batch, x, y);
			}
		}


	}

	/**
	 * for minimap rendering purposes
	 * WARNING resized sptites will continue be resized even after
	 *
	 * @param value perlin noise value
	 * @return
	 */
	private Sprite getGray(float value) {
		if (value > 0.5f)
			return gray[5]; //equvalent of grass
		else if (value > 0.4f)
			return gray[3]; // equivalent of dry
		else
			return gray[0]; // equivalent of sand
	}
	//TODO add sprites for minimap (player pointer, shops, tiles - to avoid resize operations)

	/**
	 * draws minimap on GL screen centered on desired position - currently 2x2 pixels for  a tile, using gray tiles
	 * WARNING this will give batch buffer overflow IF map is quite big
	 *
	 * @param batch  SpriteBatch
	 * @param center position where map will be drawn
	 */
	public void drawMiniMap(SpriteBatch batch, Vector2 center) {
		float tmpY;
		float tmpX;
		Sprite tmp;

		for (int y = 0; y < height; y++) {
			tmpY =  tileSize / 2f+(center.y - height*2 + y *4);
			for (int x = 0; x < width; x++) {
				tmpX = tileSize / 2f+(center.x - width*2 + x *4);
				if (discoveredTable[y][x]) {    //discovered terrain
					tmp = getGray(pTable[y][x]);
					tmp.setScale(0.5f); //warning: resize works for other instances of this sprites
					tmp.draw(batch, tmpX, tmpY);
					if (x == (int) (center.x) / tileSize && y == (int) (center.y) / tileSize) {//player position marker
						tmp = gray[7];
						tmp.setScale(0.6f);
						tmp.draw(batch, tmpX, tmpY);
						tmp.setScale(2.1f);
					}

				} else {                        //undiscovered terrain
					tmp = gray[6];
					tmp.setScale(0.25f); //warning: resize works for other instances of this sprites
					tmp.draw(batch, tmpX, tmpY);
				}


			}
		}
	}

	public void createMiniMap(Vector2 center){
		Color color=new Color();
		Bitmap.Config conf = Bitmap.Config.ARGB_8888; // see other conf types
		Bitmap bmp = Bitmap.createBitmap(width, height, conf); // this creates a MUTABLE bitmap
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (discoveredTable[y][x]) {
					bmp.setPixel(x,y,calcColor(pTable[y][x]));
					if (x == (int) (center.x) / tileSize && y == (int) (center.y) / tileSize) {//player position marker
						bmp.setPixel(x, y, color.BLACK);
					}
				}else {
					bmp.setPixel(x,y,color.WHITE);
				}
			}

		}

/*
		FileOutputStream out = null;
		try {
			out = new FileOutputStream();
			bmp.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
			// PNG is a lossless format, the compression factor (100) is ignored
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}*/


	}
	private int calcColor(float inValue){
		if (inValue > 0.6f)
			return  -16711936; //GREEN -grass
		else if (inValue > 0.5f)
			return -3355444 ;//LTGRAY-  dry
		else if (inValue > 0.4f)
			return  -256; //YELLOW - sand
		else
			return -16776961; //BLUE - water

	}

}//android drawables