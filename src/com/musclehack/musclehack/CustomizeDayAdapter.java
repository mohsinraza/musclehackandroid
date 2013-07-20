package com.musclehack.musclehack;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.musclehack.musclehack.workouts.Day;
import com.musclehack.musclehack.workouts.WorkoutManagerSingleton;

public class CustomizeDayAdapter extends BaseAdapter {
	protected ArrayList<HashMap<Integer, String>> data;
	protected Context context;
	protected LayoutInflater inflater;
	protected ListView listView;
	protected int lastPosition;
	protected boolean isChangingCheck;
	
	public CustomizeDayAdapter(Context context,
			ArrayList<HashMap<Integer, String>> data){
		Log.d("CustomizeDayAdapter", "public CustomizeDayAdapter(…){ called");
		this.context = context;
		this.data = data;
		this.lastPosition = -1;
		this.isChangingCheck = false;
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		Log.d("CustomizeDayAdapter", "public CustomizeDayAdapter(…){ end");
		
	}
	
	public Context getContext(){
		return this.context;
	}
	
	public ListView getListView(){
		return this.listView;
	}
	
	@Override
	public int getCount() {
		Log.d("CustomizeDayAdapter", "public int getCount()  called");
		int count = 0;
		if(data != null){
			count = data.size();
		}
		Log.d("CustomizeDayAdapter", "public int getCount()  end");
		return count;
	}

	@Override
	public Object getItem(int arg0) {
		Log.d("CustomizeDayAdapter", "public Object getItem(int arg0) called");
		Object item = data.get(0);
		Log.d("CustomizeDayAdapter", "public Object getItem(int arg0) end");
		return item;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup listView){
		Log.d("CustomizeDayAdapter", "public View getView(…) called");
		if(view == null){
			view = this.inflater.inflate(R.layout.create_day,
											listView,
											false);
		}
		HashMap<Integer, String> row = this.data.get(position);
		//Check box
		CheckBox checkBox = (CheckBox)
				view.findViewById(R.id.checkBoxEnabled);
		String value = row.get(R.id.checkBoxEnabled);
		Boolean checked = value.equals("true");
		checkBox.setChecked(checked);
		//Text view day name
		//TextView textView = (TextView)
				//view.findViewById(R.id.textViewDayName);
		value = row.get(R.id.textViewDayName);
		checkBox.setText(value);
		//textView.setText(value);
		//Edit text day name
		EditText editText = (EditText)
				view.findViewById(R.id.editTextWorkoutName);
		value = row.get(R.id.editTextWorkoutName);
		editText.setText(value);
		
		TextView textView
		= (TextView)view.findViewById(R.id.textViewDayOfTheWeek);
		value = row.get(R.id.textViewDayOfTheWeek);
		textView.setText(value);
		//if(position == this.data.size()-1){
			//view.setPadding(0, 0, 0, 150);
		//}
		checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				CustomizeDayAdapter.this.onCheckedBoxChanged(
						buttonView,
						isChecked);
			}
		});
		this.listView = (ListView)listView;
		Log.d("CustomizeDayAdapter", "public View getView(…) end");
		return view;
	}
	
	public void onCheckedBoxChanged(CompoundButton buttonView, boolean isChecked){
		// TODO create a fragment dialog that ask to import a day
		// and then create or destroy the day
		Log.d("CustomizeDayAdapter", "public void onCheckedBoxChanged(…) called");
		if(!this.isChangingCheck){
			View parentView = (View)buttonView.getParent();
			TextView textViewPosition
			= (TextView)parentView.findViewById(
					R.id.textViewDayOfTheWeek);
			String positionString = textViewPosition.getText().toString();
			int position = Integer.parseInt(positionString);
			this.lastPosition = position;
			EditText workoutNameEditText
			= (EditText)parentView.findViewById(
					R.id.editTextWorkoutName);
			String workoutName
			= workoutNameEditText.getText().toString();
			if(isChecked){
				/*
				if(workoutName.equals("")){ //TODO check name available and add day eventually
					Activity activity = (Activity)this.getContext();
					//Resources res = activity.getResources();
					new AlertDialog.Builder(activity)
					.setTitle("Workout name")
					.setMessage("You have to type a workout name!")
					.show();
				}else{
				//*/
				FragmentActivity activity = (FragmentActivity)
						CustomizeDayAdapter.this.getContext();
				FragmentManager fm = activity.getSupportFragmentManager();
				ImportDayDialog editNameDialog = new ImportDayDialog();
				Day dayToAdd = new Day(workoutName, position);
				editNameDialog.init(this, dayToAdd, this.context);
				editNameDialog.show(fm, "fragment_edit_name");
				//}
			}else{
				Activity activity = (Activity)this.getContext();
				AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		    	builder
		    	.setTitle("Remove day")
		    	.setMessage("Are you sure you want to remove the day?")
		    	.setIcon(android.R.drawable.ic_dialog_alert)
		    	.setPositiveButton("Yes",
		    			new DeleteDayClickListener(position))
		    	.setNegativeButton("No", new DontDeleteDayClickListener())						//Do nothing on no
		    	.show();
			}
		}
		Log.d("CustomizeDayAdapter", "public void onCheckedBoxChanged(…) end");
		
	}
	
	public void setCheckLastPosition(boolean check){
		Log.d("CustomizeDayAdapter", "public void setCheckLasPosition(…) called");
		this.isChangingCheck = true;
		ListView listView = CustomizeDayAdapter.this.getListView();
    	View view = (View)listView.getChildAt(this.lastPosition);
    	CheckBox checkBox = (CheckBox)
				view.findViewById(R.id.checkBoxEnabled);
		checkBox.setChecked(check);
		this.isChangingCheck = false;
		Log.d("CustomizeDayAdapter", "public void setCheckLasPosition(…) end");
	}


	protected class DeleteDayClickListener implements DialogInterface.OnClickListener{
		protected int dayOfTheWeek;

		public DeleteDayClickListener(int dayOfTheWeek){
			this.dayOfTheWeek = dayOfTheWeek;
		}
	    public void onClick(DialogInterface dialog, int which) {			      	
	    	WorkoutManagerSingleton.getInstance()
	    	.deleteDay(this.dayOfTheWeek);
	    }
	}
	
	protected class DontDeleteDayClickListener implements DialogInterface.OnClickListener{
		public DontDeleteDayClickListener(){
		}
	    public void onClick(DialogInterface dialog, int which) {
	    	CustomizeDayAdapter.this.setCheckLastPosition(true);
	    }
	}

}
