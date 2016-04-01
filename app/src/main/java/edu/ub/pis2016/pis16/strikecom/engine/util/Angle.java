package edu.ub.pis2016.pis16.strikecom.engine.util;

public class Angle {

	/** Returns the closest arc between two angles, rotates correctly around all 360 degrees */
	public static float angleDelta(float from, float to) {
		return ((to - from) + 180) % 360 - 180;
	}
}
