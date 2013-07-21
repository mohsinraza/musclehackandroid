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

	@Override
	public View getView(int position, View view, ViewGroup listView){
		Log.d("CustomizeExerciseAdapter", "public View getView(…) called");
		if(view == null){
			view = this.inflater.inflate(R.layout.customize_exercises,
											listView,
											false);
		}
		HashMap<Integer, String> row = this.data.get(position);
		Log.d("CustomizeExerciseAdapter", "public View getView(…) end");
		return view;
	}
}
