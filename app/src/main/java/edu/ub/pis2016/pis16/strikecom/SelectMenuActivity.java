package edu.ub.pis2016.pis16.strikecom;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by Akira on 2016-03-08.
 */
public class SelectMenuActivity extends AppCompatActivity {

	Activity selectMenu;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_menu);

		selectMenu = this;

		Button btnStart = (Button)findViewById(R.id.btnStart);
		btnStart.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent changeToGame = new Intent(selectMenu, Canvas2DGame.class);
				startActivity(changeToGame);
			}
		});

	}
}
