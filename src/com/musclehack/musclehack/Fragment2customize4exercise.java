package com.musclehack.musclehack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.RecyclerListener;
import android.widget.ListView;

import com.musclehack.musclehack.workouts.Exercice;
import com.musclehack.musclehack.workouts.WorkoutManagerSingleton;

public class Fragment2customize4exercise extends ListFragment {
	protected ArrayList<HashMap<Integer, String>> data;
	protected CustomizeExerciseAdapter adapter;
	@SuppressLint("UseSparseArrays")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.d("Fragment2customize4exercise", "public View onCreateView(...) called");
		this.data = new ArrayList<HashMap<Integer, String>>();
		List<Exercice> exercises = WorkoutManagerSingleton.getInstance()
				.getAvailableExercises();
		Log.d("Fragment2customize4exercise", "n exercises = " + exercises.size());
		int position = 0;
		if(exercises.size() == 0){
			HashMap<Integer, String> row = new HashMap<Integer, String>();
			row.put(R.id.textViewPosition,
					"0");
			row.put(R.id.editTextExerciseName,
					"Exercise name");
			row.put(R.id.editTextRestTime,
					"120");
			row.put(R.id.editTextRepRange,
					"8-12");
			this.data.add(row);
		}
		for(Exercice exercise:exercises){
			HashMap<Integer, String> row = new HashMap<Integer, String>();
			row.put(R.id.textViewPosition,
					"" + position);
			String exerciseName = exercise.getName();
			row.put(R.id.editTextExerciseName,
					exerciseName);
			String restTime = "" + exercise.getRest();
			row.put(R.id.editTextRestTime,
					restTime);
			String repRange = "" + exercise.getRepRange();
			row.put(R.id.editTextRepRange,
					repRange);
			this.data.add(row);
			position++;
		}
		View view = super.onCreateView(inflater, container, savedInstanceState);

		Log.d("Fragment2customize4exercise", "exercises added in list");
		this.adapter
		= new CustomizeExerciseAdapter(
				this.getActivity(),
				this.data);
		
		setListAdapter(adapter);
		Log.d("Fragment2customize4exercise", "adapter set");

		WorkoutManagerSingleton.getInstance().setLevelChoice(16);
		//TODO retrieve data eventually
		Log.d("Fragment2customize4exercise", "public View onCreateView(...) end");
		return view;
	}
	

	@Override
	public void onViewCreated(View viewTop, Bundle savedInstanceState){
		Log.d("Fragment2customize4exercise", "public void onViewCreated(...) called");
		super.onViewCreated(viewTop, savedInstanceState);
		this.getListView().setRecyclerListener(new RecyclerListener() {
			@Override
			public void onMovedToScrapHeap(View view) {
				Log.d("RecyclerListener", "public void onMovedToScrapHeap(...){ called");
				boolean visible
		    	= Fragment2customize4exercise.this.isVisible();
		    	if(visible){
				
					ListView listView = Fragment2customize4exercise.this.getListView();
					int selectedItemPosition = listView.getSelectedItemPosition();
					if(selectedItemPosition == ListView.INVALID_POSITION){
						view.setPadding(0, 0, 0, 0);
					}
		    	}
				Log.d("RecyclerListener", "public void onViewCreated(View viewTop, Bundle savedInstanceState){ end");
			}
		});
	}

	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id){
		Log.d("Fragment2customize4exercise", "public void onListItemClick(...) called");
		super.onListItemClick(l, v, position, id);
		Log.d("Fragment2customize4exercise", "public void onListItemClick(...) end");
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
		this.adapter.onDestroyView();
		super.onDestroyView();
		Log.d("Fragment2worklog_4exercices", "public void onDestroyView() end");
	}
}
