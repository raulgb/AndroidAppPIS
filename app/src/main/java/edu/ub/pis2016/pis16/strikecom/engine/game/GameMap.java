package edu.ub.pis2016.pis16.strikecom.engine.game;

import java.util.ArrayList;
import java.util.HashMap;

import edu.ub.pis2016.pis16.strikecom.engine.math.MathUtils;
import edu.ub.pis2016.pis16.strikecom.engine.math.Vector2;
import edu.ub.pis2016.pis16.strikecom.engine.opengl.SpriteBatch;
import edu.ub.pis2016.pis16.strikecom.engine.opengl.TextureSprite;
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
	private TextureSprite[][] tTable;
	private Physics2D physics2D;
	private int tileSize;
	private int width, height;

	TextureSprite grass;
	TextureSprite dry;
	TextureSprite[] sand;
	TextureSprite[] gray;

	HashMap<String, TextureSprite> dryToGrass = new HashMap<>();

	ArrayList<TextureSprite> allSprites = new ArrayList<>();

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
		tTable = new TextureSprite[width][height];
		pTable = perlin.perlinMap(width, height, squareSize, octaves, persistence);

		gray = new TextureSprite[8];
		for (int i = 0; i < gray.length; i++) {
			gray[i] = new TextureSprite(Assets.SPRITE_ATLAS.getRegion("gray", i));
			allSprites.add(gray[i]);
		}

		grass = new TextureSprite(Assets.SPRITE_ATLAS.getRegion("grass", 0));
		allSprites.add(grass);

		dry = new TextureSprite(Assets.SPRITE_ATLAS.getRegion("dry", 0));
		allSprites.add(dry);

		sand = new TextureSprite[2];
		sand[0] = new TextureSprite(Assets.SPRITE_ATLAS.getRegion("sand", 0));
		sand[1] = new TextureSprite(Assets.SPRITE_ATLAS.getRegion("sand", 1));
		allSprites.add(sand[0]);
		allSprites.add(sand[1]);


		dryToGrass.put("dry_grass_nw", new TextureSprite(Assets.SPRITE_ATLAS.getRegion("dry_grass_nw")));
		dryToGrass.put("dry_grass_n", new TextureSprite(Assets.SPRITE_ATLAS.getRegion("dry_grass_n")));
		dryToGrass.put("dry_grass_ne", new TextureSprite(Assets.SPRITE_ATLAS.getRegion("dry_grass_ne")));
		dryToGrass.put("dry_grass_e", new TextureSprite(Assets.SPRITE_ATLAS.getRegion("dry_grass_e")));
		dryToGrass.put("dry_grass_se", new TextureSprite(Assets.SPRITE_ATLAS.getRegion("dry_grass_se")));
		dryToGrass.put("dry_grass_s", new TextureSprite(Assets.SPRITE_ATLAS.getRegion("dry_grass_s")));
		dryToGrass.put("dry_grass_sw", new TextureSprite(Assets.SPRITE_ATLAS.getRegion("dry_grass_sw")));
		dryToGrass.put("dry_grass_w", new TextureSprite(Assets.SPRITE_ATLAS.getRegion("dry_grass_w")));

		allSprites.addAll(dryToGrass.values());

		// Scale all sprites to same dimensions
		for (TextureSprite tp : allSprites) {
			float texSize = tp.getRegion().width;
			tp.setScale(1.1f * (tileSize / texSize));
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

				TextureSprite center = getTile(pTable[row][col]);

				TextureSprite east = getTile(pTable[row][col + 1]);
				TextureSprite west = getTile(pTable[row][col - 1]);
				TextureSprite north = getTile(pTable[row + 1][col]);
				TextureSprite south = getTile(pTable[row - 1][col]);

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

	private TextureSprite getTile(float value) {
		if (value > 0.5f)
			return grass;
		else if (value > 0.4f)
			return dry;
		else
			return sand[MathUtils.random(0, 1)];
	}

	public float[][] getPTable() {
		return this.pTable;
	}

	/** Draw the map around the given position, by default 8 tiles in each direction */
	public void draw(SpriteBatch batch, Vector2 center) {
		int row, col;

		for (row = 0; row < height; row++) {
			for (col = 0; col < width; col++) {

				float x = col * (tileSize);
				float y = row * (tileSize);
				if (center.dst2(x, y) > 128 * 128)
					continue;

				tTable[row][col].draw(batch, col * tileSize, row * tileSize);
			}
		}


	}

}