package com.musclehack.musclehack;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.musclehack.musclehack.workouts.WorkoutManagerSingleton;

public class Fragment2customize2name extends Fragment {

	protected boolean fromExistingProgramMode;
	protected View mainView;
	
	public Fragment2customize2name(){
		super();
		this.fromExistingProgramMode = false;
		this.mainView = null;
	}

	
	public void enableExistingProgramMode(){
		this.fromExistingProgramMode = true;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
								Bundle savedInstanceState) {
		this.mainView = inflater.inflate(R.layout.create_workout,
				container, false);
		this.fillSpinnerEventually(this.mainView);
		this.connectButton(this.mainView);
		if(this.fromExistingProgramMode){
			WorkoutManagerSingleton.getInstance().setLevelChoice(13);
		}else{

			WorkoutManagerSingleton.getInstance().setLevelChoice(12);
		}
		return this.mainView;
	}
	
	public void fillSpinnerEventually(View view){
		if(this.fromExistingProgramMode){
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
	}
	public void connectButton(View view){
		Button button = (Button)view.findViewById(R.id.buttonSave);
		button.setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View v) {
		    	Fragment2customize2name.this.saveAndContinue();
		    	
		    }
		});
		
	}
	
	public void saveAndContinue(){
		EditText programNameTextEdit = (EditText) 
		this.mainView.findViewById(R.id.editTextName);
		EditText nWeeksEditText = (EditText) 
		this.mainView.findViewById(R.id.editTextNWeeks);
		String programName = programNameTextEdit.getText().toString();
		String nWeeksString = nWeeksEditText.getText().toString();
		WorkoutManagerSingleton workoutManager
		= WorkoutManagerSingleton.getInstance();
		if(programName.equals("")){
			Activity activity = this.getActivity();
			Resources res = activity.getResources();
			
			new AlertDialog.Builder(activity)
			.setTitle(res.getString(R.string.warningNeedName))
			.setMessage(res.getString(R.string.warningNeedNameMessage))
			.show();
			return;
		}else if (nWeeksString.equals("")){
			Activity activity = this.getActivity();
			Resources res = activity.getResources();
			new AlertDialog.Builder(activity)
			.setTitle(res.getString(R.string.warningNeedNWeeks))
			.setMessage(res.getString(R.string.warningNeedNWeeksMessage))
			.show();
			return;
		}
		boolean nameAvailable
		= workoutManager.isWorkoutNameAvailable(programName);
		if (!nameAvailable){
			Activity activity = this.getActivity();
			Resources res = activity.getResources();
			new AlertDialog.Builder(activity)
			.setTitle(res.getString(R.string.warningName))
			.setMessage(res.getString(R.string.warningNameMessage))
			.show();
			return;
		}
		int nWeeks = Integer.parseInt(nWeeksString);
		if(nWeeks < 2 || nWeeks > 50){
			Activity activity = this.getActivity();
			Resources res = activity.getResources();
			new AlertDialog.Builder(activity)
			.setTitle(res.getString(R.string.warningNWeeks))
			.show();
			return;
		}
		if(this.fromExistingProgramMode){
			Spinner spinner = (Spinner)
					this.mainView.findViewById(
							R.id.spinnerProgramNames2);
			String existingProgramName
			= spinner.getSelectedItem().toString();
			workoutManager.createProgramFromExistingOne(
					programName,
					nWeeks,
					existingProgramName);
		}else{
			workoutManager.createProgram(programName, nWeeks);
			workoutManager.selectProgram(programName);
			workoutManager.selectFistWeek();
			Fragment newFragment = new Fragment2customize3day();
			FragmentTransaction transaction
				= getFragmentManager().beginTransaction();

			transaction.replace(this.getId(), newFragment);
			transaction.addToBackStack("customizationFromName");

			transaction.commit();
		}
	}
}
