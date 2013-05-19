package com.musclehack.musclehack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
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
	protected ArrayList<HashMap<String, String>> texts;
	protected ListAdapter adapter;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.texts = new ArrayList<HashMap<String, String>>();
		List<Exercice> exercises = WorkoutManagerSingleton.getInstance().getAvailableExercices();
		List<Exercice> previousExercises = WorkoutManagerSingleton.getInstance().getPreviousExercices();
		int exerciseNumber = 1;
		for(int i=0; i<exercises.size(); i++){
			Exercice exercise = exercises.get(i);
			HashMap<String, String> map = new HashMap<String, String>();
			int exerciceId = exercise.getId();
			map.put(TAG_EXERCICE_ID, "" + exerciceId);
			String exerciseNumberString = (exerciseNumber<=9?"0":"") + exerciseNumber;
			map.put(TAG_EXERCICE_NUMBER, "" + exerciseNumberString);
			String exerciceName = exercise.getName();
			map.put(TAG_EXERCICE_NAME, exerciceName);
			String range = exercise.getRepRange();
			map.put(TAG_EXERCICE_RANGE, range + " reps"); //TODO translate
			int rest = exercise.getRest();
			map.put(TAG_EXERCICE_REST, "" + rest);
			float weight = exercise.getWeight();
			map.put(TAG_EXERCICE_WEIGHT, "" + weight);
			int nReps = exercise.getNRep();
			map.put(TAG_EXERCICE_NREPS, "" + nReps);
			int prevRest = 0;
			float prevWeight = 0.f;
			int prevNReps = 0;
			if(previousExercises != null){
				Exercice previousExercise = previousExercises.get(i);
				prevRest = previousExercise.getRest();
				prevWeight = previousExercise.getWeight();
				prevNReps = previousExercise.getNRep();
			}
			map.put(TAG_EXERCICE_PREV_REST, "" + prevRest);
			map.put(TAG_EXERCICE_PREV_WEIGHT, "" + prevWeight);
			map.put(TAG_EXERCICE_PREV_NREPS, "" + prevNReps);
			this.texts.add(map);
			exerciseNumber++;
		}

		this.adapter = new SimpleExerciseAdapter(this.getActivity(),
												this.texts,
												R.layout.fragment2worklog_exercise,
												new String[] {TAG_EXERCICE_NAME,
																TAG_EXERCICE_NUMBER,
																TAG_EXERCICE_ID,
																TAG_EXERCICE_RANGE,
																TAG_EXERCICE_REST,
																TAG_EXERCICE_WEIGHT,
																TAG_EXERCICE_NREPS,
																TAG_EXERCICE_PREV_REST,
																TAG_EXERCICE_PREV_WEIGHT,
																TAG_EXERCICE_PREV_NREPS},
												new int[] {R.id.exerciseName,
															R.id.exerciseNumber,
															R.id.exerciseId,
															R.id.range,
															R.id.rest,
															R.id.weight,
															R.id.nreps,
															R.id.previousRest,
															R.id.previousWeight,
															R.id.previousNreps});
		setListAdapter(this.adapter);
		return super.onCreateView(inflater, container, savedInstanceState);
	}
	

	@Override
	public void onViewCreated(View viewTop, Bundle savedInstanceState){
		super.onViewCreated(viewTop, savedInstanceState);
		/*
		ListView listView = this.getListView();
		if(listView != null){
			int nChilds = listView.getCount();
			for(int i=0; i<nChilds; i++){
				Class className = listView.getItemAtPosition(i).getClass();
				HashMap<String, String> map = (HashMap<String, String>)listView.getItemAtPosition(i);
				View view = (View)listView.getItemAtPosition(i);
		        if(view instanceof LinearLayout && ((LinearLayout)view).getChildAt(1) instanceof LinearLayout){
		            LinearLayout topLayout = (LinearLayout)view;
		            LinearLayout secondLayout = (LinearLayout)topLayout.getChildAt(1);
		    		EditText editText = (EditText) secondLayout.getChildAt(1);
		    		String restText = editText.getText().toString();
		    		//int rest = Integer.parseInt(restText);
		    		editText = (EditText) secondLayout.getChildAt(3);
		    		String weightText = editText.getText().toString();
		    		editText = (EditText) secondLayout.getChildAt(5);
		    		String nRepsText = editText.getText().toString();
		    		boolean exerciseDone = false;
		    		if(!weightText.equals("") && !nRepsText.equals("")){
		        		float weight = Float.parseFloat(weightText);
		        		int nReps = Integer.parseInt(nRepsText);
		        		exerciseDone = weight > 0 && nReps > 0;
		    		}
		    		if(exerciseDone){
		    			topLayout.setBackgroundColor(Color.CYAN);
		    		}else{
		    			topLayout.setBackgroundColor(Color.WHITE);
		    		}
		        }
			}
		}
		//*/
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id){
		super.onListItemClick(l, v, position, id);
		/*
		ListFragment newFragment = new Fragment2worklog_2subProg();
		TextView textView = (TextView) v.findViewById(R.id.textWorklog); 
		String clickedText = textView.getText().toString();
		WorkoutManagerSingleton.getInstance().selectDay(clickedText);

		FragmentTransaction transaction = getFragmentManager().beginTransaction();




		transaction.replace(this.getId(), newFragment);
		transaction.addToBackStack(null);

		transaction.commit();
		//*/
	}
}
