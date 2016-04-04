package edu.ub.pis2016.pis16.strikecom.engine.game.component;

import edu.ub.pis2016.pis16.strikecom.engine.game.UpdateableComponent;
import edu.ub.pis2016.pis16.strikecom.engine.opengl.TextureRegion;
import edu.ub.pis2016.pis16.strikecom.engine.util.Animation;

/**
 * A Component that allows for animation of a sprite
 *
 * @author German
 */
public class AnimatedGraphicsComponent extends GraphicsComponent implements UpdateableComponent {

	Animation anim;
	TextureRegion[] regions;

	public AnimatedGraphicsComponent(TextureRegion[] regions, float frameTime) {
		super(regions[0]);

		anim = new Animation(regions.length);
		anim.setFrameTime(frameTime);
		this.regions = regions;
	}

	public void update(float delta) {
		anim.update(delta);
		setRegion(regions[anim.frame()]);
	}
}
