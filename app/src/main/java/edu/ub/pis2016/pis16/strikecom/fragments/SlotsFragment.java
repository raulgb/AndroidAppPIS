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

	public void setNewItem(Item selectedItem) { this.newItem = selectedItem; }

	public void setTurretSelection(boolean turretIsSelected) { this.turretIsSelected = turretIsSelected; }

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
				dismiss();
				FragmentedGameActivity callingActivity = (FragmentedGameActivity) getActivity();
				if(turretIsSelected) {
					callingActivity.equipTurret((TurretItem) newItem, selectedSlot);
				} else {
					callingActivity.equipUpgrade((UpgradeItem) newItem, selectedSlot);
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

		// Turret slots assignations change between strike base models
		switch(strikeBaseModel) {
			case MKI:
				btnT1.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						selectedSlot = 0;
					}
				});

				btnT2.setBackgroundColor(Color.TRANSPARENT);
				btnT2.setEnabled(false);

				btnT3.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						selectedSlot = 1;
					}
				});

				btnT4.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						selectedSlot = 2;
					}
				});

				btnT5.setBackgroundColor(Color.TRANSPARENT);
				btnT5.setEnabled(false);

				btnT6.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						selectedSlot = 3;
					}
				});
				break;

			case MKII:
				btnT1.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						selectedSlot = 0;
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
					}
				});

				btnT5.setBackgroundColor(Color.TRANSPARENT);
				btnT5.setEnabled(false);

				btnT6.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						selectedSlot = 2;
					}
				});
				break;

			case MKIII:
				btnT1.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						selectedSlot = 0;
					}
				});

				btnT2.setBackgroundColor(Color.TRANSPARENT);
				btnT2.setEnabled(false);

				btnT3.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						selectedSlot = 1;
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
				btnT1.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						selectedSlot = 0;
					}
				});

				btnT2.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						selectedSlot = 1;
					}
				});

				btnT3.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						selectedSlot = 2;
					}
				});

				btnT4.setBackgroundColor(Color.TRANSPARENT);
				btnT4.setEnabled(false);

				btnT5.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						selectedSlot = 3;
					}
				});

				btnT6.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						selectedSlot = 4;
					}
				});
				break;

			case MKV:
				btnT1.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						selectedSlot = 0;
					}
				});

				btnT2.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						selectedSlot = 1;
					}
				});

				btnT3.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						selectedSlot = 2;
					}
				});

				btnT4.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						selectedSlot = 3;
					}
				});

				btnT5.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						selectedSlot = 4;
					}
				});

				btnT6.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						selectedSlot = 5;
					}
				});
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

		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		return view;
	}

}