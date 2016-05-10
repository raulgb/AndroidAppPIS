package edu.ub.pis2016.pis16.strikecom.fragments;

import android.view.View;
import android.widget.AdapterView;

import edu.ub.pis2016.pis16.strikecom.R;
import edu.ub.pis2016.pis16.strikecom.gameplay.InventoryItemAdapter;

public class ShopFragment extends InventoryFragment {

	private float playerScrap;

	public void setPlayerScrap(float playerScrap) { this.playerScrap = playerScrap; }

	@Override
	protected void initButtons(){
		equipBtn.setEnabled(false);
		equipBtn.setText(getString(R.string.buy_button));
		equipBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				//Item item = inventory.getItem(selectedItem);
				//equipItem((TurretItem)inventory.getItem(selectedItem));

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
	protected void initItemList(){
		itemList.setAdapter(new InventoryItemAdapter(getActivity(), inventory.getInventory()));
		itemList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
				if(i>=0){ //valid item is selected
					selectedItem = i;
					String desc = inventory.getItem(selectedItem).getDisplay() + "\n\n" + getString(R.string.player_scrap) + Float
							.toString(playerScrap);
					itemDesc.setText( desc );
					equipBtn.setEnabled(true);

				} else {
					equipBtn.setEnabled(false);
				}
			}
		});
	}
}
