package edu.ub.pis2016.pis16.strikecom.component;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.ub.pis2016.pis16.strikecom.R;
import edu.ub.pis2016.pis16.strikecom.StrikeComGLGame;


public class SidebarFragment extends Fragment {

	private StrikeComGLGame game;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
		//return super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.fragment_sidebar, container, false);

		// Link button clicks to the sidebar listener
		view.findViewById(R.id.btnMinimap).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				game.getSidebarListener().onClickMinimap();
			}
		});

		view.findViewById(R.id.btnInventory).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				game.getSidebarListener().onClickInventory();
			}
		});

		view.findViewById(R.id.btnT1).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				game.getSidebarListener().onClickTurret(0);
			}
		});

		view.findViewById(R.id.btnT2).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				game.getSidebarListener().onClickTurret(1);
			}
		});

		view.findViewById(R.id.btnT3).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				game.getSidebarListener().onClickTurret(2);
			}
		});

		view.findViewById(R.id.btnT4).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				game.getSidebarListener().onClickTurret(3);
			}
		});

		view.findViewById(R.id.btnT5).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				game.getSidebarListener().onClickTurret(4);
			}
		});

		view.findViewById(R.id.btnT6).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				game.getSidebarListener().onClickTurret(5);
			}
		});

		view.findViewById(R.id.btnU1).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				game.getSidebarListener().onClickUpgrade(0);
			}
		});

		view.findViewById(R.id.btnU2).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				game.getSidebarListener().onClickUpgrade(1);
			}
		});

		view.findViewById(R.id.btnU3).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				game.getSidebarListener().onClickUpgrade(2);
			}
		});

		return view;
	}

	public void setGame(StrikeComGLGame game) {
		this.game = game;
	}
}
