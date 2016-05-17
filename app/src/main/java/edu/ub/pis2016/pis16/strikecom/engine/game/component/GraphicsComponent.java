package edu.ub.pis2016.pis16.strikecom.engine.game.component;

import edu.ub.pis2016.pis16.strikecom.engine.game.Component;
import edu.ub.pis2016.pis16.strikecom.engine.game.DrawableComponent;
import edu.ub.pis2016.pis16.strikecom.engine.opengl.SpriteBatch;
import edu.ub.pis2016.pis16.strikecom.engine.opengl.TextureRegion;
import edu.ub.pis2016.pis16.strikecom.engine.opengl.Sprite;

/**
 * A Component in charge of drawing anything that a GameObject might need to draw.
 *
 * @author German
 */
public class GraphicsComponent extends Component implements DrawableComponent {

	private Sprite sprite;

	public GraphicsComponent(TextureRegion region) {
		this.sprite = new Sprite(region);
	}

	public GraphicsComponent(Sprite sprite){
		this.sprite = sprite;
	}

	/** Draws the associated Drawable. If the GameObject has a PhysicsComponent, it will pull position data from it. */
	public void draw(SpriteBatch batch) {
		// If entity has a physics component, draw to it
		PhysicsComponent pc;
		if ((pc = gameObject.getComponent(PhysicsComponent.class)) != null) {
			sprite.setPosition(pc.getPosition());
			sprite.setRotation(pc.getRotation());
		}

		if (sprite != null)
			sprite.draw(batch);
	}

	/** Draw the associated drawable to the given coordinates. */
	public void draw(SpriteBatch batch, float x, float y) {
		sprite.draw(batch, x, y);
	}

	public void setRegion(TextureRegion region) {
		sprite.setRegion(region);
	}

	public Sprite getSprite() {
		return sprite;
	}

}
