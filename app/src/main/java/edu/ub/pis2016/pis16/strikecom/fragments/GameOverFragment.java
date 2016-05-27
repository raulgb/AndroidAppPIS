package edu.ub.pis2016.pis16.strikecom.fragments;

import android.app.DialogFragment;
import android.graphics.Typeface;
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

public class GameOverFragment extends DialogFragment{
	public static StrikeBaseConfig.Model strikeBaseModel = StrikeBaseConfig.Model.MK2;

	private int score;

	public void setScore(int score) {
		this.score = score;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_gameover, container);

		Typeface myCustomFont= Typeface.createFromAsset(getActivity().getAssets(), "fonts/Minecraft.ttf");

		Button btnExit  = (Button) view.findViewById(R.id.btnExit);
		Button btnSubmit  = (Button) view.findViewById(R.id.btnSubmit);
		TextView title = (TextView) view.findViewById(R.id.title);
		TextView textScore = (TextView) view.findViewById(R.id.textScore);
		TextView textLabel = (TextView) view.findViewById(R.id.textLabel);

		btnExit.setTypeface(myCustomFont);
		btnSubmit.setTypeface(myCustomFont);
		title.setTypeface(myCustomFont);
		textScore.setTypeface(myCustomFont);
		textLabel.setTypeface(myCustomFont);

		switch (strikeBaseModel) {
			case MK1:
				view.setBackgroundResource(R.drawable.frame_retro_mk1);
				btnExit.setBackgroundResource(R.drawable.btn_retro_canv_mk1);
				btnSubmit.setBackgroundResource(R.drawable.btn_retro_canv_mk1);
				break;
			case MK2:
				view.setBackgroundResource(R.drawable.frame_retro_mk2);
				btnExit.setBackgroundResource(R.drawable.btn_retro_canv_mk2);
				btnSubmit.setBackgroundResource(R.drawable.btn_retro_canv_mk2);
				break;
			case MK3:
				view.setBackgroundResource(R.drawable.frame_retro_mk3);
				btnExit.setBackgroundResource(R.drawable.btn_retro_canv_mk3);
				btnSubmit.setBackgroundResource(R.drawable.btn_retro_canv_mk3);
				break;
			case MK4:
				view.setBackgroundResource(R.drawable.frame_retro_mk4);
				btnExit.setBackgroundResource(R.drawable.btn_retro_canv_mk4);
				btnSubmit.setBackgroundResource(R.drawable.btn_retro_canv_mk4);
				break;
			case MK5:
				view.setBackgroundResource(R.drawable.frame_retro_mk5);
				btnExit.setBackgroundResource(R.drawable.btn_retro_canv_mk5);
				btnSubmit.setBackgroundResource(R.drawable.btn_retro_canv_mk5);
				break;
		}

		textScore.setText(Integer.toString(score));

		btnExit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				((FragmentedGameActivity)getActivity()).backToMainMenu();
			}
		});

		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		return view;
	}
}
