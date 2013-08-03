package com.musclehack.musclehack;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.musclehack.musclehack.rss.StackOverflowXmlParser.RssItem;
import com.musclehack.musclehack.workouts.WorkoutManagerSingleton;

public class Fragment2worklog extends ListFragment {
	 
	public static String TAG_TEXT_WORKLOG = "textWorklog";
	protected ArrayList<HashMap<String, String>> texts;
	protected ListAdapter adapter;
	static protected ProgressDialog progressDialog = null;
	protected RetrieveProgramsTask retrieveProgramsTask;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.d("Fragment2worklog", "public View onCreateView(...) called");
		Activity mainActivity = this.getActivity();
		boolean databaseExists = WorkoutManagerSingleton.databaseExists();
		if(!databaseExists){
			Fragment2worklog.progressDialog = ProgressDialog.show(mainActivity,
					"",
					mainActivity.getString(R.string.creatingWorkout),
					true);
		}
		WorkoutManagerSingleton.getInstance().setLevelChoice(0);
		retrieveProgramsTask = new RetrieveProgramsTask();
		retrieveProgramsTask.execute();
		View view = super.onCreateView(inflater, container, savedInstanceState);
		
		Log.d("Fragment2worklog", "public View onCreateView(...) end");
		return view;
	}
	
	public void setPrograms(List<String> programNames){
		Log.d("Fragment2worklog", "public void setPrograms(List<String> programNames) called");
		Activity activity = this.getActivity();
		if(activity != null){

			if(programNames == null || programNames.size() <= 0){
			}else{
				this.texts = new ArrayList<HashMap<String, String>>();
				for(String programName:programNames){
					HashMap<String, String> map = new HashMap<String, String>();
					map.put(TAG_TEXT_WORKLOG, programName);
					this.texts.add(map);
				}
			}

			
			if(activity != null){
				this.adapter = new SimpleCustomableAdapter(
						this.getActivity(),
						this.texts,
						R.layout.fragment2worklog,
						new String[] { TAG_TEXT_WORKLOG },
						new int[] { R.id.textWorklog});
				setListAdapter(this.adapter);
			}
			if(Fragment2worklog.progressDialog != null){
				Fragment2worklog.progressDialog.dismiss();
				Fragment2worklog.progressDialog = null;
			}
			Log.d("Fragment2worklog", "public void setPrograms(List<RssItem> programNames) end");
		
		}
	}
	
	private class RetrieveProgramsTask extends AsyncTask<Void, Void, List<String>> {
		//protected List<RssItem> entries = null;
		@Override
		protected List<String> doInBackground(Void... urls) {
			Log.d("Fragment2worklog", "protected List<String> doInBackground(Void... urls) called");
			List<String> programNames = WorkoutManagerSingleton.getInstance().getAvailableProgramNamesWithShortcut();
			Log.d("Fragment2worklog", "protected List<String> doInBackground(Void... urls) end");
			return programNames;
		}

		@Override
		protected void onPostExecute(List<String> items) { 
			Log.d("Fragment2worklog", "protected List<String> doInBackground(Void... urls) called");
			setPrograms(items);
			if(items == null){
				this.cancel(true);
			}
			Log.d("Fragment2worklog", "protected void onPostExecute(...) end");
		}
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onStart() {
		super.onStart();
	}
	
	@Override
	public void onDestroyView(){
		Log.d("Fragment2worklog", "public void onDestroyView() called");
		retrieveProgramsTask.cancel(false);
		if(Fragment2worklog.progressDialog != null){
			Fragment2worklog.progressDialog.dismiss();
			Fragment2worklog.progressDialog = null;
		}
		super.onDestroyView();
		Log.d("Fragment2worklog", "public void onDestroyView() end");
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id){
		Log.d("Fragment2worklog", "public void onListItemClick(...) called");
		super.onListItemClick(l, v, position, id);
		TextView textView = (TextView) v.findViewById(R.id.textWorklog); 
		String clickedText = textView.getText().toString();
		this.doTransaction(clickedText);
		Log.d("Fragment2worklog", "public void onListItemClick(...) end");
	}

	public void doTransaction(String rowText){
		Log.d("Fragment2worklog", "public void doTransaction(String rowText) called");
		if(!rowText.equals("")){
			WorkoutManagerSingleton workoutManager = WorkoutManagerSingleton.getInstance();
			String lastSubProgramShortcutName = workoutManager.getLastProgramShortcutName();
			ListFragment newFragment = null;
			if(rowText.equals(lastSubProgramShortcutName)){
				workoutManager.selectLastProgram();
				newFragment = new Fragment2worklog_2week();
			}else if(rowText.equals(WorkoutManagerSingleton.CREATE_PROGRAM)){
				//TODO notify data manager we are in workout edition mode
				newFragment = new Fragment2customize1choose();
			}else{
				workoutManager.selectProgram(rowText);
				newFragment = new Fragment2worklog_2week();
			}
			
			FragmentTransaction transaction = getFragmentManager().beginTransaction();

			transaction.replace(this.getId(), newFragment);
			//transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
			transaction.addToBackStack("progToWeek");

			transaction.commit();
		}
		Log.d("Fragment2worklog", "public void doTransaction(String rowText) end");
	}
}
