package edu.ub.pis2016.pis16.strikecom.engine.util;


public class Animation {
	public enum Type {
		FRAME_TIME,
		FRAME_SPEED
	}

	private Type animType;
	private int frames;
	private int frame;
	private float accum;

	// MODE A
	private float frameTime;
	// MODE B
	private float frameSpeed;

	// Advanced
	private Runnable onFinish = null;
	private boolean looping = true;

	public Animation(int frames) {
		this.frames = frames;
		frame = 0;
	}

	/** Time each frame stays on */
	public void setFrameTime(float time) {
		frameTime = time;
		animType = Type.FRAME_TIME;
	}

	/** Frames per second */
	public void setFrameSpeed(float speed) {
		frameSpeed = speed;
		animType = Type.FRAME_SPEED;
	}

	public void update(float delta) {
		switch (animType) {
			case FRAME_TIME:
				accum += delta;
				if (accum >= frameTime) {
					accum -= frameTime;

					// Stop animation and execute Run action
					if (frame == (frames - 1) && !looping) {
						if (onFinish != null)
							onFinish.run();
						animType = Type.FRAME_SPEED;
						frameSpeed = 0;
						break;
					}

					frame = (frame + 1) % frames;
				}
				break;
			case FRAME_SPEED:
				accum += frameSpeed * delta;
				if (accum >= 1) {

					accum -= 1;
					frame = (frame + 1) % frames;
				}
				if (accum <= -1) {
					accum += 1;
					frame = frame == 0 ? frames - 1 : frame - 1;
				}
				break;
		}
	}

	public void setFrame(int frame) {
		this.frame = frame % frames;
	}

	public int frame() {
		return frame;
	}

	public void setOnFinishAction(Runnable runnable) {
		this.onFinish = runnable;
	}

	public void setLooping(boolean looping) {
		this.looping = looping;
	}

	public void play() {
		frame = 0;
		accum = 0;
	}

}
