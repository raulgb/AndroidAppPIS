package edu.ub.pis2016.pis16.strikecom.fragments;

import android.app.DialogFragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;

import edu.ub.pis2016.pis16.strikecom.FragmentedGameActivity;
import edu.ub.pis2016.pis16.strikecom.R;
import edu.ub.pis2016.pis16.strikecom.gameplay.config.StrikeBaseConfig;
import edu.ub.pis2016.pis16.strikecom.gameplay.items.Item;
import edu.ub.pis2016.pis16.strikecom.gameplay.items.TurretItem;
import edu.ub.pis2016.pis16.strikecom.gameplay.items.UpgradeItem;

public class SlotsFragment extends DialogFragment {

	public static StrikeBaseConfig.Model strikeBaseModel = StrikeBaseConfig.Model.MKII;

	private Item newItem;
	private int selectedSlot = -1;
	private boolean turretIsSelected = true;

	HashMap<Integer, TurretItem> equippedTurrets = new HashMap<>();
	HashMap<Integer, UpgradeItem> equippedUpgrades = new HashMap<>();

	public void setNewItem(Item selectedItem) { this.newItem = selectedItem; }

	public void setTurretSelection(boolean turretIsSelected) { this.turretIsSelected = turretIsSelected; }

	public void setEquippedTurrets(HashMap equippedTurrets) { this.equippedTurrets = equippedTurrets; }

	public void setEquippedUpgrades(HashMap equippedUpgrades) { this.equippedUpgrades = equippedUpgrades; }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view;
		switch (strikeBaseModel) {
			case MKI:
				view = inflater.inflate(R.layout.fragment_slots_mk1, container, false);
				break;
			case MKII:
				view = inflater.inflate(R.layout.fragment_slots_mk2, container, false);
				break;
			case MKIII:
				view = inflater.inflate(R.layout.fragment_slots_mk3, container, false);
				break;
			case MKIV:
				view = inflater.inflate(R.layout.fragment_slots_mk4, container, false);
				break;
			default:
				view = inflater.inflate(R.layout.fragment_slots_mk2, container, false);
		}

		final TextView currentItemDesc = (TextView) view.findViewById(R.id.currentItemDesc);

		// NEW ITEM ATTRIBUTES
		TextView newItemDesc = (TextView) view.findViewById(R.id.newItemDesc);
		newItemDesc.setText(newItem.getDisplay());

		// BUTTONS
		Button equipToSlotBtn = (Button) view.findViewById(R.id.slotsBtn_1);
		equipToSlotBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (selectedSlot >= 0) {
					dismiss();
					FragmentedGameActivity callingActivity = (FragmentedGameActivity) getActivity();
					if(turretIsSelected) {
						callingActivity.equipTurret((TurretItem) newItem, selectedSlot);
					} else {
						callingActivity.equipUpgrade((UpgradeItem) newItem, selectedSlot);
					}
				}
			}
		});
		Button backToInventoryBtn = (Button) view.findViewById(R.id.slotsBtn_2);
		backToInventoryBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				dismiss();
				FragmentedGameActivity callingActivity = (FragmentedGameActivity) getActivity();
				callingActivity.showInventoryDialog(-1, turretIsSelected, true);
			}
		});

		// SLOTS
		Button btnT1 = (Button) view.findViewById(R.id.slotT1);
		Button btnT2 = (Button) view.findViewById(R.id.slotT2);
		Button btnT3 = (Button) view.findViewById(R.id.slotT3);
		Button btnT4 = (Button) view.findViewById(R.id.slotT4);
		Button btnT5 = (Button) view.findViewById(R.id.slotT5);
		Button btnT6 = (Button) view.findViewById(R.id.slotT6);
		Button btnU1 = (Button) view.findViewById(R.id.slotU1);
		Button btnU2 = (Button) view.findViewById(R.id.slotU2);
		Button btnU3 = (Button) view.findViewById(R.id.slotU3);

		if(turretIsSelected) {
			btnU1.setEnabled(false);
			btnU1.setBackgroundResource(R.drawable.btn_retro_act);
			btnU2.setEnabled(false);
			btnU2.setBackgroundResource(R.drawable.btn_retro_act);
			btnU3.setEnabled(false);
			btnU3.setBackgroundResource(R.drawable.btn_retro_act);
		} else {
			btnT1.setEnabled(false);
			btnT1.setBackgroundResource(R.drawable.btn_retro_act);
			btnT2.setEnabled(false);
			btnT2.setBackgroundResource(R.drawable.btn_retro_act);
			btnT3.setEnabled(false);
			btnT3.setBackgroundResource(R.drawable.btn_retro_act);
			btnT4.setEnabled(false);
			btnT4.setBackgroundResource(R.drawable.btn_retro_act);
			btnT5.setEnabled(false);
			btnT5.setBackgroundResource(R.drawable.btn_retro_act);
			btnT6.setEnabled(false);
			btnT6.setBackgroundResource(R.drawable.btn_retro_act);
		}

		HashMap<Integer, Button> turretAssign = new HashMap<>();
		HashMap<Integer, Button> upgradeAssign = new HashMap<>();


		// Turret slots assignations change between strike base models
		switch(strikeBaseModel) {
			case MKI:
				btnT1.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						selectedSlot = 0;
						if (equippedTurrets.containsKey(0)) {
							currentItemDesc.setText(equippedTurrets.get(0).getDisplay());
						} else {
							currentItemDesc.setText("");
						}
					}
				});
				turretAssign.put(0, btnT1);

				btnT2.setBackgroundColor(Color.TRANSPARENT);
				btnT2.setEnabled(false);

				btnT3.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						selectedSlot = 1;
						if (equippedTurrets.containsKey(1)) {
							currentItemDesc.setText(equippedTurrets.get(1).getDisplay());
						} else {
							currentItemDesc.setText("");
						}
					}
				});
				turretAssign.put(1, btnT3);


				btnT4.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						selectedSlot = 2;
						if (equippedTurrets.containsKey(2)) {
							currentItemDesc.setText(equippedTurrets.get(2).getDisplay());
						} else {
							currentItemDesc.setText("");
						}
					}
				});
				turretAssign.put(2, btnT4);


				btnT5.setBackgroundColor(Color.TRANSPARENT);
				btnT5.setEnabled(false);

				btnT6.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						selectedSlot = 3;
						if (equippedTurrets.containsKey(3)) {
							currentItemDesc.setText(equippedTurrets.get(3).getDisplay());
						} else {
							currentItemDesc.setText("");
						}
					}
				});
				turretAssign.put(3, btnT6);
				break;

			case MKII:
				btnT1.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						selectedSlot = 0;
						if (equippedTurrets.containsKey(0)) {
							currentItemDesc.setText(equippedTurrets.get(0).getDisplay());
						} else {
							currentItemDesc.setText("");
						}
					}
				});
				turretAssign.put(0, btnT1);

				btnT2.setBackgroundColor(Color.TRANSPARENT);
				btnT2.setEnabled(false);

				btnT3.setBackgroundColor(Color.TRANSPARENT);
				btnT3.setEnabled(false);

				btnT4.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						selectedSlot = 1;
						if (equippedTurrets.containsKey(1)) {
							currentItemDesc.setText(equippedTurrets.get(1).getDisplay());
						} else {
							currentItemDesc.setText("");
						}
					}
				});
				turretAssign.put(1, btnT4);

				btnT5.setBackgroundColor(Color.TRANSPARENT);
				btnT5.setEnabled(false);

				btnT6.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						selectedSlot = 2;
						if (equippedTurrets.containsKey(2)) {
							currentItemDesc.setText(equippedTurrets.get(2).getDisplay());
						} else {
							currentItemDesc.setText("");
						}
					}
				});
				turretAssign.put(2, btnT6);
				break;

			case MKIII:
				btnT1.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						selectedSlot = 0;
						if (equippedTurrets.containsKey(0)) {
							currentItemDesc.setText(equippedTurrets.get(0).getDisplay());
						} else {
							currentItemDesc.setText("");
						}
					}
				});
				turretAssign.put(0, btnT1);

				btnT2.setBackgroundColor(Color.TRANSPARENT);
				btnT2.setEnabled(false);

				btnT3.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						selectedSlot = 1;
						if (equippedTurrets.containsKey(1)) {
							currentItemDesc.setText(equippedTurrets.get(1).getDisplay());
						} else {
							currentItemDesc.setText("");
						}
					}
				});
				turretAssign.put(1, btnT3);

				btnT4.setBackgroundColor(Color.TRANSPARENT);
				btnT4.setEnabled(false);

				btnT5.setBackgroundColor(Color.TRANSPARENT);
				btnT5.setEnabled(false);

				btnT6.setBackgroundColor(Color.TRANSPARENT);
				btnT6.setEnabled(false);
				break;

			case MKIV:
				btnT1.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						selectedSlot = 0;
						if (equippedTurrets.containsKey(0)) {
							currentItemDesc.setText(equippedTurrets.get(0).getDisplay());
						} else {
							currentItemDesc.setText("");
						}
					}
				});
				turretAssign.put(0, btnT1);

				btnT2.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						selectedSlot = 1;
						if (equippedTurrets.containsKey(1)) {
							currentItemDesc.setText(equippedTurrets.get(1).getDisplay());
						} else {
							currentItemDesc.setText("");
						}
					}
				});
				turretAssign.put(1, btnT2);

				btnT3.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						selectedSlot = 2;
						if (equippedTurrets.containsKey(2)) {
							currentItemDesc.setText(equippedTurrets.get(2).getDisplay());
						} else {
							currentItemDesc.setText("");
						}
					}
				});
				turretAssign.put(2, btnT3);

				btnT4.setBackgroundColor(Color.TRANSPARENT);
				btnT4.setEnabled(false);

				btnT5.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						selectedSlot = 3;
						if (equippedTurrets.containsKey(3)) {
							currentItemDesc.setText(equippedTurrets.get(3).getDisplay());
						} else {
							currentItemDesc.setText("");
						}
					}
				});
				turretAssign.put(3, btnT5);

				btnT6.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						selectedSlot = 4;
						if (equippedTurrets.containsKey(4)) {
							currentItemDesc.setText(equippedTurrets.get(4).getDisplay());
						} else {
							currentItemDesc.setText("");
						}
					}
				});
				turretAssign.put(4, btnT6);
				break;

			case MKV:
				btnT1.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						selectedSlot = 0;
						if (equippedTurrets.containsKey(0)) {
							currentItemDesc.setText(equippedTurrets.get(0).getDisplay());
						} else {
							currentItemDesc.setText("");
						}
					}
				});
				turretAssign.put(0, btnT1);

				btnT2.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						selectedSlot = 1;
						if (equippedTurrets.containsKey(1)) {
							currentItemDesc.setText(equippedTurrets.get(1).getDisplay());
						} else {
							currentItemDesc.setText("");
						}
					}
				});
				turretAssign.put(1, btnT2);

				btnT3.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						selectedSlot = 2;
						if (equippedTurrets.containsKey(2)) {
							currentItemDesc.setText(equippedTurrets.get(2).getDisplay());
						} else {
							currentItemDesc.setText("");
						}
					}
				});
				turretAssign.put(2, btnT3);

				btnT4.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						selectedSlot = 3;
						if (equippedTurrets.containsKey(3)) {
							currentItemDesc.setText(equippedTurrets.get(3).getDisplay());
						} else {
							currentItemDesc.setText("");
						}
					}
				});
				turretAssign.put(3, btnT4);

				btnT5.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						selectedSlot = 4;
						if (equippedTurrets.containsKey(4)) {
							currentItemDesc.setText(equippedTurrets.get(4).getDisplay());
						} else {
							currentItemDesc.setText("");
						}
					}
				});
				turretAssign.put(4, btnT5);

				btnT6.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						selectedSlot = 5;
						if (equippedTurrets.containsKey(6)) {
							currentItemDesc.setText(equippedTurrets.get(6).getDisplay());
						} else {
							currentItemDesc.setText("");
						}
					}
				});
				turretAssign.put(5, btnT6);
				break;
		}

		// Assignations for the upgrade slots are the same regardless the configuration
		btnU1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				selectedSlot = 0;
			}
		});
		btnU2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				selectedSlot = 1;
			}
		});
		btnU3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				selectedSlot = 2;
			}
		});
		upgradeAssign.put(0, btnU1);
		upgradeAssign.put(1, btnU2);
		upgradeAssign.put(2, btnU3);

		if (turretIsSelected) {
			drawEquippedTurrets(turretAssign);
		} else {
			drawEquippedUpgrades(upgradeAssign);
		}

		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		return view;
	}

	private void drawEquippedTurrets(HashMap<Integer, Button> turretAssign) {
		int imageID;

		// Draw equipped turrets
		for (Integer key : turretAssign.keySet()) {
			Button slotBtn = turretAssign.get(key);
			if(equippedTurrets.containsKey(key)){

				imageID = getResources().getIdentifier(equippedTurrets.get(key).getImage(), "drawable", getActivity().getPackageName());
				Bitmap original = BitmapFactory.decodeResource(getResources(), imageID);
				Bitmap b = Bitmap.createScaledBitmap(original, 64, 64, false);
				Drawable d = new BitmapDrawable(getResources(), b);
				slotBtn.setCompoundDrawablesWithIntrinsicBounds(d, null, null, null);
			}
		}
	}

	private void drawEquippedUpgrades(HashMap<Integer, Button> upgradeAssign) {
		int imageID;

		// Draw equipped upgrades
		for (Integer key : upgradeAssign.keySet()) {
			Button slotBtn = upgradeAssign.get(key);
			if(equippedUpgrades.containsKey(key)){
				imageID = getResources().getIdentifier(equippedUpgrades.get(key).getImage(), "drawable", getActivity().getPackageName());
				Bitmap original = BitmapFactory.decodeResource(getResources(), imageID);
				Bitmap b = Bitmap.createScaledBitmap(original, 64, 64, false);
				Drawable d = new BitmapDrawable(getResources(), b);
				slotBtn.setCompoundDrawablesWithIntrinsicBounds(d, null, null, null);
			}
		}
	}

}