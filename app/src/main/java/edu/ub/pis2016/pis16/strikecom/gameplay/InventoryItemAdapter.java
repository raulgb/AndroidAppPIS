package edu.ub.pis2016.pis16.strikecom.gameplay;

import java.util.List;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import edu.ub.pis2016.pis16.strikecom.R;
import edu.ub.pis2016.pis16.strikecom.gameplay.config.StrikeBaseConfig;
import edu.ub.pis2016.pis16.strikecom.gameplay.items.Item;

public class InventoryItemAdapter extends BaseAdapter {

	public static StrikeBaseConfig.Model strikeBaseModel = StrikeBaseConfig.Model.MKII;

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
			switch (strikeBaseModel) {
				case MKI:
					rowView.setBackgroundResource(R.drawable.frame_retro_mk1);
					break;
				case MKII:
					rowView.setBackgroundResource(R.drawable.frame_retro_mk2);
					break;
				case MKIII:
					rowView.setBackgroundResource(R.drawable.frame_retro_mk3);
			}

		}

		// Set data into the view.
		ImageView itemImage = (ImageView) rowView.findViewById(R.id.itemImage);
		TextView itemName = (TextView) rowView.findViewById(R.id.itemName);

		Item item = this.items.get(position);
		itemName.setText(item.getName());

		int imageID = context.getResources().getIdentifier(item.getImage(), "drawable", context.getPackageName());
		if (imageID > 0) {
			// Item has an associated image in drawables
			itemImage.setImageResource(imageID);
		} else {
			itemImage.setImageResource(R.drawable.scrap_64);
		}

		return rowView;
	}

}