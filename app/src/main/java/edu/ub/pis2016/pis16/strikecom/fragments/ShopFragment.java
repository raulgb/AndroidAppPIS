package edu.ub.pis2016.pis16.strikecom.fragments;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.ub.pis2016.pis16.strikecom.StrikeComGLGame;

public class ShopFragment extends DialogFragment {

	private StrikeComGLGame game;

	public void setGame(StrikeComGLGame game) {
		this.game = game;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return super.onCreateView(inflater, container, savedInstanceState);
	}
}
