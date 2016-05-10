package edu.ub.pis2016.pis16.strikecom.fragments;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import edu.ub.pis2016.pis16.strikecom.FragmentedGameActivity;
import edu.ub.pis2016.pis16.strikecom.R;
import edu.ub.pis2016.pis16.strikecom.StrikeComGLGame;
import edu.ub.pis2016.pis16.strikecom.gameplay.InventoryItemAdapter;
import edu.ub.pis2016.pis16.strikecom.gameplay.items.Inventory;
import edu.ub.pis2016.pis16.strikecom.gameplay.items.Item;

public class InventoryFragment extends DialogFragment {

	protected StrikeComGLGame game;

	protected Inventory inventory;

	protected Button equipBtn;
	protected Button cancelBtn;
	protected ListView itemList;
	protected TextView itemDesc;

	protected int selectedItem;
	protected int selectedSlot = -1;
	//boolean isEquipped = false; //selected item is currently equipped

	public void setGame(StrikeComGLGame game) {
		this.game = game;
	}

	public void setInventory(Inventory inventory) { this.inventory = inventory; }

	public void setSelectedSlot(int selectedSlot) { this.selectedSlot = selectedSlot; }

	public Item getSelectedItem() {
		if (selectedItem >= 0){
			return inventory.getItem(selectedItem);
		}
		return null;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.inventory, container);

		equipBtn = (Button) view.findViewById(R.id.inventoryBtn_1);
		cancelBtn = (Button) view.findViewById(R.id.inventoryBtn_2);
		itemList = (ListView) view.findViewById(R.id.itemList);
		itemDesc = (TextView) view.findViewById(R.id.itemDesc);

		initButtons();
		initItemList();

		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		return view;
	}

	protected void initButtons(){
		equipBtn.setEnabled(false);
		equipBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				//Item item = inventory.getItem(selectedItem);
				//equipItem((TurretItem)inventory.getItem(selectedItem));

				dismiss();
				FragmentedGameActivity callingActivity = (FragmentedGameActivity) getActivity();
				callingActivity.showSlotsDialog( inventory.getItem(selectedItem) );
			}
		});

		cancelBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				dismiss();
			}
		});
	}

	protected void initItemList(){
		itemList.setAdapter(new InventoryItemAdapter(getActivity(), inventory.getInventory()));
		itemList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
				if(i>=0){ //valid item is selected
					selectedItem = i;
					itemDesc.setText( inventory.getItem(selectedItem).getDisplay() );
					equipBtn.setEnabled(true);

				} else {
					equipBtn.setEnabled(false);
				}
			}
		});
	}
}
