package edu.ub.pis2016.pis16.strikecom.fragments;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;

import edu.ub.pis2016.pis16.strikecom.R;
import edu.ub.pis2016.pis16.strikecom.StrikeComGLGame;
import edu.ub.pis2016.pis16.strikecom.gameplay.config.StrikeBaseConfig;


public class SidebarFragment extends Fragment {

	private StrikeBaseConfig.Model strikeBaseModel = StrikeBaseConfig.Model.MKII;
	private StrikeComGLGame game;

	private HashMap<Integer, View> slotsMap = new HashMap<>();


	public void setGame(StrikeComGLGame game) {
		this.game = game;
	}

	public void setStrikeBaseModel(StrikeBaseConfig.Model strikeBaseModel) {this.strikeBaseModel = strikeBaseModel; }

	public View getSlot(int key) { return slotsMap.get(key); }

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_sidebar, container, false);

		// Turret slots
		View btnT1 = view.findViewById(R.id.btnT1);
		View btnT2 = view.findViewById(R.id.btnT2);
		View btnT3 = view.findViewById(R.id.btnT3);
		View btnT4 = view.findViewById(R.id.btnT4);
		View btnT5 = view.findViewById(R.id.btnT5);
		View btnT6 = view.findViewById(R.id.btnT6);

		// Each strike base model has its own turret slots assignation.
		switch(strikeBaseModel) {
			case MKI:
				slotsMap.put(0, btnT1);
				slotsMap.put(1, btnT3);
				slotsMap.put(2, btnT4);
				slotsMap.put(3, btnT5);

				btnT1.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						game.getSidebarListener().onClickTurret(0);
					}
				});
				btnT2.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						game.getSidebarListener().onClickTurret(1);
					}
				});
				btnT3.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						game.getSidebarListener().onClickTurret(2);
					}
				});
				btnT4.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						game.getSidebarListener().onClickTurret(3);
					}
				});
				btnT5.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						game.getSidebarListener().onClickTurret(2);
					}
				});
				btnT6.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						game.getSidebarListener().onClickTurret(2);
					}
				});
				break;

			case MKII:
				slotsMap.put(0, btnT1);
				slotsMap.put(1, btnT4);
				slotsMap.put(2, btnT6);

				btnT1.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						game.getSidebarListener().onClickTurret(0);
					}
				});

				btnT2.setBackgroundColor(Color.TRANSPARENT);
				btnT2.setEnabled(false);

				btnT3.setBackgroundColor(Color.TRANSPARENT);
				btnT3.setEnabled(false);

				btnT4.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						game.getSidebarListener().onClickTurret(1);
					}
				});

				btnT5.setBackgroundColor(Color.TRANSPARENT);
				btnT5.setEnabled(false);

				btnT6.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						game.getSidebarListener().onClickTurret(2);
					}
				});
				break;
		}

		// assignations for Minimap, inventory button and upgrade slots are common to every model.
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

}
