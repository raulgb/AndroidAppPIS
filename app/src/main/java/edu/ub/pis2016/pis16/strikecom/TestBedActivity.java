package edu.ub.pis2016.pis16.strikecom;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class TestBedActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Define type of game here
		Intent changeToGame = new Intent(this, Canvas2DGame.class);
		startActivity(changeToGame);
	}

}
