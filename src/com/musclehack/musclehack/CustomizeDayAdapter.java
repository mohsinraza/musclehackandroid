package com.musclehack.musclehack;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
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

public class CustomizeDayAdapter extends BaseAdapter {
	protected ArrayList<HashMap<Integer, String>> data;
	protected Context context;
	protected LayoutInflater inflater;
	
	public CustomizeDayAdapter(Context context,
			ArrayList<HashMap<Integer, String>> data){
		Log.d("CustomizeDayAdapter", "public CustomizeDayAdapter(…){ called");
		this.context = context;
		this.data = data;
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		Log.d("CustomizeDayAdapter", "public CustomizeDayAdapter(…){ end");
		
	}
	
	public Context getContext(){
		return this.context;
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
				view.findViewById(R.id.editTextName);
		value = row.get(R.id.editTextName);
		editText.setText(value);
		if(position == this.data.size()-1){
			view.setPadding(0, 0, 0, 150);
		}
		checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO create a fragment dialog that ask to import a day
				// and then create or destroy the day
				if(isChecked){
					FragmentActivity activity = (FragmentActivity)
							CustomizeDayAdapter.this.getContext();
					FragmentManager fm = activity.getSupportFragmentManager();
					ImportDayDialog editNameDialog = new ImportDayDialog();
					editNameDialog.show(fm, "fragment_edit_name");
				}else{
					//TODO Ask for removing
				}

			}
		});
		Log.d("CustomizeDayAdapter", "public View getView(…) end");
		return view;
	}

}
