package edu.ub.pis2016.pis16.strikecom.engine.android;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import edu.ub.pis2016.pis16.strikecom.engine.framework.Audio;
import edu.ub.pis2016.pis16.strikecom.engine.framework.FileIO;
import edu.ub.pis2016.pis16.strikecom.engine.framework.Game;
import edu.ub.pis2016.pis16.strikecom.engine.framework.Graphics;
import edu.ub.pis2016.pis16.strikecom.engine.framework.Input;
import edu.ub.pis2016.pis16.strikecom.engine.framework.Screen;
import edu.ub.pis2016.pis16.strikecom.engine.opengl.GLGraphics;

/**
 * Created by Akira on 2016-02-29.
 */
public abstract class AndroidGame extends Activity implements Game {

	AndroidFastRenderView renderView;
	Graphics graphics;
	Audio audio;
	Input input;
	FileIO fileIO;
	Screen screen;
	PowerManager.WakeLock wakeLock;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.hideStatusAndNavBar();

		// Get landscape state
		boolean isLandscape = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;

		// Device touch size
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
		int frameBufferHeight = metrics.heightPixels;
		int frameBufferWidth = metrics.widthPixels;
		//frameBufferWidth = 1920;

		Log.i("SCREEN", String.format("%dx%d", frameBufferWidth, frameBufferHeight));

		Bitmap frameBuffer = Bitmap.createBitmap(frameBufferWidth, frameBufferHeight, Bitmap.Config.RGB_565);
		float scaleX = 1;
		float scaleY = 1;

		renderView = new AndroidFastRenderView(this, frameBuffer);
		graphics = new AndroidGraphics(getAssets(), frameBuffer);
		fileIO = new AndroidFileIO(this);
		audio = new AndroidAudio(this);
		input = new AndroidInput(this, renderView, scaleX, scaleY);
		screen = getStartScreen();

		setContentView(renderView);
		PowerManager powerManager = (PowerManager)getSystemService(Context.POWER_SERVICE);
		wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "GLGame");
	}

	@Override
	public void onResume() {
		super.onResume();

		wakeLock.acquire();
		screen.resume();
		renderView.resume();
	}

	@Override
	public void onPause() {
		super.onPause();
		wakeLock.release();
		renderView.pause();
		screen.pause();
		if (isFinishing())
			screen.dispose();
	}

	public Input getInput() {
		return input;
	}

	public FileIO getFileIO() {
		return fileIO;
	}

	public Graphics getGraphics() {
		return graphics;
	}

	public GLGraphics getGLGraphics() {
		throw new IllegalStateException("Using Canvas API to draw.");
	}

	public Audio getAudio() {
		return audio;
	}

	public void setScreen(Screen screen) {
		if (screen == null)
			throw new IllegalArgumentException("Screen must not be null");
		this.screen.pause();
		this.screen.dispose();
		screen.resume();
		screen.update(0);
		this.screen = screen;
	}

	public Screen getCurrentScreen() {
		return screen;
	}


	private void hideStatusAndNavBar() {
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
//		getWindow().setFlags(
//				WindowManager.LayoutParams.FLAG_FULLSCREEN,
//				WindowManager.LayoutParams.FLAG_FULLSCREEN
//		);

		getWindow().getDecorView().setSystemUiVisibility(
				View.SYSTEM_UI_FLAG_LAYOUT_STABLE
						| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
						| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
						| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
						| View.SYSTEM_UI_FLAG_FULLSCREEN
						| View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
	}
}
