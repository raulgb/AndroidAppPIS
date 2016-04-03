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
import android.widget.Toast;

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


		Button btnStart = (Button) findViewById(R.id.btnStart);
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
					startActivity(changeToGame);
				}
			}
		});

		// Disable Texture Filtering
		Paint p = new Paint();
		p.setDither(false);
		p.setAntiAlias(false);
		findViewById(R.id.imgSelectedBase).setLayerPaint(p);

	}
}
