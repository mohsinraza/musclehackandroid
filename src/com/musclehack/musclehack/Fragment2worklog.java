package com.musclehack.musclehack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.musclehack.musclehack.workouts.WorkoutManagerSingleton;

public class Fragment2worklog extends ListFragment {
	 
	public static String TAG_TEXT_WORKLOG = "textWorklog";
	protected ArrayList<HashMap<String, String>> texts;
	protected ListAdapter adapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		//*
		Context context = this.getActivity().getApplicationContext();
		WorkoutManagerSingleton.setContext(context);

		this.texts = new ArrayList<HashMap<String, String>>();
		List<String> programNames = WorkoutManagerSingleton.getInstance().getAvailableProgramNames();
		for(String programName:programNames){
			HashMap<String, String> map = new HashMap<String, String>();
			map.put(TAG_TEXT_WORKLOG, programName);
			this.texts.add(map);
		}


		this.adapter = new SimpleCustomableAdapter(this.getActivity(),
												this.texts,
												R.layout.fragment2worklog,
												new String[] { TAG_TEXT_WORKLOG },
												new int[] { R.id.textWorklog});
		setListAdapter(this.adapter);
		/**/
 
		
		
		return super.onCreateView(inflater, container, savedInstanceState);
	
	}
	
	
	
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id){
		super.onListItemClick(l, v, position, id);
		ListFragment newFragment = new Fragment2worklog_1prog();
		TextView textView = (TextView) v.findViewById(R.id.textWorklog); 
		String clickedText = textView.getText().toString();
		WorkoutManagerSingleton.getInstance().selectProgram(clickedText);

		FragmentTransaction transaction = getFragmentManager().beginTransaction();




		transaction.replace(this.getId(), newFragment);
		transaction.addToBackStack(null);

		transaction.commit();
	}
}
