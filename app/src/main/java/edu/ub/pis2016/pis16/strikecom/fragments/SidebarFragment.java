package edu.ub.pis2016.pis16.strikecom.fragments;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;

import edu.ub.pis2016.pis16.strikecom.R;
import edu.ub.pis2016.pis16.strikecom.StrikeComGLGame;
import edu.ub.pis2016.pis16.strikecom.gameplay.config.StrikeBaseConfig;


public class SidebarFragment extends Fragment {

	public static StrikeBaseConfig.Model strikeBaseModel = StrikeBaseConfig.Model.MKII;
	private StrikeComGLGame game;

	private HashMap<Integer, View> turretSlotsMap = new HashMap<>();
	private HashMap<Integer, View> upgradeSlotsMap = new HashMap<>();

	private TextView scrapText;
	private TextView fuelText;
	private Button btnInventory;

	public void setGame(StrikeComGLGame game) {
		this.game = game;
	}

	public View getTurretSlot(int key) { return turretSlotsMap.get(key); }

	public View getUpgradeSlot(int key) { return upgradeSlotsMap.get(key); }

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
		View view;
		switch (strikeBaseModel) {
			case MKI:
				view = inflater.inflate(R.layout.fragment_sidebar_mk1, container, false);
				break;
			case MKII:
				view = inflater.inflate(R.layout.fragment_sidebar_mk2, container, false);
				break;
			case MKIII:
				view = inflater.inflate(R.layout.fragment_sidebar_mk3, container, false);
				break;
			case MKIV:
				view = inflater.inflate(R.layout.fragment_sidebar_mk4, container, false);
				break;
			case MKV:
				view = inflater.inflate(R.layout.fragment_sidebar_mk5, container, false);
				break;
			default:
				view = inflater.inflate(R.layout.fragment_sidebar_mk2, container, false);
		}

		btnInventory = (Button) view.findViewById(R.id.btnInventory);

		// Resources display
		scrapText = (TextView) view.findViewById(R.id.textScrap);
		fuelText = (TextView) view.findViewById(R.id.textFuel);

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
				turretSlotsMap.put(0, btnT1);
				turretSlotsMap.put(1, btnT3);
				turretSlotsMap.put(2, btnT4);
				turretSlotsMap.put(3, btnT6);

				btnT1.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						game.getSidebarListener().onClickTurret(0);
					}
				});

				btnT2.setBackgroundColor(Color.TRANSPARENT);
				btnT2.setEnabled(false);

				btnT3.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						game.getSidebarListener().onClickTurret(1);
					}
				});

				btnT4.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						game.getSidebarListener().onClickTurret(2);
					}
				});

				btnT5.setBackgroundColor(Color.TRANSPARENT);
				btnT5.setEnabled(false);

				btnT6.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						game.getSidebarListener().onClickTurret(3);
					}
				});
				break;

			case MKII:
				turretSlotsMap.put(0, btnT1);
				turretSlotsMap.put(1, btnT4);
				turretSlotsMap.put(2, btnT6);

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

			case MKIII:
				turretSlotsMap.put(0, btnT1);
				turretSlotsMap.put(1, btnT3);

				btnT1.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						game.getSidebarListener().onClickTurret(0);
					}
				});

				btnT2.setBackgroundColor(Color.TRANSPARENT);
				btnT2.setEnabled(false);

				btnT3.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						game.getSidebarListener().onClickTurret(1);
					}
				});

				btnT4.setBackgroundColor(Color.TRANSPARENT);
				btnT4.setEnabled(false);

				btnT5.setBackgroundColor(Color.TRANSPARENT);
				btnT5.setEnabled(false);

				btnT6.setBackgroundColor(Color.TRANSPARENT);
				btnT6.setEnabled(false);
				break;

			case MKIV:
				turretSlotsMap.put(0, btnT1);
				turretSlotsMap.put(1, btnT2);
				turretSlotsMap.put(2, btnT3);
				turretSlotsMap.put(3, btnT5);
				turretSlotsMap.put(4, btnT6);

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

				btnT4.setBackgroundColor(Color.TRANSPARENT);
				btnT4.setEnabled(false);

				btnT5.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						game.getSidebarListener().onClickTurret(3);
					}
				});

				btnT6.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						game.getSidebarListener().onClickTurret(4);
					}
				});

			case MKV:
				turretSlotsMap.put(0, btnT1);
				turretSlotsMap.put(1, btnT2);
				turretSlotsMap.put(2, btnT3);
				turretSlotsMap.put(3, btnT4);
				turretSlotsMap.put(4, btnT5);
				turretSlotsMap.put(5, btnT6);

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
						game.getSidebarListener().onClickTurret(4);
					}
				});

				btnT6.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						game.getSidebarListener().onClickTurret(5);
					}
				});
		}

		// assignations for Minimap, inventory button and upgrade slots are common to every model.
		view.findViewById(R.id.btnMinimap).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				game.getSidebarListener().onClickMinimap();
			}
		});
		btnInventory.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				game.getSidebarListener().onClickInventory();
			}
		});
		View btnU1 = view.findViewById(R.id.btnU1);
		View btnU2 = view.findViewById(R.id.btnU2);
		View btnU3 = view.findViewById(R.id.btnU3);

		btnU1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				game.getSidebarListener().onClickUpgrade(0);
			}
		});
		btnU2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				game.getSidebarListener().onClickUpgrade(1);
			}
		});
		btnU3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				game.getSidebarListener().onClickUpgrade(2);
			}
		});

		upgradeSlotsMap.put(0, btnU1);
		upgradeSlotsMap.put(1, btnU2);
		upgradeSlotsMap.put(2, btnU3);

		return view;
	}

	public void updateScrap(int scrap){
		scrapText.setText(Integer.toString(scrap));
	}

	public void updateFuel(float fuel){
		fuelText.setText(Integer.toString(Math.round(fuel)));
	}

	public void setInventoyText(String text){
		btnInventory.setText(text);
	}
}
