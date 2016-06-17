package edu.ub.pis2016.pis16.strikecom.fragments;

import android.app.DialogFragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
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

	public static StrikeBaseConfig.Model strikeBaseModel = StrikeBaseConfig.Model.MK2;

	private Item newItem;
	private int selectedSlot = -1;
	private boolean turretIsSelected = true;

	TextView currentItemDesc;
	TextView newItemDesc;

	HashMap<Integer, TurretItem> equippedTurrets = new HashMap<>();
	HashMap<Integer, UpgradeItem> equippedUpgrades = new HashMap<>();

	HashMap<Integer, Button> turretAssign = new HashMap<>();
	HashMap<Integer, Button> upgradeAssign = new HashMap<>();

	public void setNewItem(Item selectedItem) {
		this.newItem = selectedItem;
	}

	public void setTurretSelection(boolean turretIsSelected) {
		this.turretIsSelected = turretIsSelected;
	}

	public void setEquippedTurrets(HashMap equippedTurrets) {
		this.equippedTurrets = equippedTurrets;
	}

	public void setEquippedUpgrades(HashMap equippedUpgrades) {
		this.equippedUpgrades = equippedUpgrades;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view;

		switch (strikeBaseModel) {
			case MK1:
				view = inflater.inflate(R.layout.fragment_slots_mk1, container, false);
				break;
			case MK2:
				view = inflater.inflate(R.layout.fragment_slots_mk2, container, false);
				break;
			case MK3:
				view = inflater.inflate(R.layout.fragment_slots_mk3, container, false);
				break;
			case MK4:
				view = inflater.inflate(R.layout.fragment_slots_mk4, container, false);
				break;
			case MK5:
				view = inflater.inflate(R.layout.fragment_slots_mk5, container, false);
				break;
			default:
				view = inflater.inflate(R.layout.fragment_slots_mk2, container, false);
		}

		final Typeface myCustomFont= Typeface.createFromAsset(getActivity().getAssets(), getString(R.string.game_font));

		((TextView) view.findViewById(R.id.currentItemLabel)).setTypeface(myCustomFont);
		((TextView) view.findViewById(R.id.newItemLabel)).setTypeface(myCustomFont);

		currentItemDesc = (TextView) view.findViewById(R.id.currentItemDesc);
		currentItemDesc.setTypeface(myCustomFont);

		// NEW ITEM ATTRIBUTES
		newItemDesc = (TextView) view.findViewById(R.id.newItemDesc);
		if (newItem != null)
			newItemDesc.setText(newItem.getDisplayText());
		newItemDesc.setTypeface(myCustomFont);

		// BUTTONS
		Button equipToSlotBtn = (Button) view.findViewById(R.id.slotsBtn_1);
		equipToSlotBtn.setTypeface(myCustomFont);
		equipToSlotBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (selectedSlot >= 0) {
					dismiss();
					FragmentedGameActivity callingActivity = (FragmentedGameActivity) getActivity();
					if (turretIsSelected) {
						callingActivity.equipTurret((TurretItem) newItem, selectedSlot);
					} else {
						callingActivity.equipUpgrade((UpgradeItem) newItem, selectedSlot);
					}
				}
			}
		});
		Button backToInventoryBtn = (Button) view.findViewById(R.id.slotsBtn_2);
		backToInventoryBtn.setTypeface(myCustomFont);
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

		if (turretIsSelected) {
			btnU1.setEnabled(false);
			btnU1.setBackgroundResource(R.drawable.btn_retro_act);
			btnU2.setEnabled(false);
			btnU2.setBackgroundResource(R.drawable.btn_retro_act);
			btnU3.setEnabled(false);
			btnU3.setBackgroundResource(R.drawable.btn_retro_act);
		} else {
//			btnT1.setEnabled(false);
//			btnT1.setBackgroundResource(R.drawable.btn_retro_act);
//			btnT2.setEnabled(false);
//			btnT2.setBackgroundResource(R.drawable.btn_retro_act);
//			btnT3.setEnabled(false);
//			btnT3.setBackgroundResource(R.drawable.btn_retro_act);
//			btnT4.setEnabled(false);
//			btnT4.setBackgroundResource(R.drawable.btn_retro_act);
//			btnT5.setEnabled(false);
//			btnT5.setBackgroundResource(R.drawable.btn_retro_act);
//			btnT6.setEnabled(false);
//			btnT6.setBackgroundResource(R.drawable.btn_retro_act);
		}

		// Turret slots assignations change between strike base models
		switch (strikeBaseModel) {
			case MK1:
				setButtonAsign(btnT1, 0);
				setButtonAsign(btnT2, 1);
				setButtonAsign(btnT3, 2);
				setButtonAsign(btnT4, 3);
				break;

			case MK2:
				setButtonAsign(btnT1, 0);
				setButtonAsign(btnT2, 1);
				setButtonAsign(btnT3, 2);
				break;

			case MK3:
				setButtonAsign(btnT1, 0);
				setButtonAsign(btnT2, 1);
				break;

			case MK4:
				setButtonAsign(btnT1, 0);
				setButtonAsign(btnT2, 1);
				setButtonAsign(btnT3, 2);
				setButtonAsign(btnT4, 3);
				setButtonAsign(btnT5, 4);
				break;

			case MK5:
				setButtonAsign(btnT1, 0);
				setButtonAsign(btnT2, 1);
				setButtonAsign(btnT3, 2);
				setButtonAsign(btnT4, 3);
				setButtonAsign(btnT5, 4);
				setButtonAsign(btnT6, 5);
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

	private void setButtonAsign(Button button, final int slot){
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				selectedSlot = slot;
				if (equippedTurrets.containsKey(slot)) {
					currentItemDesc.setText(equippedTurrets.get(slot).getDisplayText());
				} else {
					currentItemDesc.setText("");
				}
			}
		});
		turretAssign.put(slot, button);
	}

	private void drawEquippedTurrets(HashMap<Integer, Button> turretAssign) {
		int imageID;

		// Draw equipped turrets
		for (Integer key : turretAssign.keySet()) {
			Button slotBtn = turretAssign.get(key);
			if (equippedTurrets.containsKey(key)) {

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
			if (equippedUpgrades.containsKey(key)) {
				imageID = getResources().getIdentifier(equippedUpgrades.get(key).getImage(), "drawable", getActivity().getPackageName());
				Bitmap original = BitmapFactory.decodeResource(getResources(), imageID);
				Bitmap b = Bitmap.createScaledBitmap(original, 64, 64, false);
				Drawable d = new BitmapDrawable(getResources(), b);
				slotBtn.setCompoundDrawablesWithIntrinsicBounds(d, null, null, null);
			}
		}
	}

}