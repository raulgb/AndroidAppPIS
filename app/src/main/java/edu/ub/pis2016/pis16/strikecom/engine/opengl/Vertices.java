package edu.ub.pis2016.pis16.strikecom.engine.opengl;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

public class Vertices {

	final GLGraphics glGraphics;
	final boolean hasColor;
	final boolean hasTexCoords;
	final int vertexSize;
	final FloatBuffer vertices;
	final ShortBuffer indices;

	/**
	 * Create a new OpenGL vertices object to bind vertices to the render pipeline.
	 *
	 * @param maxVertices  Max number of vertices allocated
	 * @param maxIndices   Max number of indices possible
	 * @param hasColor     Include color information in vertices
	 * @param hasTexCoords Include texture coords information in vertices
	 */
	public Vertices(GLGraphics glGraphics, int maxVertices, int maxIndices, boolean hasColor, boolean hasTexCoords) {

		this.glGraphics = glGraphics;
		this.hasColor = hasColor;
		this.hasTexCoords = hasTexCoords;
		this.vertexSize = (2 + (hasColor ? 4 : 0) + (hasTexCoords ? 2 : 0)) * 4;

		ByteBuffer buffer = ByteBuffer.allocateDirect(maxVertices * vertexSize);
		buffer.order(ByteOrder.nativeOrder());
		vertices = buffer.asFloatBuffer();

		if (maxIndices > 0) {
			buffer = ByteBuffer.allocateDirect(maxIndices * Short.SIZE / 8);
			buffer.order(ByteOrder.nativeOrder());
			indices = buffer.asShortBuffer();
		} else { indices = null; }
	}

	public void setVertices(float[] vertices, int offset, int lenght) {
		this.vertices.clear();
		this.vertices.put(vertices, offset, lenght);
		this.vertices.flip();
	}

	public void setIndices(short[] indices, int offset, int lenght) {
		this.indices.clear();
		this.indices.put(indices, offset, lenght);
		this.indices.flip();
	}

	public void bind() {
		GL10 gl = glGraphics.getGL();

		// Position pointer
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		vertices.position(0);
		gl.glVertexPointer(2, GL10.GL_FLOAT, vertexSize, vertices);

		// Color pointer
		if (hasColor) {
			gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
			vertices.position(2);
			gl.glColorPointer(4, GL10.GL_FLOAT, vertexSize, vertices);
		}

		// Texture pointer
		if (hasTexCoords) {
			gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
			vertices.position(hasColor ? 6 : 2);
			gl.glTexCoordPointer(2, GL10.GL_FLOAT, vertexSize, vertices);
		}
	}

	public void draw(int type, int offset, int numVertices) {
		GL10 gl = glGraphics.getGL();

		// Set indices for triangles if applicable
		if (indices != null) {
			indices.position(offset);
			gl.glDrawElements(type, numVertices, GL10.GL_UNSIGNED_SHORT, indices);
		} else {
			gl.glDrawArrays(type, offset, numVertices);
		}
	}

	public void unbind() {
		GL10 gl = glGraphics.getGL();

		// Clean up states
		if (hasTexCoords) gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		if (hasColor) gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
	}
}
