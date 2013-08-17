package com.musclehack.musclehack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.musclehack.musclehack.workouts.WorkoutManagerSingleton;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AbsListView.RecyclerListener;

public class Fragment2worklog_2week extends ListFragment {
	public static String TAG_TEXT_WORKLOG = "textWorklog";
	protected ArrayList<HashMap<String, String>> texts;
	protected ListAdapter adapter;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.d("Fragment2worklog_2week", "public View onCreateView(...) called");
		this.texts = new ArrayList<HashMap<String, String>>();
		List<String> weekNames = WorkoutManagerSingleton.getInstance().getAvailableWeeks();
		for(String weekName:weekNames){
			HashMap<String, String> map = new HashMap<String, String>();
			map.put(TAG_TEXT_WORKLOG, weekName);
			this.texts.add(map);
		}

		this.adapter = new SimpleWeekAdapter(this.getActivity(),
												this.texts,
												R.layout.fragment2worklog,
												new String[] { TAG_TEXT_WORKLOG },
												new int[] { R.id.textWorklog});
		setListAdapter(this.adapter);
		WorkoutManagerSingleton.getInstance().setLevelChoice(1);
		View view = super.onCreateView(inflater, container, savedInstanceState);
		Log.d("Fragment2worklog_2week", "public View onCreateView(...) end");
		return view;
	}
	
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id){
		Log.d("Fragment2worklog_2week", "public void onListItemClick(...) called");
		super.onListItemClick(l, v, position, id);
		ListFragment newFragment = new Fragment2worklog_3day();
		TextView textView = (TextView) v.findViewById(R.id.textWorklog); 
		String clickedText = textView.getText().toString();
		WorkoutManagerSingleton.getInstance().selectWeek(clickedText);
		FragmentTransaction transaction = getFragmentManager().beginTransaction();

		transaction.replace(this.getId(), newFragment);
		//transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		transaction.addToBackStack("weekToDay");

		transaction.commit();
		Log.d("Fragment2worklog_2week", "public void onListItemClick(...) end");
	}
	
	@Override
	public void onViewCreated(View viewTop, Bundle savedInstanceState){
		Log.d("Fragment2worklog_2week", "public void onViewCreated(...) called");
		super.onViewCreated(viewTop, savedInstanceState);
		this.getListView().setRecyclerListener(new RecyclerListener() {
			@Override
			public void onMovedToScrapHeap(View view) {
				Log.d("RecyclerListener 4exo", "public void onMovedToScrapHeap(...){ called");
				boolean visible
		    	= Fragment2worklog_2week.this.isVisible();
		    	if(visible){
					Log.d("RecyclerListener 4exo", "visible");
					ListView listView = Fragment2worklog_2week.this.getListView();
					int selectedItemPosition = listView.getSelectedItemPosition();
					if(selectedItemPosition == ListView.INVALID_POSITION){
						view.setBackgroundColor(Color.WHITE);
					}
		    	}
				Log.d("RecyclerListener week", "public void onMovedToScrapHeap(...){ end");
			}
		});
		Log.d("Fragment2worklog_2week", "public void onViewCreated(...) end");
	}
}
