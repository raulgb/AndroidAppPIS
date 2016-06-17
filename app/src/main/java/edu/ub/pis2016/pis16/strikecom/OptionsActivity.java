package edu.ub.pis2016.pis16.strikecom;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import edu.ub.pis2016.pis16.strikecom.engine.framework.Screen;
import edu.ub.pis2016.pis16.strikecom.engine.opengl.GLGameFragment;

/**
 * Created by root on 13/05/16.
 */
public class OptionsActivity extends Activity {

	Activity Options;
	SeekBar seekBarMusic;
	SeekBar seekBarSound;
	TextView textViewMusic;
	TextView textViewSound;
	Switch switchDevMode;

	static int percentMusic = 100;
	static int percentSound = 100;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Hide window decorations
		hideSystemUI();

		setContentView(R.layout.activity_options);
		Options = this;

		final Typeface myCustomFont = Typeface.createFromAsset(getAssets(), getString(R.string.game_font));

		TextView title = (TextView) findViewById(R.id.Options);
		TextView musicLabel = (TextView) findViewById(R.id.Music);
		TextView soundLabel = (TextView) findViewById(R.id.Sound);
		title.setTypeface(myCustomFont);
		musicLabel.setTypeface(myCustomFont);
		soundLabel.setTypeface(myCustomFont);

		seekBarMusic = (SeekBar) findViewById(R.id.seekBarMusic);
		seekBarSound = (SeekBar) findViewById(R.id.seekBarSound);
		textViewMusic = (TextView) findViewById(R.id.percentMusic);
		textViewSound = (TextView) findViewById(R.id.percentSound);
		seekBarMusic.setProgress(percentMusic);
		seekBarSound.setProgress(percentSound);
		textViewMusic.setText(percentMusic + "%");
		textViewSound.setText(percentSound + "%");

		textViewMusic.setTypeface(myCustomFont);
		textViewSound.setTypeface(myCustomFont);

		switchDevMode = (Switch) findViewById(R.id.switchDevMode);

		// Toggle dev mode
		switchDevMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				SharedPreferences prefs = getSharedPreferences(getPackageName(), MODE_PRIVATE);
				prefs.edit().putBoolean("dev_mode", isChecked).apply();

				if (isChecked) {
					Toast.makeText(getApplicationContext(), getString(R.string.dev_mode_on), Toast.LENGTH_LONG).show();
				}
			}
		});

		seekBarMusic.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			int seekBarPercent = 0;

			public void onProgressChanged(SeekBar seekBarMusic, int percent, boolean fromUser) {
				seekBarPercent = percent;
			}

			public void onStartTrackingTouch(SeekBar seekBarMusic) {
			}

			public void onStopTrackingTouch(SeekBar seekBarMusic) {
				textViewMusic.setText(seekBarPercent + "%");
			}
		});
		seekBarSound.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			int seekBarPercent = 0;

			public void onProgressChanged(SeekBar seekBarMusic, int percent, boolean fromUser) {
				seekBarPercent = percent;

			}

			public void onStartTrackingTouch(SeekBar seekBarMusic) {

			}

			public void onStopTrackingTouch(SeekBar seekBarMusic) {
				textViewSound.setText(seekBarPercent + "%");
			}
		});

		Button btnSave = (Button) findViewById(R.id.btnSave);
		btnSave.setTypeface(myCustomFont);

		btnSave.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				percentMusic = seekBarMusic.getProgress();
				percentSound = seekBarSound.getProgress();/*
				Bundle bundle = new Bundle();
                bundle.putInt("percentMusic", percentMusic);
                StrikeComGLGame fragObj = new StrikeComGLGame();
                fragObj.newInstance(bundle);
                FragmentTransaction transaction= getFragmentManager().beginTransaction();
                transaction.replace(R.id.gameFragment, fragObj);
                transaction.commit();*/

				SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(Options);
				SharedPreferences.Editor editor = sharedPreferences.edit();
				editor.putInt("percentMusic", percentMusic);
				editor.commit();
				finish();

                /*Intent changeToGame = new Intent(Options, MainMenuActivity.class);
				startActivity(changeToGame);*/
			}
		});

		Button btnExit = (Button) findViewById(R.id.btnExit2);
		btnExit.setTypeface(myCustomFont);

		btnExit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				finish();
				/*Intent changeToGame = new Intent(Options, MainMenuActivity.class);
				startActivity(changeToGame);*/
			}
		});


	}

	@Override
	protected void onResume() {
		super.onResume();

		// Hide window decorations
		hideSystemUI();
	}

	private void hideSystemUI() {
		// Set the IMMERSIVE flag.
		// Set the content to appear under the system bars so that the content
		// doesn't resize when the system bars hide and show.
		getWindow().getDecorView().setSystemUiVisibility(
				View.SYSTEM_UI_FLAG_LAYOUT_STABLE
						| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
						| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
						| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
						| View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
						| View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
	}

	public int getPercentMusic() {
		return percentMusic;
	}

	public int getPercentSound() {
		return percentSound;
	}
}
