package com.musclehack.musclehack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.musclehack.musclehack.workouts.WorkoutManagerSingleton;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class Fragment2worklog_1subProg extends ListFragment {
	 
	public static String TAG_TEXT_WORKLOG = "textWorklog";
	protected ArrayList<HashMap<String, String>> texts;
	protected ListAdapter adapter;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.texts = new ArrayList<HashMap<String, String>>();
		List<String> subProgramNames = WorkoutManagerSingleton.getInstance().getAvailableSubProgramNames();
		for(String subProgramName:subProgramNames){
			HashMap<String, String> map = new HashMap<String, String>();
			map.put(TAG_TEXT_WORKLOG, subProgramName);
			this.texts.add(map);
		}

		this.adapter = new SimpleCustomableAdapter(this.getActivity(),
												this.texts,
												R.layout.fragment2worklog,
												new String[] { TAG_TEXT_WORKLOG },
												new int[] { R.id.textWorklog});
		setListAdapter(this.adapter);
		return super.onCreateView(inflater, container, savedInstanceState);
	
	}
	
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id){
		super.onListItemClick(l, v, position, id);
		ListFragment newFragment = new Fragment2worklog_2week();
		TextView textView = (TextView) v.findViewById(R.id.textWorklog); 
		String clickedText = textView.getText().toString();
		WorkoutManagerSingleton.getInstance().selectSubProgram(clickedText);

		FragmentTransaction transaction = getFragmentManager().beginTransaction();


		transaction.replace(this.getId(), newFragment);
		transaction.addToBackStack(null);

		transaction.commit();
	}
}
