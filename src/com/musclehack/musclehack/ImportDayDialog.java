package com.musclehack.musclehack;

import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.musclehack.musclehack.workouts.Day;
import com.musclehack.musclehack.workouts.WorkoutManagerSingleton;



public class ImportDayDialog extends DialogFragment {
	protected View view;
	protected Day dayToCreate;
	
	public void setDay(Day day){
		this.dayToCreate = day;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.d("ImportDayDialog", "public View onCreateView(...) called");
		this.view = inflater.inflate(
				R.layout.import_exercises_in_day,
				container);
		getDialog().setTitle("Import exercises"); //TODO translate
		this._fillSpinner(view);
		this._connectButtons(view);
		Log.d("ImportDayDialog", "public View onCreateView(...) end");
		return this.view;
	}
	
	public void _fillSpinner(View view){
		Log.d("ImportDayDialog", "public void _fillSpinner(...) called");
		List<String> dayNames =
				WorkoutManagerSingleton
				.getInstance().getAvailableDayNames();
		Spinner spinner = (Spinner)
				view.findViewById(R.id.spinnerDay);
		spinner.setVisibility(View.VISIBLE);
		Context context = this.getActivity();
		ArrayAdapter adapter = new ArrayAdapter<String>(context,
				R.layout.small_spinner,
				dayNames);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		Log.d("ImportDayDialog", "public void _fillSpinner(...) end");
	}
	
	public void _connectButtons(View view){
		Log.d("ImportDayDialog", "public void _connectButtons(...) called");
		Button importButton
		= (Button)view.findViewById(
				R.id.buttonOkImport);
		OnClickListener importButtonListener = new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				ImportDayDialog.this.importDay();
			}
			
		};
		importButton.setOnClickListener(importButtonListener);
		
		Button dontImportButton
		= (Button)view.findViewById(
				R.id.buttonNoImport);
		OnClickListener dontImportButtonListener = new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				ImportDayDialog.this.dontImportDay();
			}
			
		};
		dontImportButton.setOnClickListener(dontImportButtonListener);
		Log.d("ImportDayDialog", "public void _connectButtons(...) end");
		
		Button cancelButton
		= (Button)view.findViewById(
				R.id.buttonCancel);
		OnClickListener cancelButtonListener = new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				ImportDayDialog.this.cancel();
			}
			
		};
		cancelButton.setOnClickListener(cancelButtonListener);
	}
	
	public void importDay(){
		Log.d("ImportDayDialog", "public void importDay(...) called");
		WorkoutManagerSingleton workoutManager = WorkoutManagerSingleton.getInstance();
		String workoutName = this.dayToCreate.getWorkoutName();
		int dayOfTheWeek = this.dayToCreate.getDayOfTheWeek();
		workoutManager.createDay(workoutName, dayOfTheWeek); //TODO check we have a selected program name
		//TODO launch next Fragment
		Log.d("ImportDayDialog", "public void importDay(...) end");
	}
	
	public void dontImportDay(){
		Log.d("ImportDayDialog", "public void dontImportDay(...) called");
		WorkoutManagerSingleton workoutManager = WorkoutManagerSingleton.getInstance();
		String workoutName = this.dayToCreate.getWorkoutName();
		int dayOfTheWeek = this.dayToCreate.getDayOfTheWeek();
		Spinner spinner = (Spinner)this.view
				.findViewById(R.id.spinnerDay);
		String fromDay
		= spinner.getSelectedItem().toString();
		workoutManager.createDayFromExistingOne(
				workoutName,
				dayOfTheWeek,
				fromDay); //TODO check we have a selected program name
		//TODO launch next Fragment
		Log.d("ImportDayDialog", "public void dontImportDay(...) end");
	
	}
	public void cancel(){
		Log.d("ImportDayDialog", "public void cancel() called");
		this.dismiss();
		Log.d("ImportDayDialog", "public void cancel() end");
	}
}
