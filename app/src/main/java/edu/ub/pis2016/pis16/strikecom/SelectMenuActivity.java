package edu.ub.pis2016.pis16.strikecom;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import edu.ub.pis2016.pis16.strikecom.gameplay.PlayerState;
import edu.ub.pis2016.pis16.strikecom.gameplay.config.StrikeBaseConfig;

/**
 * Created by Akira on 2016-03-08.
 */
public class SelectMenuActivity extends AppCompatActivity {

	Activity selectMenu;

	private int selectedConfig = 0;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Hide window decorations
		hideSystemUI();

		setContentView(R.layout.activity_select_menu);

		selectMenu = this;

		final Typeface myCustomFont= Typeface.createFromAsset(getAssets(), getString(R.string.game_font));
		final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(selectMenu);

		TextView title = (TextView) findViewById(R.id.textView);
		TextView label = (TextView) findViewById(R.id.textView3);

		final TextView modelName = (TextView) findViewById(R.id.textView2);
		final Button btnNext = (Button) findViewById(R.id.btnNext);
		final Button btnPrev = (Button) findViewById(R.id.btnPrev);
		final Button btnStart = (Button) findViewById(R.id.btnStart);

		title.setTypeface(myCustomFont);
		label.setTypeface(myCustomFont);
		modelName.setTypeface(myCustomFont);
		btnNext.setTypeface(myCustomFont);
		btnPrev.setTypeface(myCustomFont);
		btnStart.setTypeface(myCustomFont);

		btnStart.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				// cannot start gameFrag if a name has not been introduced
				EditText textName = (EditText) findViewById(R.id.textName);
				textName.setTypeface(myCustomFont);

				// Disabled check
				if (false && textName.getText().toString().isEmpty()) {

					Toast.makeText(getApplicationContext(), getString(R.string.enter_name_toast),
							Toast.LENGTH_SHORT).show();

				} else {
					Intent changeToGame = new Intent(selectMenu, FragmentedGameActivity.class);
					changeToGame.putExtra("config", selectedConfig);
					changeToGame.putExtra("player_name", textName.getText().toString());
					startActivity(changeToGame);
				}
			}
		});


		btnNext.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				selectedConfig += 1;
				if (selectedConfig == StrikeBaseConfig.Model.values().length){
					selectedConfig = 0;
				}

				// TODO Draw a cooler image for locked models
				String model = "locked";
				switch (StrikeBaseConfig.Model.values()[selectedConfig]){
					case MK1:
						model = "sbmk1";
						if (sharedPreferences.getBoolean(model, false)) {
							btnNext.setBackgroundResource(R.drawable.btn_retro_canv_mk1);
							btnPrev.setBackgroundResource(R.drawable.btn_retro_canv_mk1);
							modelName.setText(getString(R.string.sel_menu_mk1name));
							btnStart.setBackgroundResource(R.drawable.btn_retro_canv_mk1);
							btnStart.setEnabled(true);
							break;
						}
						modelName.setText(getString(R.string.sel_menu_locked));
						btnStart.setBackgroundResource(R.drawable.btn_retro_act);
						btnStart.setEnabled(false);
						model = "locked";
						break;

					case MK2:
						model = "sbmk2";
						if (sharedPreferences.getBoolean(model, true)) {
							btnNext.setBackgroundResource(R.drawable.btn_retro_canv_mk2);
							btnPrev.setBackgroundResource(R.drawable.btn_retro_canv_mk2);
							modelName.setText(getString(R.string.sel_menu_mk2name));
							btnStart.setBackgroundResource(R.drawable.btn_retro_canv_mk2);
							btnStart.setEnabled(true);
							break;
						}
						modelName.setText(getString(R.string.sel_menu_locked));
						btnStart.setBackgroundResource(R.drawable.btn_retro_act);
						btnStart.setEnabled(false);
						model = "locked";
						break;

					case MK3:
						model = "sbmk3";
						if (sharedPreferences.getBoolean(model, true)) {
							btnNext.setBackgroundResource(R.drawable.btn_retro_canv_mk3);
							btnPrev.setBackgroundResource(R.drawable.btn_retro_canv_mk3);
							modelName.setText(getString(R.string.sel_menu_mk3name));
							btnStart.setBackgroundResource(R.drawable.btn_retro_canv_mk3);
							btnStart.setEnabled(true);
							break;
						}
						modelName.setText(getString(R.string.sel_menu_locked));
						btnStart.setBackgroundResource(R.drawable.btn_retro_act);
						btnStart.setEnabled(false);
						model = "locked";
						break;

					case MK4:
						model = "sbmk4";
						if (sharedPreferences.getBoolean(model, false)) {
							btnNext.setBackgroundResource(R.drawable.btn_retro_canv_mk4);
							btnPrev.setBackgroundResource(R.drawable.btn_retro_canv_mk4);
							modelName.setText(getString(R.string.sel_menu_mk4name));
							btnStart.setBackgroundResource(R.drawable.btn_retro_canv_mk4);
							btnStart.setEnabled(true);
							break;
						}
						modelName.setText(getString(R.string.sel_menu_locked));
						btnStart.setBackgroundResource(R.drawable.btn_retro_act);
						btnStart.setEnabled(false);
						model = "locked";
						break;

					case MK5:
						model = "sbmk5";
						if (sharedPreferences.getBoolean(model, false)) {
							btnNext.setBackgroundResource(R.drawable.btn_retro_canv_mk5);
							btnPrev.setBackgroundResource(R.drawable.btn_retro_canv_mk5);
							modelName.setText(getString(R.string.sel_menu_mk5name));
							btnStart.setBackgroundResource(R.drawable.btn_retro_canv_mk5);
							btnStart.setEnabled(true);
							break;
						}
						modelName.setText(getString(R.string.sel_menu_locked));
						btnStart.setBackgroundResource(R.drawable.btn_retro_act);
						btnStart.setEnabled(false);
						model = "locked";
						break;
				}
				((ImageView) findViewById(R.id.imgSelectedBase)).setImageResource(getResources().getIdentifier(model, "drawable", getPackageName()));
			}
		});

		btnPrev.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				selectedConfig -= 1;
				if (selectedConfig < 0){
					selectedConfig = StrikeBaseConfig.Model.values().length -1;
				}

				String model = "locked";
				switch (StrikeBaseConfig.Model.values()[selectedConfig]){
					case MK1:
						model = "sbmk1";
						if (sharedPreferences.getBoolean(model, false)) {
							btnNext.setBackgroundResource(R.drawable.btn_retro_canv_mk1);
							btnPrev.setBackgroundResource(R.drawable.btn_retro_canv_mk1);
							modelName.setText(getString(R.string.sel_menu_mk1name));
							btnStart.setBackgroundResource(R.drawable.btn_retro_canv_mk1);
							btnStart.setEnabled(true);
							break;
						}
						modelName.setText(getString(R.string.sel_menu_locked));
						btnStart.setBackgroundResource(R.drawable.btn_retro_act);
						btnStart.setEnabled(false);
						model = "locked";
						break;

					case MK2:
						model = "sbmk2";
						if (sharedPreferences.getBoolean(model, true)) {
							btnNext.setBackgroundResource(R.drawable.btn_retro_canv_mk2);
							btnPrev.setBackgroundResource(R.drawable.btn_retro_canv_mk2);
							modelName.setText(getString(R.string.sel_menu_mk2name));
							btnStart.setBackgroundResource(R.drawable.btn_retro_canv_mk2);
							btnStart.setEnabled(true);
							break;
						}
						modelName.setText(getString(R.string.sel_menu_locked));
						btnStart.setBackgroundResource(R.drawable.btn_retro_act);
						btnStart.setEnabled(false);
						model = "locked";
						break;

					case MK3:
						model = "sbmk3";
						if (sharedPreferences.getBoolean(model, true)) {
							btnNext.setBackgroundResource(R.drawable.btn_retro_canv_mk3);
							btnPrev.setBackgroundResource(R.drawable.btn_retro_canv_mk3);
							modelName.setText(getString(R.string.sel_menu_mk3name));
							btnStart.setBackgroundResource(R.drawable.btn_retro_canv_mk3);
							btnStart.setEnabled(true);
							break;
						}
						modelName.setText(getString(R.string.sel_menu_locked));
						btnStart.setBackgroundResource(R.drawable.btn_retro_act);
						btnStart.setEnabled(false);
						model = "locked";
						break;

					case MK4:
						model = "sbmk4";
						if (sharedPreferences.getBoolean(model, false)) {
							btnNext.setBackgroundResource(R.drawable.btn_retro_canv_mk4);
							btnPrev.setBackgroundResource(R.drawable.btn_retro_canv_mk4);
							modelName.setText(getString(R.string.sel_menu_mk4name));
							btnStart.setBackgroundResource(R.drawable.btn_retro_canv_mk4);
							btnStart.setEnabled(true);
							break;
						}
						modelName.setText(getString(R.string.sel_menu_locked));
						btnStart.setBackgroundResource(R.drawable.btn_retro_act);
						btnStart.setEnabled(false);
						model = "locked";
						break;

					case MK5:
						model = "sbmk5";
						if (sharedPreferences.getBoolean(model, false)) {
							btnNext.setBackgroundResource(R.drawable.btn_retro_canv_mk5);
							btnPrev.setBackgroundResource(R.drawable.btn_retro_canv_mk5);
							modelName.setText(getString(R.string.sel_menu_mk5name));
							btnStart.setBackgroundResource(R.drawable.btn_retro_canv_mk5);
							btnStart.setEnabled(true);
							break;
						}
						modelName.setText(getString(R.string.sel_menu_locked));
						btnStart.setBackgroundResource(R.drawable.btn_retro_act);
						btnStart.setEnabled(false);
						model = "locked";
						break;
				}
				((ImageView) findViewById(R.id.imgSelectedBase)).setImageResource(getResources().getIdentifier(model, "drawable", getPackageName()));
			}
		});

		// Disable Texture Filtering
		Paint p = new Paint();
		p.setDither(false);
		p.setAntiAlias(false);
		findViewById(R.id.imgSelectedBase).setLayerPaint(p);
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
}
