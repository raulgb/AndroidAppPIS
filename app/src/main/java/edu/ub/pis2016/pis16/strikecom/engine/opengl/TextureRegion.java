package edu.ub.pis2016.pis16.strikecom.engine.opengl;

/**
 * A class defining a drawable region from a {@link Texture } object. Used to draw different sections of a
 * texture for animation, STRIKEBASE_ATLAS images, and more. No need to disposeAll of native resources. Use freely.
 */
public class TextureRegion {

	public final float u1, v1;
	public final float u2, v2;
	public final float width, height;
	public final Texture texture;

	/** Define a new texture region from a Texture object. Coordinates start at the top-left corner
	 * of the texture and extend right and downwards. All fields are final.
	 *
	 * @param texture
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	public TextureRegion(Texture texture, float x, float y, float width, float height) {
		this.width = width;
		this.height = height;

		this.u1 = x / texture.width;
		this.v1 = y / texture.height;
		this.u2 = u1 + width / texture.width;
		this.v2 = v1 + height / texture.height;
		this.texture = texture;
	}

	@Override
	public String toString(){
		return new StringBuilder().append(texture.fileName)
				.append(" p: ")
				.append(u1 * texture.width).append(", ").append(v1 * texture.height)
				.append(", s: ")
				.append(width).append(", ").append(height)
				.toString();
	}
}
