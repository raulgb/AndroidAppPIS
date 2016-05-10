package edu.ub.pis2016.pis16.strikecom.engine.game;

import edu.ub.pis2016.pis16.strikecom.engine.framework.Screen;
import edu.ub.pis2016.pis16.strikecom.engine.game.component.PhysicsComponent;
import edu.ub.pis2016.pis16.strikecom.engine.math.Vector2;
import edu.ub.pis2016.pis16.strikecom.engine.opengl.TextureSprite;
import edu.ub.pis2016.pis16.strikecom.engine.physics.Physics2D;
import edu.ub.pis2016.pis16.strikecom.engine.util.Assets;
import edu.ub.pis2016.pis16.strikecom.engine.util.Perlin2D;
import edu.ub.pis2016.pis16.strikecom.gameplay.StrikeBaseTest;

/**
 * Created by Alexander Bevzenko on 10/05/16.
 * supposed to contain everything related to map
 */
public class GameMap {
	private Perlin2D perlin;
	private float[][] pTable;
	private Physics2D physics2D;
	private StrikeBaseTest strikeBase;
	int sptireSize;

/*
	TextureSprite g0;
	TextureSprite g1;
	TextureSprite g2;
	TextureSprite g3;
	TextureSprite g4;
	TextureSprite g5;
	TextureSprite g6;
	TextureSprite g7;*/
	public float[][] getPTable() {
		return this.pTable;
	}

	/**
	 * This generates map of desired size and adds borders to physics engine
	 * @param seed seed to genetare perlin noise
	 * @param width world width  - measured in 8pixel-squares
	 * @param height world height - measured in 8pixel-squares
	 * @param squareSize - size of a region where interpolation goes
	 * @param octaves - octaves of perlin noise
	 * @param persistence - noise persistence
	 * @param physics2D - link to game's physics
	 * @param strikeBase - link to strikebase, to know it's position
	 */
	public GameMap(long seed,int width, int height, int squareSize, int octaves, float persistence, Physics2D physics2D,StrikeBaseTest
			strikeBase){
		perlin=new Perlin2D(seed);
		sptireSize=8; // for 8x8 sprites
		this.physics2D=physics2D;
		this.strikeBase=strikeBase;
		pTable = new float[width][height];
		pTable = perlin.perlinMap(width,height,squareSize,octaves,persistence);


	}
	public int[] getRelativePosition(){

		int[] result;
		result = new int[2];
		result[0]=(int) Math.floor(strikeBase.getComponent(PhysicsComponent.class).body.position.x/sptireSize);
		result[1]=(int) Math.floor(strikeBase.getComponent(PhysicsComponent.class).body.position.y/sptireSize);
		return result;


	}

}