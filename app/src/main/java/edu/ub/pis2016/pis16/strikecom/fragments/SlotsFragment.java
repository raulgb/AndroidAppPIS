package edu.ub.pis2016.pis16.strikecom.fragments;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import edu.ub.pis2016.pis16.strikecom.FragmentedGameActivity;
import edu.ub.pis2016.pis16.strikecom.R;
import edu.ub.pis2016.pis16.strikecom.StrikeComGLGame;
import edu.ub.pis2016.pis16.strikecom.gameplay.items.Item;

public class SlotsFragment extends DialogFragment {

	private StrikeComGLGame game;

	private Button equipToSlotBtn;
	private Button backToInventoryBtn;
	private TextView currentItemDesc;
	//private TextView newItemDesc;

	private Item newItem;
	private int selectedSlot = -1;
	//boolean isEquipped = false; //selected item is currently equipped

	public void setGame(StrikeComGLGame game) {
		this.game = game;
	}

	public void setNewItem(Item selectedItem) { this.newItem = selectedItem; }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.slots_dialog, container);

		currentItemDesc = (TextView) view.findViewById(R.id.currentItemDesc);

		// NEW ITEM ATTRIBUTES
		TextView newItemDesc = (TextView) view.findViewById(R.id.newItemDesc);
		newItemDesc.setText(newItem.getDisplay());

		// BUTTONS
		equipToSlotBtn = (Button) view.findViewById(R.id.slotsBtn_1);
		equipToSlotBtn.setEnabled(false);
		equipToSlotBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				dismiss();
				FragmentedGameActivity callingActivity = (FragmentedGameActivity) getActivity();
				callingActivity.equipItem(newItem, selectedSlot);
			}
		});
		backToInventoryBtn = (Button) view.findViewById(R.id.slotsBtn_2);
		backToInventoryBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				dismiss();
				FragmentedGameActivity callingActivity = (FragmentedGameActivity) getActivity();
				callingActivity.showInventoryDialog(-1);
			}
		});

		// SLOTS
		Button slotT1 = (Button) view.findViewById(R.id.slotT1);
		slotT1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				selectedSlot = 1;
				equipToSlotBtn.setEnabled(true);
			}
		});
		Button slotT2 = (Button) view.findViewById(R.id.slotT2);
		slotT2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				selectedSlot = 2;
				equipToSlotBtn.setEnabled(true);
			}
		});
		Button slotT3 = (Button) view.findViewById(R.id.slotT3);
		slotT3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				selectedSlot = 3;
				equipToSlotBtn.setEnabled(true);
			}
		});
		Button slotT4 = (Button) view.findViewById(R.id.slotT4);
		slotT4.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				selectedSlot = 4;
				equipToSlotBtn.setEnabled(true);
			}
		});
		Button slotT5 = (Button) view.findViewById(R.id.slotT5);
		slotT5.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				selectedSlot = 5;
				equipToSlotBtn.setEnabled(true);
			}
		});
		Button slotT6 = (Button) view.findViewById(R.id.slotT6);
		slotT6.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				selectedSlot = 6;
				equipToSlotBtn.setEnabled(true);
			}
		});
		Button slotU1 = (Button) view.findViewById(R.id.slotU1);
		slotU1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				selectedSlot = 7;
				equipToSlotBtn.setEnabled(true);
			}
		});
		Button slotU2 = (Button) view.findViewById(R.id.slotU2);
		slotU2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				selectedSlot = 8;
				equipToSlotBtn.setEnabled(true);
			}
		});
		Button slotU3 = (Button) view.findViewById(R.id.slotU3);
		slotU3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				selectedSlot = 9;
				equipToSlotBtn.setEnabled(true);
			}
		});

		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		return view;
	}
}