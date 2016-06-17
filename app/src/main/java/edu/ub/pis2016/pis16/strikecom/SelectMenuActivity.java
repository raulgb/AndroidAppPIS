package edu.ub.pis2016.pis16.strikecom;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.util.CircularArray;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import edu.ub.pis2016.pis16.strikecom.engine.math.MathUtils;
import edu.ub.pis2016.pis16.strikecom.gameplay.PlayerState;
import edu.ub.pis2016.pis16.strikecom.gameplay.StrikeBase;
import edu.ub.pis2016.pis16.strikecom.gameplay.config.StrikeBaseConfig;

public class SelectMenuActivity extends AppCompatActivity {

	Activity selectMenu;

	private int selectedConfig = 0;

	private Typeface myCustomFont;
	private SharedPreferences sharedPreferences;

	private TextView modelName;
	private Button btnNext;
	private Button btnPrev;
	private Button btnStart;

	private TextView title;
	private TextView label;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Hide window decorations
		hideSystemUI();

		setContentView(R.layout.activity_select_menu);

		selectMenu = this;

		myCustomFont = Typeface.createFromAsset(getAssets(), getString(R.string.game_font));
		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(selectMenu);

		sharedPreferences.edit().putBoolean("sbmk3", true).apply();

		modelName = (TextView) findViewById(R.id.textView2);
		btnNext = (Button) findViewById(R.id.btnNext);
		btnPrev = (Button) findViewById(R.id.btnPrev);
		btnStart = (Button) findViewById(R.id.btnStart);

		title = (TextView) findViewById(R.id.textView);
		label = (TextView) findViewById(R.id.textView3);
		title.setTypeface(myCustomFont);
		label.setTypeface(myCustomFont);

		modelName.setTypeface(myCustomFont);
		btnNext.setTypeface(myCustomFont);
		btnPrev.setTypeface(myCustomFont);
		btnStart.setTypeface(myCustomFont);

		// Display the first Strikebase
		showStrikeBaseSelection(StrikeBaseConfig.Model.MK3);

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
				selectedConfig = MathUtils.mod(selectedConfig + 1, StrikeBaseConfig.Model.values().length);
				showStrikeBaseSelection(StrikeBaseConfig.Model.values()[selectedConfig]);
			}
		});

		btnPrev.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				selectedConfig = MathUtils.mod(selectedConfig - 1, StrikeBaseConfig.Model.values().length);
				showStrikeBaseSelection(StrikeBaseConfig.Model.values()[selectedConfig]);
			}
		});

		// Disable Texture Filtering
		Paint p = new Paint();
		p.setDither(false);
		p.setAntiAlias(false);
		findViewById(R.id.imgSelectedBase).setLayerPaint(p);
	}

	private void showStrikeBaseSelection(StrikeBaseConfig.Model model) {
		String suffix = model.toString().toLowerCase();

		Log.i("Select", model.toString());

		boolean devMode = getSharedPreferences(getPackageName(), 0).getBoolean("dev_mode", false);

		ImageView imageView = ((ImageView) findViewById(R.id.imgSelectedBase));
		int btnResId = getResources().getIdentifier("btn_retro_canv_" + suffix, "drawable", getPackageName());
		int sbNameResId = getResources().getIdentifier("sel_menu_" + suffix + "name", "string", getPackageName());

		if (devMode || sharedPreferences.getBoolean("sb" + suffix, false)) {
			btnNext.setBackgroundResource(btnResId);
			btnPrev.setBackgroundResource(btnResId);
			modelName.setText(getString(sbNameResId));
			btnStart.setBackgroundResource(btnResId);
			btnStart.setEnabled(true);

			imageView.setImageResource(getResources().getIdentifier("sb" + suffix, "drawable", getPackageName()));
			return;
		}

		imageView.setImageResource(R.drawable.locked);
		modelName.setText(getString(R.string.sel_menu_locked));
		btnStart.setBackgroundResource(R.drawable.btn_retro_act);
		btnStart.setEnabled(false);
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
