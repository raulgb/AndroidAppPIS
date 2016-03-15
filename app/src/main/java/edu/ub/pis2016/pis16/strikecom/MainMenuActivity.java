package edu.ub.pis2016.pis16.strikecom;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainMenuActivity extends AppCompatActivity {

	Activity mainMenu;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mainMenu = this;

		setContentView(R.layout.activity_main_menu);

		Button btnPlay = (Button)findViewById(R.id.btnPlay);
		btnPlay.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent changeToGame = new Intent(mainMenu, SelectMenuActivity.class);
				startActivity(changeToGame);
			}
		});

		Button btnExit = (Button)findViewById(R.id.btnExit);
		btnExit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				finish();
				//System.exit(0);
			}
		});

		Button btnOptions = (Button)findViewById(R.id.bntOptions);
		btnOptions.setEnabled(false);


	}
}
