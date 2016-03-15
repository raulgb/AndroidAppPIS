package edu.ub.pis2016.pis16.strikecom.engine.opengl;


public class TextureRegion {

	public final float u1, v1;
	public final float u2, v2;
	public final Texture texture;

	public TextureRegion(Texture texture, float x, float y, float width, float height) {
		this.u1 = x / texture.width;
		this.v1 = y / texture.height;
		this.u2 = u1 + width / texture.width;
		this.v2 = v1 + height / texture.height;
		this.texture = texture;
	}
}
