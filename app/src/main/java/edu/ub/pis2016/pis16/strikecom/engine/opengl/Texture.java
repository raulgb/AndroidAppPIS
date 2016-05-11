package edu.ub.pis2016.pis16.strikecom.engine.opengl;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Objects;

import javax.microedition.khronos.opengles.GL10;

import edu.ub.pis2016.pis16.strikecom.engine.framework.Disposable;
import edu.ub.pis2016.pis16.strikecom.engine.framework.FileIO;
import edu.ub.pis2016.pis16.strikecom.engine.framework.Game;

/**
 * Native OpenGL texture object. Manages a Bitmap data array and uploads it to the GPU for rendering
 * using the bind() method. Must be disposed if unused.
 */
public class Texture implements Disposable {

	/** Managed textures */
	public static ArrayList<Texture> managedTextures = new ArrayList<Texture>();

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

	public static void addManagedTexture(Texture t) {
		managedTextures.add(t);
	}

	public static void removeManagedTexture(Texture t) {
		managedTextures.remove(t);
	}

	private static final Object reloadingTextures = new Object();

	public static void reloadManagedTextures() {
		Log.i("Texture", "Reloaded all managed textures");
		synchronized (reloadingTextures) {
			for (Texture t : managedTextures)
				t.reload();
		}
	}

	public static void clearManagedTextures() {
		managedTextures.clear();
	}

}
