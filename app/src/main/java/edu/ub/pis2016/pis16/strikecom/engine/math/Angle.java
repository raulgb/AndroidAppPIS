package edu.ub.pis2016.pis16.strikecom.engine.math;

import java.util.Locale;

public class Angle {

	/** Returns the closest arc between two angles, rotates correctly around all 360 degrees */
	public static float angleDelta(float from, float to) {
		//return ((to - from + 360 + 180) % 360) - 180;
		return ((((to - from) % 360) + 540) % 360) - 180;
	}

	public static boolean angleInsideArc(float angle, float start, float end) {
		end = (end - start) < 0.0f ? end - start + 360.0f : end - start;
		angle = (angle - start) < 0.0f ? angle - start + 360.0f : angle - start;
		return (angle < end);
	}

	public static void unitTest() {

		float targetAngle = 90;
		try {

			for (int a = 0; a < 360; a++) {
				Thread.sleep(1);
				System.out.println(a + " to " + targetAngle + ": " + angleDelta(a, targetAngle));
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.exit(0);

		float[][] tests = new float[6][3];
		tests[0][0] = 0;
		tests[0][1] = 90;
		tests[0][2] = 90;

		tests[1][0] = 0;
		tests[1][1] = 270;
		tests[1][2] = -90;

		tests[2][0] = 180;
		tests[2][1] = 90;
		tests[2][2] = -90;

		tests[3][0] = 180;
		tests[3][1] = 270;
		tests[3][2] = 90;

		tests[4][0] = 45;
		tests[4][1] = 360 - 45;
		tests[4][2] = -90;

		tests[5][0] = 180 + 15;
		tests[5][1] = 180 - 15;
		tests[5][2] = -30;

		for (int i = 0; i < 6; i++) {
			float[] test = tests[i];
			String res = String.format(Locale.ENGLISH, "%3.0f to %3.0f: %3.0f : %3.0f", test[0], test[1], angleDelta(test[0], test[1]), test[2]);
			System.out.println(res);
		}

		System.exit(0);
	}
}
