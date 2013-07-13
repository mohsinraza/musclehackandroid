package com.musclehack.musclehack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.RecyclerListener;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.musclehack.musclehack.workouts.Exercice;
import com.musclehack.musclehack.workouts.WorkoutManagerSingleton;

public class Fragment2worklog_4exercices extends ListFragment {
	public static String TAG_EXERCICE_ID = "exerciseId";
	public static String TAG_EXERCICE_NUMBER = "exerciseNumber";
	public static String TAG_EXERCICE_NAME = "exerciseName";
	public static String TAG_EXERCICE_RANGE = "exerciseRange";
	public static String TAG_EXERCICE_REST = "exerciseRest";
	public static String TAG_EXERCICE_WEIGHT = "exerciseWeight";
	public static String TAG_EXERCICE_NREPS = "exerciseNreps";
	public static String TAG_EXERCICE_PREV_REST = "exercisePrevRest";
	public static String TAG_EXERCICE_PREV_WEIGHT = "exercisePrevWeight";
	public static String TAG_EXERCICE_PREV_NREPS = "exercisePrevNreps";
	//protected ArrayList<HashMap<String, String>> texts;
	protected ArrayList<HashMap<Integer, String>> texts;
	protected ListAdapter adapter;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.d("Fragment2worklog_4exercices", "public View onCreateView(...) called");
		//this.texts = new ArrayList<HashMap<String, String>>();
		this.texts = new ArrayList<HashMap<Integer, String>>();
		List<Exercice> exercises = WorkoutManagerSingleton.getInstance().getAvailableExercices();
		Log.d("Fragment2worklog_4exercices", "exercises got");
		List<Exercice> previousExercises = WorkoutManagerSingleton.getInstance().getPreviousExercices();
		Log.d("Fragment2worklog_4exercices", "previousExercises got");
		int exerciseNumber = 1;
		for(int i=0; i<exercises.size(); i++){
			Exercice exercise = exercises.get(i);
			HashMap<Integer, String> map = new HashMap<Integer, String>();
			int exerciceId = exercise.getId();
			//map.put(TAG_EXERCICE_ID, "" + exerciceId);
			map.put(R.id.exerciseId, "" + exerciceId);
			String end = "th";
			if(exerciseNumber == 1){
				end = "st";
			}else if(exerciseNumber == 2){
				end = "nd";
			}else if(exerciseNumber == 3){
				end = "rd";
			}
			String exerciseNumberString = exerciseNumber + end + " set";
			//map.put(TAG_EXERCICE_NUMBER, "" + exerciseNumberString);
			map.put(R.id.exerciseNumber, "" + exerciseNumberString);
			String exerciceName = exercise.getName();
			//map.put(TAG_EXERCICE_NAME, exerciceName);
			map.put(R.id.exerciseName, exerciceName);
			String range = exercise.getRepRange();
			//map.put(TAG_EXERCICE_RANGE, range + " reps"); //TODO translate
			map.put(R.id.range, range + " reps"); //TODO translate
			int rest = exercise.getRest();
			//map.put(TAG_EXERCICE_REST, "" + rest);
			map.put(R.id.rest, "" + rest);
			float weight = exercise.getWeight();
			//map.put(TAG_EXERCICE_WEIGHT, "" + weight);
			map.put(R.id.weight, "" + weight);
			int nReps = exercise.getNRep();
			//map.put(TAG_EXERCICE_NREPS, "" + nReps);
			map.put(R.id.nreps, "" + nReps);
			int prevRest = 0;
			float prevWeight = 0.f;
			int prevNReps = 0;
			if(previousExercises != null){
				Exercice previousExercise = previousExercises.get(i);
				prevRest = previousExercise.getRest();
				prevWeight = previousExercise.getWeight();
				prevNReps = previousExercise.getNRep();
			}
			//map.put(TAG_EXERCICE_PREV_WEIGHT, "" + prevWeight);
			map.put(R.id.previousWeight, "" + prevWeight);
			//map.put(TAG_EXERCICE_PREV_NREPS, "" + prevNReps);
			map.put(R.id.previousNreps, "" + prevNReps);
			//map.put(TAG_EXERCICE_PREV_REST, "" + prevRest);
			map.put(R.id.previousRest, "" + prevRest);
			this.texts.add(map);
			exerciseNumber++;
		}
		Log.d("Fragment2worklog_4exercices", "exercises added in list");
		this.adapter = new ExercisesAdapter(this.getActivity(),
				this.texts);
		
		setListAdapter(this.adapter);
		Log.d("Fragment2worklog_4exercices", "adapter set");
		WorkoutManagerSingleton.getInstance().setLevelChoice(3);
		View view = super.onCreateView(inflater, container, savedInstanceState);

		Log.d("Fragment2worklog_4exercices", "public View onCreateView(...) end");
		return view;
	}
	

	@Override
	public void onViewCreated(View viewTop, Bundle savedInstanceState){
		Log.d("Fragment2worklog_4exercices", "public void onViewCreated(...) called");
		super.onViewCreated(viewTop, savedInstanceState);
		//*
		this.getListView().setRecyclerListener(new RecyclerListener() {
		    @Override
		    public void onMovedToScrapHeap(View view) {
		    	Log.d("RecyclerListener", "public void onViewCreated(View viewTop, Bundle savedInstanceState){ called");
		    	ListView listView = Fragment2worklog_4exercices.this.getListView();
		    	int selectedItemPosition = listView.getSelectedItemPosition();
		    	if(selectedItemPosition == ListView.INVALID_POSITION){
			    	view.findViewById(R.id.hidenForFocus).requestFocus();
			        // Cast the view to the type of the view we inflated.
			    	this.resetTextView(view,  R.id.weight);
			    	this.resetTextView(view,  R.id.nreps);
			    	this.resetTextView(view,  R.id.rest);
			    	View mainLayout = (View)view.findViewById(R.id.mainLayout);
			    	mainLayout.setBackgroundColor(Color.WHITE);
					
		    	}
		        Log.d("RecyclerListener", "public void onViewCreated(View viewTop, Bundle savedInstanceState){ end");
				
		    }
		    protected void resetTextView(View view, int id){
		    	EditText editText = (EditText)view.findViewById(id);
		    	editText.setText("");
		    }
		});
		//*/
		Log.d("Fragment2worklog_4exercices", "public void onViewCreated(...) end");
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id){
		Log.d("Fragment2worklog_4exercices", "public void onListItemClick(...) called");
		super.onListItemClick(l, v, position, id);
		Log.d("Fragment2worklog_4exercices", "public void onListItemClick(...) end");
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
