package edu.ub.pis2016.pis16.strikecom.engine.opengl;

import javax.microedition.khronos.opengles.GL10;

import edu.ub.pis2016.pis16.strikecom.engine.math.MathUtils;

public class SpriteBatch {

	/** Temp array to store vertices when draw() is called */
	final float[] verticesBuffer;
	/** Next writtable position on the buffer */
	int bufferIndex;

	final Vertices vertices;
	int numSprites;

	public SpriteBatch(GLGraphics glGraphics, int maxSprites) {
		// num vertices = num sprites * 4 vertices * 4 UV coords
		this.verticesBuffer = new float[maxSprites * 4 * 4];
		// Create vertices buffer. No color coords, only Texture coords
		this.vertices = new Vertices(glGraphics, maxSprites * 4, maxSprites * 6, false, true);
		this.bufferIndex = 0;
		this.numSprites = 0;

		// Set up all sprite indices in one go. These will not vary
		short[] indices = new short[maxSprites * 6];
		int len = indices.length;
		short j = 0;
		for (int i = 0; i < len; i += 6, j += 4) {
			indices[i + 0] = (short)(j + 0);
			indices[i + 1] = (short)(j + 1);
			indices[i + 2] = (short)(j + 2);
			indices[i + 3] = (short)(j + 2);
			indices[i + 4] = (short)(j + 3);
			indices[i + 5] = (short)(j + 0);
		}
		vertices.setIndices(indices, 0, indices.length);
	}

	/** Begin a new batch of sprites, which will all use the same texture. */
	public void begin(Texture texture) {
		texture.bind();
		numSprites = 0;
		bufferIndex = 0;
	}

	/** Drawing will actually occur here. */
	public void end() {
		// Load the temp buffer onto the VBO and bind to OpenGL
		vertices.setVertices(verticesBuffer, 0, bufferIndex);
		vertices.bind();
		// Draw the vertices to the screen, using numSprite*6 as lenght for the number of vertices
		vertices.draw(GL10.GL_TRIANGLES, 0, numSprites * 6);
		vertices.unbind();
	}

	public void drawSprite(float x, float y, TextureRegion region) {
		drawSprite(x, y, region.width, region.height, region);
	}

	public void drawSprite(float x, float y, float width, float height, TextureRegion region) {
		float halfWidth = width / 2;
		float halfHeight = height / 2;
		float x1 = x - halfWidth;
		float y1 = y - halfHeight;
		float x2 = x + halfWidth;
		float y2 = y + halfHeight;

		verticesBuffer[bufferIndex++] = x1;
		verticesBuffer[bufferIndex++] = y1;
		verticesBuffer[bufferIndex++] = region.u1;
		verticesBuffer[bufferIndex++] = region.v2;

		verticesBuffer[bufferIndex++] = x2;
		verticesBuffer[bufferIndex++] = y1;
		verticesBuffer[bufferIndex++] = region.u2;
		verticesBuffer[bufferIndex++] = region.v2;

		verticesBuffer[bufferIndex++] = x2;
		verticesBuffer[bufferIndex++] = y2;
		verticesBuffer[bufferIndex++] = region.u2;
		verticesBuffer[bufferIndex++] = region.v1;

		verticesBuffer[bufferIndex++] = x1;
		verticesBuffer[bufferIndex++] = y2;
		verticesBuffer[bufferIndex++] = region.u1;
		verticesBuffer[bufferIndex++] = region.v1;
		numSprites++;
	}

	/**
	 * Draw a sprite at the given coordinates, scaled to fit the width and height and rotated to the
	 * given angle.
	 *
	 * @param x      Horizontal Position
	 * @param y      Vertical Position
	 * @param width  Final width
	 * @param height Final height
	 * @param angle  Rotation angle
	 * @param region TextureRegion to draw
	 */
	public void drawSprite(float x, float y, float width, float height, float angle, TextureRegion region) {
		// Precalcualte some things
		float halfWidth = width / 2;
		float halfHeight = height / 2;
		float rad = angle * MathUtils.degreesToRadians;
		float cos = MathUtils.cos(rad);
		float sin = MathUtils.sin(rad);

		// All four corners
		float x1 = -halfWidth * cos - (-halfHeight) * sin;
		float y1 = -halfWidth * sin + (-halfHeight) * cos;
		float x2 = halfWidth * cos - (-halfHeight) * sin;
		float y2 = halfWidth * sin + (-halfHeight) * cos;
		float x3 = halfWidth * cos - halfHeight * sin;
		float y3 = halfWidth * sin + halfHeight * cos;
		float x4 = -halfWidth * cos - halfHeight * sin;
		float y4 = -halfWidth * sin + halfHeight * cos;

		// Offset by position
		x1 += x;
		y1 += y;
		x2 += x;
		y2 += y;
		x3 += x;
		y3 += y;
		x4 += x;
		y4 += y;

		verticesBuffer[bufferIndex++] = x1;
		verticesBuffer[bufferIndex++] = y1;
		verticesBuffer[bufferIndex++] = region.u1;
		verticesBuffer[bufferIndex++] = region.v2;

		verticesBuffer[bufferIndex++] = x2;
		verticesBuffer[bufferIndex++] = y2;
		verticesBuffer[bufferIndex++] = region.u2;
		verticesBuffer[bufferIndex++] = region.v2;

		verticesBuffer[bufferIndex++] = x3;
		verticesBuffer[bufferIndex++] = y3;
		verticesBuffer[bufferIndex++] = region.u2;
		verticesBuffer[bufferIndex++] = region.v1;

		verticesBuffer[bufferIndex++] = x4;
		verticesBuffer[bufferIndex++] = y4;
		verticesBuffer[bufferIndex++] = region.u1;
		verticesBuffer[bufferIndex++] = region.v1;
		numSprites++;
	}
}