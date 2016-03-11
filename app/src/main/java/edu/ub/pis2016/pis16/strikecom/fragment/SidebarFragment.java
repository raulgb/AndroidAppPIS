package edu.ub.pis2016.pis16.strikecom.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import javax.microedition.khronos.opengles.GL10;

import edu.ub.pis2016.pis16.strikecom.R;
import edu.ub.pis2016.pis16.strikecom.engine.opengl.GLGameFragment;


public class SidebarFragment extends Fragment {

	public GLGameFragment game;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		//return super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.fragment_sidebar, container, false);

		Button btnRed = (Button)view.findViewById(R.id.btnRed);
		btnRed.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				game.postRunnable(new Runnable() {
					@Override
					public void run() {
						GL10 gl = game.getGLGraphics().getGL();
						gl.glClearColor(1, 0, 0, 1);
					}
				});
			}
		});

		Button btnGreen = (Button)view.findViewById(R.id.btnGreen);
		btnGreen.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				game.postRunnable(new Runnable() {
					@Override
					public void run() {
						GL10 gl = game.getGLGraphics().getGL();
						gl.glClearColor(0, 1, 0, 1);
					}
				});
			}
		});

		return view;
	}

	public void setGame(GLGameFragment game) {
		this.game = game;
	}
}
