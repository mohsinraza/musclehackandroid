package com.musclehack.musclehack;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Fragment2customize2name extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
								Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.create_workout,
				container, false);
		//TODO Here I add the button listener
		return view;
	}
}
