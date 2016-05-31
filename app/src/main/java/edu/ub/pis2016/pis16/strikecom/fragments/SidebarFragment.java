package edu.ub.pis2016.pis16.strikecom.fragments;

import android.app.Fragment;

import android.graphics.BitmapRegionDecoder;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;

import edu.ub.pis2016.pis16.strikecom.R;
import edu.ub.pis2016.pis16.strikecom.StrikeComGLGame;
import edu.ub.pis2016.pis16.strikecom.engine.math.MathUtils;
import edu.ub.pis2016.pis16.strikecom.engine.math.Vector2;
import edu.ub.pis2016.pis16.strikecom.gameplay.config.GameConfig;
import edu.ub.pis2016.pis16.strikecom.gameplay.config.StrikeBaseConfig;


public class SidebarFragment extends Fragment {

	public static StrikeBaseConfig.Model strikeBaseModel = StrikeBaseConfig.Model.MK2;
	private StrikeComGLGame game;

	private HashMap<Integer, View> turretSlotsMap = new HashMap<>();
	private HashMap<Integer, View> upgradeSlotsMap = new HashMap<>();

	private TextView scrapText;
	private TextView fuelText;

	private Button btnInventory;
	private Button btnMinimap;

	public void setGame(StrikeComGLGame game) {
		this.game = game;
	}

	public View getTurretSlot(int key) {
		return turretSlotsMap.get(key);
	}

	public View getUpgradeSlot(int key) {
		return upgradeSlotsMap.get(key);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_sidebar, container, false);

		final Typeface myCustomFont = Typeface.createFromAsset(getActivity().getAssets(), getString(R.string.game_font));

		/** Extract all UI elements */
		btnInventory = (Button) view.findViewById(R.id.btnInventory);
		btnMinimap = (Button) view.findViewById(R.id.btnMinimap);
		btnMinimap.setRotation(-90); // to display minimap correctly - easiest solution ^^

		scrapText = (TextView) view.findViewById(R.id.textScrap);
		fuelText = (TextView) view.findViewById(R.id.textFuel);

		// Apply custom font
		btnInventory.setTypeface(myCustomFont);
		scrapText.setTypeface(myCustomFont);
		fuelText.setTypeface(myCustomFont);

		LinearLayout outerFrame = (LinearLayout) view.findViewById(R.id.outerFrame);
		LinearLayout mapFrame = (LinearLayout) view.findViewById(R.id.minimapFrame);

		View btnT1 = view.findViewById(R.id.btnT1);
		View btnT2 = view.findViewById(R.id.btnT2);
		View btnT3 = view.findViewById(R.id.btnT3);
		View btnT4 = view.findViewById(R.id.btnT4);
		View btnT5 = view.findViewById(R.id.btnT5);
		View btnT6 = view.findViewById(R.id.btnT6);

		View btnU1 = view.findViewById(R.id.btnU1);
		View btnU2 = view.findViewById(R.id.btnU2);
		View btnU3 = view.findViewById(R.id.btnU3);


		// Get strikebase model name and find relevant resources
		String modeSuffix = strikeBaseModel.toString().toLowerCase();

		int frameResID = getResources().getIdentifier("frame_retro_" + modeSuffix, "drawable", getActivity().getPackageName());
		int mapFrameResID = getResources().getIdentifier("frame_minimap_" + modeSuffix, "drawable", getActivity().getPackageName());
		int buttonResId = getResources().getIdentifier("btn_retro_" + modeSuffix, "drawable", getActivity().getPackageName());
		int buttonFillResId = getResources().getIdentifier("btn_retro_fill_" + modeSuffix, "drawable", getActivity().getPackageName());
		int buttonUpgradeResId = getResources().getIdentifier("btn_retro_canv_" + modeSuffix, "drawable", getActivity()
				.getPackageName());

		outerFrame.setBackgroundResource(frameResID);
		mapFrame.setBackgroundResource(mapFrameResID);
		btnT1.setBackgroundResource(buttonFillResId);
		btnT2.setBackgroundResource(buttonFillResId);
		btnT3.setBackgroundResource(buttonFillResId);
		btnT4.setBackgroundResource(buttonFillResId);
		btnT5.setBackgroundResource(buttonFillResId);
		btnT6.setBackgroundResource(buttonFillResId);

		btnU1.setBackgroundResource(buttonUpgradeResId);
		btnU2.setBackgroundResource(buttonUpgradeResId);
		btnU3.setBackgroundResource(buttonUpgradeResId);

		btnInventory.setBackgroundResource(buttonResId);
		btnMinimap.setBackgroundResource(buttonResId);
		// FINISHED Image Setup


		// Each strike base model has its own turret slots assignation.
		switch (strikeBaseModel) {
			case MK1:
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

			case MK2:
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

			case MK3:
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

			case MK4:
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

			case MK5:
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

	public void updateScrap(int scrap) {
		scrapText.setText(String.format("%04d", scrap));
	}

	public void updateFuel(float fuel) {
		fuelText.setText(String.format("%04d", MathUtils.round(fuel)));
	}

	Vector2 tmp = new Vector2();

	public void updateMiniMap(final Vector2 center, final int radius) {
		getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				try {

					tmp.set(center).scl(1f/ GameConfig.TILE_SIZE);

					int left = MathUtils.max(0, (int) tmp.x - radius);
					int right = MathUtils.min(GameConfig.MAP_SIZE, (int) tmp.x + radius);

					int bottom = MathUtils.min(GameConfig.MAP_SIZE, (int) tmp.y + radius);
					int top = MathUtils.max(0, (int) tmp.y - radius);

					BitmapRegionDecoder decoder = BitmapRegionDecoder.newInstance("/data/data/edu.ub.pis2016.pis16" +
									".strikecom/files/gameMap.png",
							true);
					Rect decodeRect = new Rect(top, left, bottom, right);

					BitmapDrawable bm = new BitmapDrawable(decoder.decodeRegion(decodeRect, null));
					//BitmapDrawable bm = new BitmapDrawable("/data/data/edu.ub.pis2016.pis16.strikecom/files/gameMap.png");
					btnMinimap.setBackground(bm);
					//btnMinimap.setRotation(-90);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void setInventoyText(String text) {
		btnInventory.setText(text);
	}
}
