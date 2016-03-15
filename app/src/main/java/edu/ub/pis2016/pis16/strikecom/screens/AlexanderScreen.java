package edu.ub.pis2016.pis16.strikecom.screens;

import android.graphics.Color;

import java.util.List;

import edu.ub.pis2016.pis16.strikecom.engine.android.AndroidPixmap;
import edu.ub.pis2016.pis16.strikecom.engine.android.AndroidSprite;
import edu.ub.pis2016.pis16.strikecom.engine.framework.Game;
import edu.ub.pis2016.pis16.strikecom.engine.framework.Graphics;
import edu.ub.pis2016.pis16.strikecom.engine.framework.Input;
import edu.ub.pis2016.pis16.strikecom.engine.framework.Screen;
import edu.ub.pis2016.pis16.strikecom.engine.framework.graphics.Pixmap;
import edu.ub.pis2016.pis16.strikecom.engine.framework.graphics.Sprite;
import edu.ub.pis2016.pis16.strikecom.engine.opengl.OrthoCamera;

/**
 * Created by abevzebe7.alumnes on 15/03/16.
 */
public class AlexanderScreen extends Screen{

    Graphics g;


    OrthoCamera camera;
    Sprite sprite;

    public AlexanderScreen(Game game) {
        super(game);

        g = game.getGraphics();
        camera = new OrthoCamera(1920, 1080);

        Pixmap p = game.getGraphics().newPixmap("strike_base.png");
        sprite = new AndroidSprite(p);
        sprite.setOrigin(0.5f, 0.5f);
    }

    @Override
    public void update(float deltaTime) {
        for(Input.TouchEvent t : game.getInput().getTouchEvents()){

        }

        camera.update();
    }

    @Override
    public void present(float deltaTime) {
        g.clear(Color.WHITE);

        g.setTransformation(camera.combined);


        sprite.draw(g);



    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }
}