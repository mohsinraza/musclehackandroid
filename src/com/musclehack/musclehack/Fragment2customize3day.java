package com.musclehack.musclehack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView.RecyclerListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;

import com.musclehack.musclehack.workouts.Day;
import com.musclehack.musclehack.workouts.WorkoutManagerSingleton;

public class Fragment2customize3day extends ListFragment {
	protected ArrayList<HashMap<Integer, String>> data;
	@SuppressLint("UseSparseArrays")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.d("Fragment2customize3day", "public View onCreateView(...) called");
		this.data = new ArrayList<HashMap<Integer, String>>();
		List<Day> days = WorkoutManagerSingleton.getInstance()
				.getAvailableDays();
		
		//TODO get right Day
		//TODO add in previous fragment to select a workout
		HashMap<Integer, Day> daysHash = new HashMap<Integer, Day>();
		for(Day day : days){
			int dayOfTheWeek = day.getDayOfTheWeek();
			daysHash.put(dayOfTheWeek, day);
		}
		
		for(int i=0; i<7; i++){
			HashMap<Integer, String> map = new HashMap<Integer, String>();
			String dayName = Day.dayNames[i];
			map.put(R.id.textViewDayName, dayName);
			if(daysHash.containsKey(i)){
				Day day = daysHash.get(i);
				map.put(R.id.checkBoxEnabled, "true");
				String workoutName = day.getWorkoutName();
				map.put(R.id.editTextWorkoutName, workoutName);
			}else{
				map.put(R.id.checkBoxEnabled, "false");
				map.put(R.id.editTextWorkoutName, "");
			}
			map.put(R.id.textViewDayOfTheWeek, "" + i);
			this.data.add(map);
		}
		Log.d("Fragment2customize3day", "exercises added in list");
		CustomizeDayAdapter adapter
		= new CustomizeDayAdapter(
				this.getActivity(),
				this.data,
				this);
		
		setListAdapter(adapter);
		Log.d("Fragment2customize3day", "adapter set");
		//WorkoutManagerSingleton.getInstance().setLevelChoice(3);
		View view = super.onCreateView(inflater, container, savedInstanceState);
		Button buttonEdit = (Button) view.findViewById(R.id.buttonEdit);

		WorkoutManagerSingleton.getInstance().setLevelChoice(15);
		Log.d("Fragment2customize3day", "public View onCreateView(...) end");
		return view;
	}
	

	@Override
	public void onViewCreated(View viewTop, Bundle savedInstanceState){
		Log.d("Fragment2customize3day", "public void onViewCreated(...) called");
		super.onViewCreated(viewTop, savedInstanceState);
		this.getListView().setRecyclerListener(new RecyclerListener() {
			
			@Override
		    public void onMovedToScrapHeap(View view) {
				Log.d("RecyclerListener 3", "public void onMovedToScrapHeap(...){ called");
				boolean visible
		    	= Fragment2customize3day.this.isVisible();
		    	if(visible){
					CheckBox checkBox = (CheckBox)
							view.findViewById(R.id.checkBoxEnabled);
					checkBox.setOnCheckedChangeListener(null);
					checkBox.setChecked(false);
					Log.d("RecyclerListener 3", "listView...");
			    	ListView listView = Fragment2customize3day.this.getListView();
					Log.d("RecyclerListener 3", "listView ok");
			    	int selectedItemPosition = listView.getSelectedItemPosition();
					Log.d("RecyclerListener 3", "listView selected ok");
			    	if(selectedItemPosition == ListView.INVALID_POSITION){
			    		view.setPadding(0, 0, 0, 0);
			    	}
					Log.d("RecyclerListener 3", "public void onMovedToScrapHeap(...){ end");
		    	}
		    }
		});
	}
	
	
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id){
		Log.d("Fragment2customize3day", "public void onListItemClick(...) called");
		super.onListItemClick(l, v, position, id);
		Log.d("Fragment2customize3day", "public void onListItemClick(...) end");
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		Log.d("Fragment2worklog_4exercices", "protected void onSaveInstanceState(Bundle outState) called");
		super.onSaveInstanceState(outState);
		Log.d("Fragment2worklog_4exercices", "protected void onSaveInstanceState(Bundle outState) end");
	}
	
	@Override
	public void onDestroyView(){
		Log.d("Fragment2worklog_4exercices", "public void onDestroyView() called");
		super.onDestroyView();
		Log.d("Fragment2worklog_4exercices", "public void onDestroyView() end");
	}
}
