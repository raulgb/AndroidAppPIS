package edu.ub.pis2016.pis16.strikecom.fragments;

import android.app.DialogFragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import edu.ub.pis2016.pis16.strikecom.FragmentedGameActivity;
import edu.ub.pis2016.pis16.strikecom.R;
import edu.ub.pis2016.pis16.strikecom.gameplay.config.StrikeBaseConfig;
import edu.ub.pis2016.pis16.strikecom.gameplay.items.Item;

public class SlotsFragment extends DialogFragment {

	StrikeBaseConfig.Model strikeBaseModel = StrikeBaseConfig.Model.MKII;

	private Button equipToSlotBtn;

	private Item newItem;
	private int selectedSlot = -1;

	public void setStrikeBaseModel(StrikeBaseConfig.Model strikeBaseModel) {this.strikeBaseModel = strikeBaseModel; }

	public void setNewItem(Item selectedItem) { this.newItem = selectedItem; }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.slots_dialog, container);

		TextView currentItemDesc = (TextView) view.findViewById(R.id.currentItemDesc);

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
		Button backToInventoryBtn = (Button) view.findViewById(R.id.slotsBtn_2);
		backToInventoryBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				dismiss();
				FragmentedGameActivity callingActivity = (FragmentedGameActivity) getActivity();
				callingActivity.showInventoryDialog(-1);
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

		// Turret slots assignations change between strike base models
		switch(strikeBaseModel) {
			case MKI:
				btnT1.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						selectedSlot = 0;
						equipToSlotBtn.setEnabled(true);
					}
				});

				btnT2.setBackgroundColor(Color.TRANSPARENT);
				btnT2.setEnabled(false);

				btnT3.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						selectedSlot = 1;
						equipToSlotBtn.setEnabled(true);
					}
				});

				btnT4.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						selectedSlot = 2;
						equipToSlotBtn.setEnabled(true);
					}
				});

				btnT5.setBackgroundColor(Color.TRANSPARENT);
				btnT5.setEnabled(false);

				btnT6.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						selectedSlot = 3;
						equipToSlotBtn.setEnabled(true);
					}
				});
				break;

			case MKII:
				btnT1.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						selectedSlot = 0;
						equipToSlotBtn.setEnabled(true);
					}
				});

				btnT2.setBackgroundColor(Color.TRANSPARENT);
				btnT2.setEnabled(false);

				btnT3.setBackgroundColor(Color.TRANSPARENT);
				btnT3.setEnabled(false);

				btnT4.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						selectedSlot = 1;
						equipToSlotBtn.setEnabled(true);
					}
				});

				btnT5.setBackgroundColor(Color.TRANSPARENT);
				btnT5.setEnabled(false);

				btnT6.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						selectedSlot = 2;
						equipToSlotBtn.setEnabled(true);
					}
				});
				break;
		}

		btnU1.setEnabled(false);
		btnU1.setBackgroundResource(R.drawable.btn_retro_act);
		btnU2.setEnabled(false);
		btnU2.setBackgroundResource(R.drawable.btn_retro_act);
		btnU3.setEnabled(false);
		btnU3.setBackgroundResource(R.drawable.btn_retro_act);

		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		return view;
	}
}