package edu.ub.pis2016.pis16.strikecom.gameplay;

import java.util.List;
import android.content.Context;
//import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import edu.ub.pis2016.pis16.strikecom.R;
import edu.ub.pis2016.pis16.strikecom.gameplay.items.Item;

public class InventoryItemAdapter extends BaseAdapter {

	private Context context;
	private List<Item> items;

	public InventoryItemAdapter(Context context, List<Item> items) {
		this.context = context;
		this.items = items;
	}

	@Override
	public int getCount() {
		return this.items.size();
	}

	@Override
	public Object getItem(int position) {
		return this.items.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View rowView = convertView;

		if (convertView == null) {
			// Create a new view into the list.
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			rowView = inflater.inflate(R.layout.list_item, parent, false);
		}

		// Set data into the view.
		ImageView itemImage = (ImageView) rowView.findViewById(R.id.itemImage);
		TextView itemName = (TextView) rowView.findViewById(R.id.itemName);

		Item item = this.items.get(position);
		itemName.setText(item.getName());
		//itemImage.setImageDrawable(Drawable.createFromPath(item.getImage()));

		return rowView;
	}

}