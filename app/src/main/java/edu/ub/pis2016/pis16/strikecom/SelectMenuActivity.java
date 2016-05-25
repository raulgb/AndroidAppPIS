package edu.ub.pis2016.pis16.strikecom;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import edu.ub.pis2016.pis16.strikecom.gameplay.config.StrikeBaseConfig;

/**
 * Created by Akira on 2016-03-08.
 */
public class SelectMenuActivity extends AppCompatActivity {

	Activity selectMenu;

	private int selectedConfig = 1;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Hide window decorations
		hideSystemUI();

		setContentView(R.layout.activity_select_menu);

		selectMenu = this;

		final Button btnNext = (Button) findViewById(R.id.btnNext);
		final Button btnPrev = (Button) findViewById(R.id.btnPrev);
		final Button btnStart = (Button) findViewById(R.id.btnStart);

		btnStart.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				// cannot start gameFrag if a name has not been introduced
				EditText textName = (EditText) findViewById(R.id.textName);

				// Disabled check
				if (false && textName.getText().toString().isEmpty()) {

					Toast.makeText(getApplicationContext(), getString(R.string.enter_name_toast),
							Toast.LENGTH_SHORT).show();

				} else {
					Intent changeToGame = new Intent(selectMenu, FragmentedGameActivity.class);
					changeToGame.putExtra("config", selectedConfig);
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

				TextView text = (TextView)findViewById(R.id.textView2);
				String model;
				switch (StrikeBaseConfig.Model.values()[selectedConfig]){
					case MK1:
						model = "sbmk1";
						text.setText(getString(R.string.sel_menu_mk1name));
						btnNext.setBackgroundResource(R.drawable.btn_retro_canv_mk1);
						btnPrev.setBackgroundResource(R.drawable.btn_retro_canv_mk1);
						btnStart.setBackgroundResource(R.drawable.btn_retro_canv_mk1);
						break;
					case MK2:
						model = "sbmk2";
						text.setText(getString(R.string.sel_menu_mk2name));
						btnNext.setBackgroundResource(R.drawable.btn_retro_canv_mk2);
						btnPrev.setBackgroundResource(R.drawable.btn_retro_canv_mk2);
						btnStart.setBackgroundResource(R.drawable.btn_retro_canv_mk2);
						break;
					case MK3:
						model = "sbmk3";
						text.setText(getString(R.string.sel_menu_mk3name));
						btnNext.setBackgroundResource(R.drawable.btn_retro_canv_mk3);
						btnPrev.setBackgroundResource(R.drawable.btn_retro_canv_mk3);
						btnStart.setBackgroundResource(R.drawable.btn_retro_canv_mk3);
						break;
					case MK4:
						model = "sbmk4";
						text.setText(getString(R.string.sel_menu_mk4name));
						btnNext.setBackgroundResource(R.drawable.btn_retro_canv_mk4);
						btnPrev.setBackgroundResource(R.drawable.btn_retro_canv_mk4);
						btnStart.setBackgroundResource(R.drawable.btn_retro_canv_mk4);
						break;
					case MK5:
						model = "sbmk5";
						text.setText(getString(R.string.sel_menu_mk5name));
						btnNext.setBackgroundResource(R.drawable.btn_retro_canv_mk5);
						btnPrev.setBackgroundResource(R.drawable.btn_retro_canv_mk5);
						btnStart.setBackgroundResource(R.drawable.btn_retro_canv_mk5);
						break;
					default:
						model = "sbmk2";
						text.setText(getString(R.string.sel_menu_mk2name));
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

				TextView text = (TextView)findViewById(R.id.textView2);
				String model;
				switch (StrikeBaseConfig.Model.values()[selectedConfig]){
					case MK1:
						model = "sbmk1";
						text.setText(getString(R.string.sel_menu_mk1name));
						btnNext.setBackgroundResource(R.drawable.btn_retro_canv_mk1);
						btnPrev.setBackgroundResource(R.drawable.btn_retro_canv_mk1);
						btnStart.setBackgroundResource(R.drawable.btn_retro_canv_mk1);
						break;
					case MK2:
						model = "sbmk2";
						text.setText(getString(R.string.sel_menu_mk2name));
						btnNext.setBackgroundResource(R.drawable.btn_retro_canv_mk2);
						btnPrev.setBackgroundResource(R.drawable.btn_retro_canv_mk2);
						btnStart.setBackgroundResource(R.drawable.btn_retro_canv_mk2);
						break;
					case MK3:
						model = "sbmk3";
						text.setText(getString(R.string.sel_menu_mk3name));
						btnNext.setBackgroundResource(R.drawable.btn_retro_canv_mk3);
						btnPrev.setBackgroundResource(R.drawable.btn_retro_canv_mk3);
						btnStart.setBackgroundResource(R.drawable.btn_retro_canv_mk3);
						break;
					case MK4:
						model = "sbmk4";
						text.setText(getString(R.string.sel_menu_mk4name));
						btnNext.setBackgroundResource(R.drawable.btn_retro_canv_mk4);
						btnPrev.setBackgroundResource(R.drawable.btn_retro_canv_mk4);
						btnStart.setBackgroundResource(R.drawable.btn_retro_canv_mk4);
						break;
					case MK5:
						model = "sbmk5";
						text.setText(getString(R.string.sel_menu_mk5name));
						btnNext.setBackgroundResource(R.drawable.btn_retro_canv_mk5);
						btnPrev.setBackgroundResource(R.drawable.btn_retro_canv_mk5);
						btnStart.setBackgroundResource(R.drawable.btn_retro_canv_mk5);
						break;
					default:
						model = "sbmk2";
						text.setText(getString(R.string.sel_menu_mk2name));
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
