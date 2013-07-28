package com.musclehack.musclehack;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.musclehack.musclehack.workouts.WorkoutManagerSingleton;

public class Fragment2customize2delete extends Fragment {
	protected View mainView;
	
	public Fragment2customize2delete(){
		super();
		this.mainView = null;
	}

	
	public void enableExistingProgramMode(){
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
								Bundle savedInstanceState) {
		this.mainView = inflater.inflate(R.layout.delete_workout,
				container, false);
		this.fillSpinner(this.mainView);
		this.connectButton(this.mainView);
		WorkoutManagerSingleton.getInstance().setLevelChoice(14);
		return this.mainView;
	}
	
	public void fillSpinner(View view){
		List<String> programNames =
				WorkoutManagerSingleton
				.getInstance().getAvailableProgramNames();
		Spinner spinner = (Spinner)
				view.findViewById(R.id.spinnerProgramNames2);
		spinner.setVisibility(View.VISIBLE);
		//View program = 
				//view.findViewById(R.id.textViewProgram);
		//program.setVisibility(View.VISIBLE);
		Context context = this.getActivity();
		ArrayAdapter adapter = new ArrayAdapter<String>(context,
				android.R.layout.simple_list_item_1,
				programNames);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
	}
	public void connectButton(View view){
		Button button = (Button)view.findViewById(R.id.buttonSave);
		button.setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View v) {
		    	Fragment2customize2delete.this.deleteAndContinue();
		    }
		});
		
	}
	
	public void deleteAndContinue(){
		Spinner spinner = (Spinner)
				this.mainView.findViewById(
						R.id.spinnerProgramNames2);
		String selectedProgramName
		= spinner.getSelectedItem().toString();
		WorkoutManagerSingleton workoutManager
		= WorkoutManagerSingleton.getInstance();
		workoutManager.deleteProgram(selectedProgramName);
		Activity activity = this.getActivity();
		Resources res = activity.getResources();
		new AlertDialog.Builder(activity)
		.setTitle(res.getString(R.string.infoWorkoutDeleted))
		.setMessage(res.getString(R.string.infoWorkoutDeletedMessage))
		.setNeutralButton("Ok",
		new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
			}
		}).show(); //TODO update spinner data with adapter or equivalent'
		this.fillSpinner(this.mainView);
	}
}
