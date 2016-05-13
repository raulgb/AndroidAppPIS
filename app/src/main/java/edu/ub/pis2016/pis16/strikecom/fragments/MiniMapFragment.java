package edu.ub.pis2016.pis16.strikecom.fragments;

import android.app.DialogFragment;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import edu.ub.pis2016.pis16.strikecom.R;

/**
 * Created by Alexander Bevzenko.
 * minimap dialogue fragment
 */
public class MiniMapFragment extends DialogFragment {

	protected Button cancelBtn;
	protected TextView mapDesc;
	protected ImageView mapImage;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_minimap, container);
		mapDesc = (TextView) view.findViewById(R.id.mapDesc); // description of the map
		cancelBtn = (Button) view.findViewById(R.id.mapBack);
		mapImage = (ImageView) view.findViewById(R.id.mapImageView);
		configButtons();

		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		return view;

	}
	protected void configButtons(){
		// Cancel button
		cancelBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				dismiss();
			}
		});
	}

}
