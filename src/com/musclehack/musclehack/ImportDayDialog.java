package com.musclehack.musclehack;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.musclehack.musclehack.R;
import com.musclehack.musclehack.workouts.Day;
import com.musclehack.musclehack.workouts.WorkoutManagerSingleton;



public class ImportDayDialog extends DialogFragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(
				R.layout.import_exercises_in_day,
				container);
		getDialog().setTitle("Import exercises"); //TODO translate
		this.fillSpinner(view);
		return view;
	}
	
	public void fillSpinner(View view){
		List<String> dayNames =
				WorkoutManagerSingleton
				.getInstance().getAvailableDayNames();
		Spinner spinner = (Spinner)
				view.findViewById(R.id.spinnerDay);
		spinner.setVisibility(View.VISIBLE);
		//View program = 
				//view.findViewById(R.id.textViewProgram);
		//program.setVisibility(View.VISIBLE);
		Context context = this.getActivity();
		ArrayAdapter adapter = new ArrayAdapter<String>(context,
				R.layout.small_spinner,
				dayNames);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
	}
}
