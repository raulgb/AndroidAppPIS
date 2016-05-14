package edu.ub.pis2016.pis16.strikecom;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainMenuActivity extends AppCompatActivity{

	Activity mainMenu;

	@Override
	protected void onCreate(Bundle savedInstanceState) {


		super.onCreate(savedInstanceState);
		mainMenu = this;

		setContentView(R.layout.activity_main_menu);

		Button btnPlay = (Button) findViewById(R.id.btnPlay);
		Button btnExit = (Button) findViewById(R.id.btnExit);
		Button btnOptions = (Button) findViewById(R.id.bntOptions);
		Typeface myCustomFont= Typeface.createFromAsset(getAssets(), "fonts/Minecraft.ttf");
		btnPlay.setTypeface(myCustomFont);
		btnExit.setTypeface(myCustomFont);
		btnOptions.setTypeface(myCustomFont);
		btnPlay.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent changeToGame = new Intent(mainMenu, SelectMenuActivity.class);

				startActivity(changeToGame);
			}
		});

		btnExit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				finish();
				//System.exit(0);
			}
		});

		btnOptions.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View view){
				Intent changeToGame = new Intent(mainMenu, OptionsActivity.class);
				startActivity(changeToGame);
			}
		});

		getWindow().getDecorView().setSystemUiVisibility(
				View.SYSTEM_UI_FLAG_LAYOUT_STABLE
						| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
						| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
						| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
						| View.SYSTEM_UI_FLAG_FULLSCREEN
						| View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
	}
}
