package edu.ub.pis2016.pis16.strikecom.fragments;

import android.app.DialogFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
	private boolean modelUnlocked = false;

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

		modelUnlocked = unlock();
		btnExit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				((FragmentedGameActivity)getActivity()).backToMainMenu(modelUnlocked);
			}
		});

		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		return view;
	}

	private boolean unlock() {
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
		SharedPreferences.Editor editor = sharedPreferences.edit();

		boolean unlocked = false;
		/*
		if (score >= 1000) {
			if (!sharedPreferences.getBoolean("sbmk2", false))
				unlocked = true;
			editor.putBoolean("sbmk2", true);
		}
		*/
		if (score >= 2000) {
			if (!sharedPreferences.getBoolean("sbmk1", false))
				unlocked = true;
			editor.putBoolean("sbmk1", true);
		}
		if (score >= 5000) {
			if (!sharedPreferences.getBoolean("sbmk4", false))
				unlocked = true;
			editor.putBoolean("sbmk4", true);
		}
		if (score >= 10000) {
			if (!sharedPreferences.getBoolean("sbmk5", false))
				unlocked = true;
			editor.putBoolean("sbmk5", true);
		}
		if (unlocked) {
			editor.commit();
		}
		return unlocked;
	}

	//-------------------------------------------------------------------

	// Use this to lock strikebase models
	public static void lock(Context context) {
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = sharedPreferences.edit();

		editor.putBoolean("sbmk1", false);
		editor.putBoolean("sbmk2", false);
		editor.putBoolean("sbmk4", false);
		editor.putBoolean("sbmk5", false);
		editor.commit();
	}

	// Use this to unlock all strikebase models
	public static void unlockAll(Context context) {
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = sharedPreferences.edit();

		editor.putBoolean("sbmk1", true);
		editor.putBoolean("sbmk2", true);
		editor.putBoolean("sbmk4", true);
		editor.putBoolean("sbmk5", true);
		editor.commit();
	}
}
