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

	public Explosion() {
		sprite = new AnimatedSprite(Assets.SPRITE_ATLAS.getRegions("explosion_tank"), 0.075f);

		putComponent(new GraphicsComponent(sprite));
		putComponent(new PhysicsComponent());
		setLayer(Screen.LAYER_GUI);

		sprite.setOnFinishAction(new Runnable() {
			@Override
			public void run() {
				Log.i("Explosion", "onFinish called");
				destroy();
			}
		});
	}

	@Override
	public void update(float delta) {
		sprite.update(delta);
	}
}
