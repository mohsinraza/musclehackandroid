package com.musclehack.musclehack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

import com.musclehack.musclehack.workouts.Day;
import com.musclehack.musclehack.workouts.WorkoutManagerSingleton;

public class Fragment2worklog_3day extends ListFragment {
	 
	public static String TAG_TEXT_WORKLOG = "textWorklog";
	protected ArrayList<HashMap<String, String>> texts;
	protected ListAdapter adapter;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.d("Fragment2worklog_3day", "public View onCreateView(...) called");
		this.texts = new ArrayList<HashMap<String, String>>();
		List<Day> days = WorkoutManagerSingleton.getInstance().getAvailableDays();
		for(Day day:days){
			HashMap<String, String> map = new HashMap<String, String>();
			String workoutName = day.getWorkoutName();
			map.put(TAG_TEXT_WORKLOG, workoutName);
			this.texts.add(map);
		}

		this.adapter = new SimpleDayAdapter(this.getActivity(),
												this.texts,
												R.layout.fragment2worklog,
												new String[] { TAG_TEXT_WORKLOG },
												new int[] { R.id.textWorklog});
		setListAdapter(this.adapter);
		WorkoutManagerSingleton.getInstance().setLevelChoice(2);
		View view = super.onCreateView(inflater, container, savedInstanceState);
		Log.d("Fragment2worklog_3day", "public View onCreateView(...) end");
		return view;
	}
	
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id){
		super.onListItemClick(l, v, position, id);
		Log.d("Fragment2worklog_3day", "public void onListItemClick(...) called");
		ListFragment newFragment = new Fragment2worklog_4exercices();
		TextView textView = (TextView) v.findViewById(R.id.textWorklog); 
		String clickedText = textView.getText().toString();
		WorkoutManagerSingleton.getInstance().selectDay(clickedText);

		FragmentTransaction transaction = getFragmentManager().beginTransaction();

		transaction.replace(this.getId(), newFragment);
		//transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		transaction.addToBackStack("dayToExercise");

		transaction.commit();
		Log.d("Fragment2worklog_3day", "public void onListItemClick(...) end");
	}
}
