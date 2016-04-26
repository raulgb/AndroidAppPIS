package edu.ub.pis2016.pis16.strikecom.engine.util;
import java.util.Random;
/**
 * Perlin noise implementation
 */
public class Perlin2D {
	byte [] permutationTable;

	/**
	 * constructor
	 * @param seed seed to generate pseudo random values from
	 */
	public Perlin2D(long seed){
		Random random = new Random(seed);
		permutationTable= new byte[1024];
		random.nextBytes(permutationTable);
	}

	/**
	 *
	 * @param x x-coordinate
	 * @param y y-coordinate
	 * @return returns one of 4 basic gradient vectors
	 */
	private float [] getPseudoRandomGradientVector(int x, int y){
		int v = (int)(((x * 1836311903) ^ (y * 2971215073L)+ 4807526976L) & 1023);
		v=permutationTable[v]&3;

		switch(v) {
			case 0:
				return new float[]{1, 0};
			case 1:
				return new float[]{-1, 0};
			case 2:
				return new float[]{0, 1};
			default:
				return new float[]{0, -1};
		}

	}

	/**
	 *   qiuntic curve
	 * @param t float from 0 to 1
	 * @return
	 */
	static float quinticCurve(float t){
		return t* t * t * (t * (t*6-15)+10);
	}

	/**
	 * linear interpolation
	 * @param a left border of interval
	 * @param b right border of interval
	 * @param t point location between a and b
	 * @return returns interpolated value of t
	 */
	static float Lerp(float a, float b, float t){
		return a + (b - a) * t;
	}

	/**
	 * Dot product of 2d vectors
	 * @param a 2d vector a
	 * @param b 2d vector b
	 * @return dot product
	 */
	static float Dot(float[] a, float[] b){
		return a[0]*b[0]+a[1]*b[1];
	}

	/**
	 * takes 2d point and returns float value
	 * @param fx x coordinate
	 * @param fy y coordinate
	 * @return returns float value assigned to this point
	 */

	public float Noise (float fx, float fy){
		int left= (int) Math.floor(fx);
		int top= (int) Math.floor(fy);
		float pointInQuadX= fx-left;
		float pointInQuadY=fy-top;

		float[] topLeftGradient = getPseudoRandomGradientVector(left, top);
		float[] topRightGradient = getPseudoRandomGradientVector(left + 1, top);
		float[] bottomLeftGradient = getPseudoRandomGradientVector(left, top + 1);
		float[] bottomRightGradient = getPseudoRandomGradientVector(left + 1, top + 1);

		float[] distanceToTopLeft = new float[]{pointInQuadX,pointInQuadY};
		float[] distanceToTopRight = new float[]{pointInQuadX-1,pointInQuadY};
		float[] distanceToBottomLeft = new float[]{pointInQuadX,pointInQuadY-1};
		float[] distanceToBottomRight = new float[]{pointInQuadX-1,pointInQuadY-1};

		float tx1 = Dot(distanceToTopLeft,topLeftGradient);
		float tx2 = Dot(distanceToTopRight,topRightGradient);
		float bx1 = Dot(distanceToBottomLeft,bottomLeftGradient);
		float bx2 = Dot(distanceToBottomRight,bottomRightGradient);

		pointInQuadX=quinticCurve(pointInQuadX);
		pointInQuadY=quinticCurve(pointInQuadY);

		float tx = Lerp(tx1, tx2, pointInQuadX);
		float bx = Lerp(bx1, bx2, pointInQuadX);
		float tb = Lerp(tx,bx, pointInQuadY);

		return tb;
	}

	/**
	 *
	 * @param fx x-coodrinate
	 * @param fy y-coordinate
	 * @param octaves number of octaves in noise
	 * @param persistence - noise persistence
	 * @return returns float value assigned to this point
	 */

	public float Noise(float fx, float fy, int octaves, float persistence){
		float amplitude = 1;
		float max = 0;
		float result = 0;
		while (octaves-- >0){
			max += amplitude;
			result += Noise(fx,fy)*amplitude;
			amplitude *= persistence;
			fx *=2;
			fy *=2;
		}

		return result/max;
	}

	/**
	 * generates 2d table with float values from -1 to 1
	 * @param width map width
	 * @param height map height
	 * @param squareSize squares, gradient interpolation is done into
	 * @param octaves octaves of noise
	 * @param persistence noise persistence
	 * @return returns 2d table with float values from -1 to 1
	 */

	public float[][] perlinMap(int width, int height, int squareSize,int octaves, float persistence){
		float[][] resultMap = new float [width][height];
		float increment = 1f/(float)squareSize;
		float cX=0;
		float cY=0;
		for(int i=0;i<width;i++){
			for(int j=0;i<height;j++){
				resultMap[i][j]=this.Noise(cX,cY,octaves,persistence);
				cY+=increment;
			}
			cX+=increment;
		}

		return resultMap;
	}


}
