package com.musclehack.musclehack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.musclehack.musclehack.workouts.WorkoutManagerSingleton;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class Fragment2worklog_1prog extends ListFragment {
	 
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

		this.adapter = new SimpleHtmlAdapter(this.getActivity(),
												this.texts,
												R.layout.fragment2worklog_row1,
												new String[] { TAG_TEXT_WORKLOG },
												new int[] { R.id.textWorklog});
		setListAdapter(this.adapter);
		/**/
 
		
		
		return super.onCreateView(inflater, container, savedInstanceState);
	
	}
	
	
	
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id){
		super.onListItemClick(l, v, position, id);
	}
}
