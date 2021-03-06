package edu.ub.pis2016.pis16.strikecom.engine.framework.audio;

public interface Sound {
	public void play(float volume);

	public void stop();

	public void pause();

	public void setLooping(boolean looping);

	public void setVolume(float volume);

	public boolean isPlaying();

	public boolean isStopped();

	public boolean isLooping();

	public void dispose();
}