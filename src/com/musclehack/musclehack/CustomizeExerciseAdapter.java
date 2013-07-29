package com.musclehack.musclehack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.musclehack.musclehack.workouts.Exercice;
import com.musclehack.musclehack.workouts.WorkoutManagerSingleton;

public class CustomizeExerciseAdapter extends BaseAdapter {
	protected ArrayList<HashMap<Integer, String>> data;
	protected Context context;
	protected LayoutInflater inflater;
	protected ListView listView;
	protected int lastPosition;
	
	public CustomizeExerciseAdapter(Context context,
			ArrayList<HashMap<Integer, String>> data){
		Log.d("CustomizeExerciseAdapter", "public CustomizeExerciseAdapter(…){ called");
		this.context = context;
		this.data = data;
		this.lastPosition = -1;
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		Log.d("CustomizeExerciseAdapter", "public CustomizeExerciseAdapter(…){ end");
		
	}
	
	public Context getContext(){
		return this.context;
	}
	
	public ListView getListView(){
		return this.listView;
	}
	
	@Override
	public int getCount() {
		Log.d("CustomizeExerciseAdapter", "public int getCount()  called");
		int count = 0;
		if(data != null){
			count = data.size();
		}
		Log.d("CustomizeExerciseAdapter", "public int getCount()  end");
		return count;
	}

	@Override
	public Object getItem(int arg0) {
		Log.d("CustomizeExerciseAdapter", "public Object getItem(int arg0) called");
		Object item = data.get(0);
		Log.d("CustomizeExerciseAdapter", "public Object getItem(int arg0) end");
		return item;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	protected class RowInfo{
		protected int position;
		protected String exerciseName;
		protected String rest;
		protected String repRange;
	}
	
	RowInfo getRowInfo(View view){
		Log.d("CustomizeExerciseAdapter", "public RowInfo getRowInfo(…) called");
		RowInfo rowInfo = new RowInfo();
		
		TextView textViewPosition
		= (TextView)view.findViewById(
				R.id.textViewPosition);
		String positionString
		= textViewPosition.getText().toString();
		//positionString
		//= positionString.substring(0, positionString.length()-6);
		Log.d("CustomizeExerciseAdapter", "positionString:" + positionString);
		rowInfo.position = Integer.parseInt(positionString);
		
		EditText editTextExerciseName
		= (EditText)view.findViewById(
				R.id.editTextExerciseName);
		rowInfo.exerciseName
		= editTextExerciseName.getText().toString();
		
		EditText editTextRest
		= (EditText)view.findViewById(R.id.editTextRestTime);
		rowInfo.rest
		= editTextRest.getText().toString();
		
		EditText editTextRepRange
		= (EditText)view.findViewById(R.id.editTextRepRange);
		rowInfo.repRange
		= editTextRepRange.getText().toString();

		Log.d("CustomizeExerciseAdapter", "public RowInfo getRowInfo(…) end");
		return rowInfo;
	}
	
	@Override
	public View getView(int position, View view, ViewGroup listView){
		Log.d("CustomizeExerciseAdapter", "public View getView(…) called");
		if(view == null){
			view = this.inflater.inflate(R.layout.customize_exercises,
											listView,
											false);
		}
		HashMap<Integer, String> row = this.data.get(position);
		String setNumber = this.positionToSetNumber(position);
		TextView textViewSetNumber
		= (TextView)view.findViewById(R.id.textViewSetNumber);
		textViewSetNumber.setText(setNumber);
		Log.d("CustomizeExerciseAdapter", "setNumber: " + setNumber);
		
		TextView textViewPosition
		= (TextView)view.findViewById(R.id.textViewPosition);
		textViewPosition.setText("" + position);
		
		EditText editTextExerciseName
		= (EditText)view.findViewById(R.id.editTextExerciseName);
		String exerciseName = row.get(R.id.editTextExerciseName);
		editTextExerciseName.setText(exerciseName);
		
		EditText editTextRest
		= (EditText)view.findViewById(R.id.editTextRestTime);
		String rest = row.get(R.id.editTextRestTime);
		editTextRest.setText(rest);
		
		EditText editTextRepRange
		= (EditText)view.findViewById(R.id.editTextRepRange);
		String repRange = row.get(R.id.editTextRepRange);
		editTextRepRange.setText(repRange);
		
		
		Button addUnderButton
		= (Button)view.findViewById(R.id.buttonAddUnder);
		OnClickListener addUnderClickListener = new OnClickListener(){
			@Override
			public void onClick(View v) {
				View parentView = (View)v.getParent();
				RowInfo rowInfo
				= CustomizeExerciseAdapter.this.getRowInfo(parentView);
				CustomizeExerciseAdapter.this.addUnder(rowInfo);
			}
		};
		addUnderButton.setOnClickListener(addUnderClickListener);

		
		Button dropButton
		= (Button)view.findViewById(R.id.buttonDrop);
		OnClickListener dropClickListener = new OnClickListener(){
			@Override
			public void onClick(View v) {
				View parentView = (View)v.getParent();
				RowInfo rowInfo
				= CustomizeExerciseAdapter.this.getRowInfo(parentView);
				CustomizeExerciseAdapter.this.drop(rowInfo);
			}
		};
		dropButton.setOnClickListener(dropClickListener);
		
		
		Button saveButton
		= (Button)view.findViewById(R.id.buttonSave);
		OnClickListener duplicateUnderClickListener = new OnClickListener(){
			@Override
			public void onClick(View v) {
				View viewRow = (View)v.getParent();
				viewRow.findViewById(R.id.hidenForFocus).requestFocus();
				CustomizeExerciseAdapter.this.save();
			}
		};
		saveButton.setOnClickListener(duplicateUnderClickListener);
		view.setPadding(0, 0, 0, 10);
		//if(position == this.data.size() - 1){
			//view.setPadding(0, 0, 0, 50);
		//}
		Log.d("CustomizeExerciseAdapter", "public View getView(…) end");
		return view;
	}
	public String positionToSetNumber(int position){
		String postfix = "th";
		if(position == 0){
			postfix = "st";
		}else if(position == 1){
			postfix = "nd";
		}else if(position == 2){
			postfix = "rd";
		}
		String setNumber = (position + 1) + postfix;
		setNumber += " set";
		return setNumber;
	}
	
	public void addUnder(RowInfo rowInfo){
		Log.d("CustomizeExerciseAdapter", "public void addUnder(…) called");
		for(HashMap<Integer, String> row:this.data){
			int currentPosition
			= Integer.parseInt(row.get(R.id.textViewPosition));
			if(currentPosition > rowInfo.position){
				currentPosition++;
				String setNumber
				= positionToSetNumber(currentPosition);
				row.put(R.id.textViewPosition,
						"" + currentPosition);
			}
		}
		HashMap<Integer, String> row = new HashMap<Integer, String>();
		
		row.put(R.id.textViewPosition,
				"" + rowInfo.position+1);
		row.put(R.id.editTextExerciseName,
				"");
		row.put(R.id.editTextRestTime,
				rowInfo.rest);
		row.put(R.id.editTextRepRange,
				rowInfo.repRange);
		this.data.add(rowInfo.position+1, row);
		this.notifyDataSetChanged();
		Log.d("CustomizeExerciseAdapter", "public void addUnder(…) end");
	}

	public void drop(RowInfo rowInfo){
		Log.d("CustomizeExerciseAdapter", "public void drop(…) called");
		this.data.remove(rowInfo.position);
		this.notifyDataSetChanged();
		Log.d("CustomizeExerciseAdapter", "public void drop(…) end");
	}
	
	public void save(){
		Log.d("CustomizeExerciseAdapter", "public void save() called");
		List<Exercice> exercises = new ArrayList<Exercice>();
		for(HashMap<Integer, String> row:this.data){
			int rest
			= Integer.parseInt(row.get(R.id.editTextRestTime));
			Exercice exercise
			= new Exercice(
					-1,
					row.get(R.id.editTextExerciseName),
					row.get(R.id.editTextRepRange),
					rest);
			exercises.add(exercise);
		}
		WorkoutManagerSingleton.getInstance()
		.setExercices(exercises);
		Log.d("CustomizeExerciseAdapter", "public void save() end");
	}
}