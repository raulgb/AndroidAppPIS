package edu.ub.pis2016.pis16.strikecom.fragments;
;
import android.view.View;
import android.widget.AdapterView;

import edu.ub.pis2016.pis16.strikecom.FragmentedGameActivity;
import edu.ub.pis2016.pis16.strikecom.R;
import edu.ub.pis2016.pis16.strikecom.gameplay.items.InventoryItemAdapter;
import edu.ub.pis2016.pis16.strikecom.gameplay.items.TurretItem;
import edu.ub.pis2016.pis16.strikecom.gameplay.items.UpgradeItem;

public class ShopFragment extends InventoryFragment {

	private String shopID;

	public void setId(String shopID) { this.shopID = shopID; }

	@Override
	protected void configButtons(){
		equipBtn.setText(getString(R.string.buy_button));
		equipBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if(selectedItem > -1){
					FragmentedGameActivity callingActivity = (FragmentedGameActivity) getActivity();
					int itemPrice;

					if(turretIsSelected) {
						TurretItem item = inventory.getTurret(selectedItem);
						itemPrice = item.getPrice();
						if(itemPrice <= playerScrap) {
							callingActivity.buyItem(shopID, item);
							fillItemList();
							playerScrap -= itemPrice;
							selectedItem = -1;
							itemDesc.setText("");
						}

					} else {
						UpgradeItem item = inventory.getUpgrade(selectedItem);
						itemPrice = item.getPrice();
						if(itemPrice <= playerScrap){
							callingActivity.buyItem(shopID, item);
							fillItemList();
							playerScrap -= itemPrice;
							selectedItem = -1;
							itemDesc.setText("");

							if(item.isFuel()){
								playerFuel += 250;
								fuelText.setText(Integer.toString(Math.round(playerFuel)));
							}
						}
					}
					scrapText.setText(Integer.toString(playerScrap));
				}

			}
		});

		cancelBtn.setText(getString(R.string.exit_button));
		cancelBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				getDialog().dismiss();
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
				//equipBtn.setText( getString(R.string.equip_button) );
				if(i>=0){ //valid item is selected
					selectedItem = i;

					if(turretIsSelected) {
						itemDesc.setText( inventory.getTurret(selectedItem).getDisplayText() );
					} else {
						itemDesc.setText( inventory.getUpgrade(selectedItem).getDisplayText() );
					}
				}
			}
		});
	}
}
