package edu.ub.pis2016.pis16.strikecom.engine.opengl;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.khronos.opengles.GL10;

import edu.ub.pis2016.pis16.strikecom.engine.framework.Disposable;
import edu.ub.pis2016.pis16.strikecom.engine.framework.FileIO;
import edu.ub.pis2016.pis16.strikecom.engine.framework.Game;
import edu.ub.pis2016.pis16.strikecom.engine.util.performance.Array;

/**
 * Native OpenGL texture object. Manages a Bitmap data array and uploads it to the GPU for rendering
 * using the bind() method. Must be disposed if unused.
 */
public class Texture implements Disposable {

	/** Managed textures */
	public static Array<Texture> managedTextures = new Array<>();

	GLGraphics glGraphics;
	FileIO fileIO;
	String fileName;
	int textureId;
	int minFilter = GL10.GL_NEAREST;
	int magFilter = GL10.GL_NEAREST;
	int width;
	int height;

	public Texture(Game game, String fileName) {
		this.glGraphics = game.getGLGraphics();
		this.fileIO = game.getFileIO();
		this.fileName = fileName;
		load();

		Texture.addManagedTexture(this);
	}

	private void load() {
		GL10 gl = glGraphics.getGL();
		int[] textureIds = new int[1];
		gl.glGenTextures(1, textureIds, 0);
		textureId = textureIds[0];
		InputStream in = null;
		try {
			in = fileIO.readAsset(fileName);
			Bitmap bitmap = BitmapFactory.decodeStream(in);
			width = bitmap.getWidth();
			height = bitmap.getHeight();
			gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId);
			GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
			setFilters(minFilter, magFilter);
			gl.glBindTexture(GL10.GL_TEXTURE_2D, 0);
		} catch (Exception e) {
			throw new RuntimeException("Couldn't load texture '" + fileName + "'", e);
		} finally {
			if (in != null)
				try {
					in.close();
				} catch (IOException e) {
				}
		}
	}

	public void reload() {
		load();
		bind();
		setFilters(minFilter, magFilter);
		glGraphics.getGL().glBindTexture(GL10.GL_TEXTURE_2D, 0);
	}

	/** Must be called after Texture.bind() */
	public void setFilters(int minFilter, int magFilter) {
		this.minFilter = minFilter;
		this.magFilter = magFilter;
		GL10 gl = glGraphics.getGL();

		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE);

		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, minFilter);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, magFilter);
	}

	public void bind() {
		GL10 gl = glGraphics.getGL();
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId);
	}

	public void dispose() {
		GL10 gl = glGraphics.getGL();
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId);
		int[] textureIds = {textureId};
		gl.glDeleteTextures(1, textureIds, 0);

		Texture.removeManagedTexture(this);
	}


	// OpenGL context loss prevention methods

	private static final Object managedTextureLock = new Object();

	public static void addManagedTexture(Texture t) {
		synchronized (managedTextureLock) {
			managedTextures.add(t);
//			managedTextureLock.notifyAll();
		}
	}

	public static void removeManagedTexture(Texture t) {
		synchronized (managedTextureLock) {
			managedTextures.removeValue(t);
//			managedTextureLock.notifyAll();
		}
	}

	public static void reloadManagedTextures() {
		Log.i("Texture", "Reloaded all managed textures");
		synchronized (managedTextureLock) {
			for (Texture t : managedTextures)
				t.reload();
//				managedTextureLock.notifyAll();
		}
	}

	public static void clearManagedTextures() {
		managedTextures.clear();
	}

	@Override
	public boolean equals(Object o) {
		if(o instanceof Texture)
			return this.fileName.equalsIgnoreCase(((Texture)o).fileName);
		return false;
	}
}
