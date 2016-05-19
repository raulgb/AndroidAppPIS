package edu.ub.pis2016.pis16.strikecom.engine.util;

import android.util.Log;

import java.util.ArrayList;

import edu.ub.pis2016.pis16.strikecom.engine.android.AndroidMusic;
import edu.ub.pis2016.pis16.strikecom.engine.android.AndroidSound;
import edu.ub.pis2016.pis16.strikecom.engine.framework.Game;
import edu.ub.pis2016.pis16.strikecom.engine.framework.audio.Sound;

/** Utility class for async resource loading */
public class Assets {

	/** Assets status */
	private static boolean ready = false;
	/** Loading progress, 0 for just started, 1 for finished */
	private static float progress = 0f;

	/** Contains all of the game's sprites */
	public static TextureAtlas SPRITE_ATLAS;


	// Sounds and Music
	public static int percentMusic;
	public static AndroidMusic music_bg;
	public static AndroidMusic music_tense;
	public static Sound sfxShoot;

	public static ArrayList<AndroidMusic> musicPlaying = new ArrayList<>();

	/** Launches the assetloader thread, using a Game instace to access the various underlying systems */
	public static void loadAssets(Game game) {
		progress = 0;
		ready = false;
		percentMusic = game.getValueMusic();

		// Start a new async thread to load assets while the game does other stuff
		new Thread(new AssetLoaderRunnable(game)).start();
	}

	public static float getProgress() {
		return progress;
	}

	public static boolean isReady() {
		return ready;
	}

	/** Class to be passed to a Thread to load all game assets asynchronously. */
	private static class AssetLoaderRunnable implements Runnable {
		Game game;

		public AssetLoaderRunnable(Game game) {
			this.game = game;
		}

		@Override
		public void run() {
			Log.i("Assets", "Begun loading");

			try {
				// PUT ALL CODE FOR LOADING TEXTURES, IMAGES, AND OTHER THINGS HERE

				// Global sprite atlas
				SPRITE_ATLAS = new TextureAtlas(game, "sprites/sprites.atlas");

				music_bg = game.getAudio().newMusic("music/waterflame-final_battle.mp3");
				music_tense = game.getAudio().newMusic("music/waterflame-endgame.mp3");

				music_bg.setLooping(true);
				music_bg.setVolume((((float) percentMusic)) / 100.0f);
				music_tense.setVolume((((float) percentMusic)) / 100.0f);
				music_tense.setLooping(true);

				music_bg.play();
				musicPlaying.add(music_bg);

				sfxShoot = game.getAudio().newSound("sounds/shoot.wav");

				Thread.sleep(250);

				// Set flags
				progress = 1;
				ready = true;
				Log.i("Assets", "Finished Loading");

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/** Disposes of all managed assets */
	public static void disposeAll() {
		ready = false;
		music_bg.stop();
		music_bg.dispose();
		SPRITE_ATLAS.dispose();
	}
}
