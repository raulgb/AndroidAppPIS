package edu.ub.pis2016.pis16.strikecom.engine.util;

import android.util.Log;

import java.util.ArrayList;

import edu.ub.pis2016.pis16.strikecom.engine.android.AndroidMusic;
import edu.ub.pis2016.pis16.strikecom.engine.framework.Game;
import edu.ub.pis2016.pis16.strikecom.engine.framework.audio.Music;
import edu.ub.pis2016.pis16.strikecom.engine.framework.audio.Sound;
import edu.ub.pis2016.pis16.strikecom.engine.opengl.Font;

/** Utility class for async resource loading */
public class Assets {

	/** Assets status */
	private static boolean ready = false;
	/** Loading progress, 0 for just started, 1 for finished */
	private static float progress = 0f;

	/** Contains all of the game's sprites */
	public static TextureAtlas SPRITE_ATLAS;
	public static TextureAtlas FONT_ATLAS;
	public static Font font;


	// Sounds and Music
	public static int percentMusic;
	public static Music music_bg;
	public static Music music_bg_2;
	public static Music music_tense;
	public static Sound sfx_shoot;
	public static Sound sfx_shoot_light;
	public static Sound sfx_shoot_laser;
	public static Sound sfx_hit;
	public static Sound sfx_expl_heavy;
	public static Sound sfx_expl_light;

	public static ArrayList<Music> musicPlaying = new ArrayList<>();

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
				FONT_ATLAS = new TextureAtlas(game, "sprites/font.atlas", 64);
				font = new Font(Assets.FONT_ATLAS.getRegions("char"), 32, 32);

				music_bg = game.getAudio().newMusic("music/waterflame-glorious_morning_2.mp3");
				music_bg_2 = game.getAudio().newMusic("music/waterflame-final_battle.mp3");
				music_tense = game.getAudio().newMusic("music/waterflame-endgame.mp3");

				music_bg.setLooping(true);
				music_bg.setVolume((((float) percentMusic)) / 100.0f);
				music_tense.setVolume((((float) percentMusic)) / 100.0f);
				music_tense.setLooping(true);

				music_bg.play();
				musicPlaying.add(music_bg);

				sfx_shoot = game.getAudio().newSound("sounds/shoot.wav");
				sfx_shoot_light = game.getAudio().newSound("sounds/shoot_light.wav");
				sfx_shoot_laser = game.getAudio().newSound("sounds/shoot_laser.wav");
				sfx_hit = game.getAudio().newSound("sounds/hit.wav");
				sfx_expl_light = game.getAudio().newSound("sounds/expl_light.wav");
				sfx_expl_heavy = game.getAudio().newSound("sounds/expl_heavy.wav");

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

		// Stop and dispose SFX
		sfx_expl_heavy.dispose();
		sfx_expl_light.dispose();
		sfx_hit.dispose();

		sfx_shoot.dispose();
		sfx_shoot_light.dispose();
		sfx_shoot_laser.dispose();

		music_bg.stop();
		music_bg.dispose();
		music_bg_2.stop();
		music_bg_2.dispose();
		music_tense.stop();
		music_tense.dispose();

		SPRITE_ATLAS.dispose();
	}
}
