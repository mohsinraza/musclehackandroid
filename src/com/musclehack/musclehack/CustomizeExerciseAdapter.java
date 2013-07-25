package com.musclehack.musclehack;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

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
	}
	
	RowInfo getRowInfo(View view){
		RowInfo rowInfo = new RowInfo();
		
		TextView textViewPosition
		= (TextView)view.findViewById(
				R.id.textViewPosition);
		String positionString
		= textViewPosition.getText().toString();
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
		String postfix = "th";
		if(position == 0){
			postfix = "st";
		}else if(position == 1){
			postfix = "nd";
		}
		String setNumber = (position + 1) + postfix;
		setNumber += " set";
		TextView textViewSetNumber
		= (TextView)view.findViewById(R.id.textViewPosition);
		textViewSetNumber.setText(setNumber);
		
		EditText editTextExerciseName
		= (EditText)view.findViewById(R.id.editTextExerciseName);
		String exerciseName = row.get(R.id.editTextExerciseName);
		editTextExerciseName.setText(exerciseName);
		
		EditText editTextRest
		= (EditText)view.findViewById(R.id.editTextRestTime);
		String rest = row.get(R.id.editTextRestTime);
		editTextRest.setText(rest);
		
		
		Button addUnderButton
		= (Button)view.findViewById(R.id.buttonAddUnder);
		OnClickListener addUnderClickListener = new OnClickListener(){
			@Override
			public void onClick(View v) {
				View parentView = (View)v.getParent();
				RowInfo rowInfo
				= CustomizeExerciseAdapter.this.getRowInfo(parentView);
			}
		};
		
		Button duplicateUnderButton
		= (Button)view.findViewById(R.id.buttonDuplicateUnder);
		OnClickListener duplicateUnderClickListener = new OnClickListener(){
			@Override
			public void onClick(View v) {
				View parentView = (View)v.getParent();
				RowInfo rowInfo
				= CustomizeExerciseAdapter.this.getRowInfo(parentView);
				
			}
		};
		
		Button dropButton
		= (Button)view.findViewById(R.id.buttonDrop);
		OnClickListener dropClickListener = new OnClickListener(){
			@Override
			public void onClick(View v) {
				View parentView = (View)v.getParent();
				RowInfo rowInfo
				= CustomizeExerciseAdapter.this.getRowInfo(parentView);
				
			}
		};
		
		view.setPadding(0, 0, 0, 10);
		//if(position == this.data.size() - 1){
			//view.setPadding(0, 0, 0, 50);
		//}
		Log.d("CustomizeExerciseAdapter", "public View getView(…) end");
		return view;
	}
}
