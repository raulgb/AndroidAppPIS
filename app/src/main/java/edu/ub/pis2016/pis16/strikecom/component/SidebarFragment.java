package edu.ub.pis2016.pis16.strikecom.component;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import edu.ub.pis2016.pis16.strikecom.R;
import edu.ub.pis2016.pis16.strikecom.engine.opengl.GLGameFragment;


public class SidebarFragment extends Fragment {

	public GLGameFragment gameFrag;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
		//return super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.fragment_sidebar, container, false);

		Button btnInventory = (Button)view.findViewById(R.id.btnInventory);
		btnInventory.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

			}
		});

		return view;
	}

	public void setGameFrag(GLGameFragment gameFrag) {
		this.gameFrag = gameFrag;
	}
}
