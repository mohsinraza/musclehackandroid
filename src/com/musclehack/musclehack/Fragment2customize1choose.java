package com.musclehack.musclehack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.musclehack.musclehack.workouts.WorkoutManagerSingleton;

public class Fragment2customize1choose extends ListFragment {
	public static String TAG_TEXT_WORKLOG = "textCustomize";
	protected ArrayList<HashMap<String, String>> texts;
	protected ListAdapter adapter;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.d("Fragment2customize1choose", "public View onCreateView(...) called");
		this.texts = new ArrayList<HashMap<String, String>>();
		List<String> choices
		= WorkoutManagerSingleton.getInstance()
		.getAvailableProgramNamesToCustomize();
		for(String choice:choices){
			HashMap<String, String> map = new HashMap<String, String>();
			map.put(TAG_TEXT_WORKLOG, choice);
			this.texts.add(map);
		}

		this.adapter = new SimpleWeekAdapter(this.getActivity(),
												this.texts,
												R.layout.fragment2worklog,
												new String[] { TAG_TEXT_WORKLOG },
												new int[] { R.id.textWorklog});
		setListAdapter(this.adapter);
		WorkoutManagerSingleton.getInstance().setLevelChoice(11); // it will go from 11 to more for this branch
		View view = super.onCreateView(inflater, container, savedInstanceState);
		Log.d("Fragment2customize1choose", "public View onCreateView(...) end");
		return view;
	}
	
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id){
		Log.d("Fragment2customize1choose", "public void onListItemClick(...) called");
		super.onListItemClick(l, v, position, id);
		TextView textView = (TextView) v.findViewById(R.id.textWorklog); 
		String clickedText = textView.getText().toString();
		Fragment newFragment = null;
		if(clickedText.equals(WorkoutManagerSingleton.NEW_PROGRAM)){
			newFragment = new Fragment2customize2name();
		}else if(clickedText.equals(WorkoutManagerSingleton.NEW_PROGRAM_FROM_EXISTING)){
			Fragment2customize2name fragment = new Fragment2customize2name();
			fragment.enableExistingProgramMode();
			newFragment = fragment;
		}else if(clickedText.equals(WorkoutManagerSingleton.DELETE_AN_EXISTING_ONE)){
			Fragment2customize2delete fragment = new Fragment2customize2delete();
			newFragment = fragment;
		}else{
			WorkoutManagerSingleton workoutManager = WorkoutManagerSingleton.getInstance();
			workoutManager.selectProgram(clickedText);
			workoutManager.selectFistWeek();
			newFragment = new Fragment2customize3day();
		
		}
		FragmentTransaction transaction
			= getFragmentManager().beginTransaction();

		transaction.replace(this.getId(), newFragment);
		transaction.addToBackStack("customization");

		transaction.commit();
		Log.d("Fragment2customize1choose", "public void onListItemClick(...) end");
	}
}
