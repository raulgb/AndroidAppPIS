package edu.ub.pis2016.pis16.strikecom.fragments;
;
import android.view.View;
import android.widget.AdapterView;

import edu.ub.pis2016.pis16.strikecom.FragmentedGameActivity;
import edu.ub.pis2016.pis16.strikecom.R;
import edu.ub.pis2016.pis16.strikecom.gameplay.InventoryItemAdapter;

public class ShopFragment extends InventoryFragment {

	@Override
	protected void configButtons(){
		equipBtn.setText(getString(R.string.buy_button));
		equipBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				dismiss();
				FragmentedGameActivity callingActivity = (FragmentedGameActivity) getActivity();
				if(turretIsSelected) {
					callingActivity.buyItem(inventory.getTurret(selectedItem));
				} else {
					callingActivity.buyItem(inventory.getUpgrade(selectedItem));
				}
			}
		});

		cancelBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				dismiss();
				FragmentedGameActivity callingActivity = (FragmentedGameActivity) getActivity();
				callingActivity.resumeGame();
			}
		});
	}

	@Override
	protected void fillItemList() {
		if(turretIsSelected) {
			itemList.setAdapter(new InventoryItemAdapter(getActivity(), inventory.getTurretInventory()));
		} else {
			itemList.setAdapter(new InventoryItemAdapter(getActivity(), inventory.getUpgradeInventory()));
		}

		itemList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
				equipBtn.setText( getString(R.string.equip_button) );
				if(i>=0){ //valid item is selected
					selectedItem = i;

					if(turretIsSelected) {
						itemDesc.setText( inventory.getTurret(selectedItem).getDisplay() );
					} else {
						itemDesc.setText( inventory.getUpgrade(selectedItem).getDisplay() );
					}
				}
			}
		});
	}
}
