package edu.ub.pis2016.pis16.strikecom;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.View;

import edu.ub.pis2016.pis16.strikecom.engine.opengl.GLGameFragment;
import edu.ub.pis2016.pis16.strikecom.fragment.SidebarFragment;

public class FragmentedGameActivity extends Activity {

	PowerManager.WakeLock wakeLock;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Hide window decorations
		hideStatusAndNavBar();

		// Obtain wakelok (to keep the screen on)
		PowerManager powerManager = (PowerManager)getSystemService(Context.POWER_SERVICE);
		wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "GLGame");

		// Load layout with both fragments defined
		setContentView(R.layout.activity_fragmented_game);

		// Get a reference to both fragments
		GLGameFragment gameFragment = (GLGameFragment)getFragmentManager().findFragmentById(R.id.gameFragment);
		SidebarFragment sidebarFragment = (SidebarFragment)getFragmentManager().findFragmentById(R.id.sidebarFragment);

		// Give the sidebar fragment a reference to the game fragment.
		sidebarFragment.setGame(gameFragment);
	}

	@Override
	protected void onPause() {
		super.onPause();
		wakeLock.release();
	}

	@Override
	protected void onResume() {
		super.onResume();
		wakeLock.acquire();
	}

	private void hideStatusAndNavBar() {
		/*
		getActivity().requestWindowFeature(Window.FEATURE_NO_TITLE);
		getActivity().getWindow().setFlags(
				WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN
		);
		*/

		getWindow().getDecorView().setSystemUiVisibility(
				View.SYSTEM_UI_FLAG_LAYOUT_STABLE
						| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
						| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
						| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
						| View.SYSTEM_UI_FLAG_FULLSCREEN
						| View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
	}
}

