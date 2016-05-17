package edu.ub.pis2016.pis16.strikecom.gameplay;

import android.util.Log;

import edu.ub.pis2016.pis16.strikecom.engine.framework.Screen;
import edu.ub.pis2016.pis16.strikecom.engine.game.GameObject;
import edu.ub.pis2016.pis16.strikecom.engine.game.component.GraphicsComponent;
import edu.ub.pis2016.pis16.strikecom.engine.game.component.PhysicsComponent;
import edu.ub.pis2016.pis16.strikecom.engine.opengl.AnimatedSprite;
import edu.ub.pis2016.pis16.strikecom.engine.util.Assets;

public class Explosion extends GameObject {

	AnimatedSprite sprite;

	public Explosion(String effectName) {
		// Construct a new AnimatedSprite, no looping and destroy this gameObject on finish
		sprite = new AnimatedSprite(Assets.SPRITE_ATLAS.getRegions(effectName), 0.1f);
		sprite.setLooping(false);
		sprite.setOnFinishAction(new Runnable() {
			@Override
			public void run() {
				destroy();
			}
		});

		// Setup
		putComponent(new GraphicsComponent(sprite));
		putComponent(new PhysicsComponent());
		setLayer(Screen.LAYER_EFFECTS);
	}

	@Override
	public void update(float delta) {
		sprite.update(delta);
	}
}
