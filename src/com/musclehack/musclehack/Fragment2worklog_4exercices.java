package com.musclehack.musclehack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.musclehack.musclehack.workouts.Exercice;
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

public class Fragment2worklog_4exercices extends ListFragment {
	public static String TAG_EXERCICE_NAME = "exerciseName";
	public static String TAG_EXERCICE_RANGE = "exerciseRange";
	public static String TAG_EXERCICE_REST = "exerciseRest";
	public static String TAG_EXERCICE_WEIGHT = "exerciseWeight";
	public static String TAG_EXERCICE_NREPS = "exerciseNreps";
	protected ArrayList<HashMap<String, String>> texts;
	protected ListAdapter adapter;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.texts = new ArrayList<HashMap<String, String>>();
		List<Exercice> exercices = WorkoutManagerSingleton.getInstance().getAvailableExercices();
		for(Exercice exercice:exercices){
			HashMap<String, String> map = new HashMap<String, String>();
			String exerciceName = exercice.getName();
			map.put(TAG_EXERCICE_NAME, exerciceName);
			String range = exercice.getRepRange();
			map.put(TAG_EXERCICE_RANGE, range + " reps"); //TODO translate
			int rest = exercice.getRest();
			map.put(TAG_EXERCICE_REST, "" + rest);
			float weight = exercice.getWeight();
			map.put(TAG_EXERCICE_WEIGHT, "" + weight);
			int nReps = exercice.getNRep();
			map.put(TAG_EXERCICE_NREPS, "" + nReps);
			this.texts.add(map);
		}

		this.adapter = new SimpleHtmlAdapter(this.getActivity(),
												this.texts,
												R.layout.fragment2worklog_row1,
												new String[] {TAG_EXERCICE_NAME,
																TAG_EXERCICE_RANGE,
																TAG_EXERCICE_REST,
																TAG_EXERCICE_WEIGHT,
																TAG_EXERCICE_NREPS},
												new int[] {R.id.exerciseName,
															R.id.range,
															R.id.rest,
															R.id.weight,
															R.id.nreps});
		setListAdapter(this.adapter);
		return super.onCreateView(inflater, container, savedInstanceState);
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
