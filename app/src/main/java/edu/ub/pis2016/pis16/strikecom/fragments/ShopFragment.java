package edu.ub.pis2016.pis16.strikecom.fragments;

import android.view.View;
import android.widget.AdapterView;

import edu.ub.pis2016.pis16.strikecom.R;
import edu.ub.pis2016.pis16.strikecom.gameplay.InventoryItemAdapter;

public class ShopFragment extends InventoryFragment {

	private float playerScrap;

	public void setPlayerScrap(float playerScrap) { this.playerScrap = playerScrap; }

	@Override
	protected void configButtons(){
		equipBtn.setText(getString(R.string.buy_button));
		equipBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				dismiss();
			}
		});

		cancelBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				dismiss();
			}
		});
	}

	@Override
	protected void fillItemList(){
		if(turretIsSelected) {
			itemList.setAdapter(new InventoryItemAdapter(getActivity(), inventory.getTurretInventory()));
		} else {
			itemList.setAdapter(new InventoryItemAdapter(getActivity(), inventory.getUpgradeInventory()));
		}

		itemList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
				if(i>=0){ //valid item is selected
					selectedItem = i;
					String desc;
					if(turretIsSelected) {
						desc = inventory.getTurret(selectedItem).getDisplay() + "\n\n" + getString(R.string.player_scrap) + Float.toString(playerScrap);
					} else {
						desc = inventory.getTurret(selectedItem).getDisplay() + "\n\n" + getString(R.string.player_scrap) + Float.toString(playerScrap);
					}
					itemDesc.setText( desc );
				}
			}
		});
	}
}
