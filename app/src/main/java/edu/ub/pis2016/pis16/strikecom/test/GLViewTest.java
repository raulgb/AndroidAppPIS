package edu.ub.pis2016.pis16.strikecom.test;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class GLViewTest extends Activity {

	GLSurfaceView glView;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(
				WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		glView = new GLSurfaceView(this);
		glView.setRenderer(new SimpleRenderer());
		setContentView(glView);
	}

	public void onResume() {
		super.onResume();
		glView.onResume();
	}

	public void onPause() {
		super.onPause();
		glView.onPause();
	}

	public class SimpleRenderer implements GLSurfaceView.Renderer {

		@Override
		public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
			Log.i("GL", "Surface created");
		}

		@Override
		public void onSurfaceChanged(GL10 gl10, int width, int height) {
			Log.i("GL", "Surface changed: " + width + "x" + height);
		}

		@Override
		public void onDrawFrame(GL10 gl) {
			gl.glClearColor(1, 0, 0, 1);
			gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		}
	}

}
