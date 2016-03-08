package edu.ub.pis2016.pis16.strikecom;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import edu.ub.pis2016.pis16.strikecom.engine.framework.Game;
import edu.ub.pis2016.pis16.strikecom.engine.framework.Screen;
import edu.ub.pis2016.pis16.strikecom.engine.math.WindowedMean;
import edu.ub.pis2016.pis16.strikecom.engine.opengl.GLGraphics;


class OpenGLTestScreen extends Screen {
	GLGraphics glGraphics;
	FloatBuffer vertices;

	final int w = 1920 / 4, h = 1080 / 4;
	int[] texIds = new int[16];

	/** Vertex Size in bytes */
	public final int VERTEX_SIZE = (2 + 2) * 4;

	public OpenGLTestScreen(Game game) {
		super(game);
		glGraphics = game.getGLGraphics();


		ByteBuffer byteBuffer = ByteBuffer.allocateDirect(3 * VERTEX_SIZE);
		byteBuffer.order(ByteOrder.nativeOrder());
		vertices = byteBuffer.asFloatBuffer();
		vertices.put(new float[]{
				0, 0, 1, 0, 1,
				w, 0, 0, 1, 1,
				w, h, 0, 1, 0,
		});
		vertices.flip();
	}

	@Override
	public void resume() {
		GL10 gl = glGraphics.getGL();
		gl.glViewport(0, 0, glGraphics.getWidth(), glGraphics.getHeight());
		gl.glClearColor(0, 1, 1, 1);
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glOrthof(0, w, 0, h, 1, -1);

		// Setup OpenGL for texture mapping
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

		vertices.position(0);
		gl.glVertexPointer(2, GL10.GL_FLOAT, VERTEX_SIZE, vertices);
		vertices.position(2);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, VERTEX_SIZE, vertices);

		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glColor4f(1, 0, 0, 1);

		// Load Texture
		Bitmap bitmap = BitmapFactory.decodeStream(game.getFileIO().readAsset("tools.png"));

		gl.glGenTextures(1, texIds, 0);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, texIds[0]);
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_NEAREST);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, 0);
		bitmap.recycle();

	}

	WindowedMean fpsMean = new WindowedMean(10);

	@Override
	public void present(float deltaTime) {
		fpsMean.addValue(1f / deltaTime);
		//Log.i("FPS", "" + fpsMean.getMean());

		GL10 gl = glGraphics.getGL();
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		gl.glDrawArrays(GL10.GL_TRIANGLES, 0, 3);
	}

	@Override
	public void update(float deltaTime) {
		game.getInput().getTouchEvents();
		game.getInput().getKeyEvents();
	}

	@Override
	public void pause() {

	}


	@Override
	public void dispose() {

	}
}
