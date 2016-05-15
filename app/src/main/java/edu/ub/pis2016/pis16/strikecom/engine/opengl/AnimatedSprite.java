package edu.ub.pis2016.pis16.strikecom.engine.opengl;

import edu.ub.pis2016.pis16.strikecom.engine.util.Animation;

/**
 * An automatically animated sprite wrapped over a basic sprite.
 * Created by Herman Dempere on 15/05/2016.
 */
public class AnimatedSprite extends Sprite {

	private TextureRegion[] regions = null;
	private Animation anim;

	public AnimatedSprite(TextureRegion[] regions, float frameTime) {
		super(regions[0]);

		anim = new Animation(regions.length);
		anim.setFrameTime(frameTime);
	}

	public void setRegions(TextureRegion[] regions) {
		this.regions = regions;
	}

	public void update(float delta) {
		anim.update(delta);
	}

	public void draw(SpriteBatch batch) {
		super.setRegion(regions[anim.frame()]);
		super.draw(batch);
	}
}
