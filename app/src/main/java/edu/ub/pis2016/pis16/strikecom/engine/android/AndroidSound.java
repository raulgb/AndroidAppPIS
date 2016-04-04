package edu.ub.pis2016.pis16.strikecom.engine.android;

import android.media.SoundPool;

import edu.ub.pis2016.pis16.strikecom.engine.framework.audio.Sound;

public class AndroidSound implements Sound {

	SoundPool soundPool;

	int looping = 0;
	int soundId;
	int streamId;

	public AndroidSound(SoundPool soundPool, int soundId) {
		this.soundId = soundId;
		this.soundPool = soundPool;
	}

	public void play(float volume) {
		streamId = soundPool.play(soundId, volume, volume, 0, looping, 1);
	}

	@Override
	public void stop() {
		soundPool.stop(streamId);
	}

	@Override
	public void pause() {
		soundPool.pause(streamId);
	}

	@Override
	public void setLooping(boolean looping) {
		this.looping = looping ? 1 : 0;
	}

	@Override
	public void setVolume(float volume) {
		soundPool.setVolume(streamId, volume, volume);

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
		return looping == 1;
	}

	public void dispose() {
		soundPool.unload(soundId);
	}
}