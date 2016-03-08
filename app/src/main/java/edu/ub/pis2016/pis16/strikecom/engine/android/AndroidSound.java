package edu.ub.pis2016.pis16.strikecom.engine.android;

import android.media.SoundPool;
import android.util.Log;

import edu.ub.pis2016.pis16.strikecom.engine.framework.audio.Sound;

public class AndroidSound implements Sound {
	int soundId;
	SoundPool soundPool;

	public AndroidSound(SoundPool soundPool, int soundId) {
		this.soundId = soundId;
		this.soundPool = soundPool;
	}

	public void play(float volume) {
		soundPool.play(soundId, volume, volume, 0, 0, 1);
	}

	@Override
	public void stop() {
		soundPool.stop(soundId);
	}

	@Override
	public void pause() {
		soundPool.pause(soundId);
	}

	@Override
	public void setLooping(boolean looping) {
		Log.e("SOUND", "METHOD STUB: setLooping");
	}

	@Override
	public void setVolume(float volume) {
		soundPool.setVolume(soundId, volume, volume);

	}

	@Override
	public boolean isPlaying() {
		return false;
	}

	@Override
	public boolean isStopped() {
		return false;
	}

	@Override
	public boolean isLooping() {
		return false;
	}

	public void dispose() {
		soundPool.unload(soundId);
	}
}