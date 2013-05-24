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
	static protected ProgressDialog progressDialog;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.d("Fragment2worklog", "public View onCreateView(...) called");
		//*
		//Context context = this.getActivity().getApplicationContext();
		//WorkoutManagerSingleton.setContext(context);


		/*
		this.texts = new ArrayList<HashMap<String, String>>();
		List<String> programNames = WorkoutManagerSingleton.getInstance().getAvailableProgramNames();
		for(String programName:programNames){
			HashMap<String, String> map = new HashMap<String, String>();
			map.put(TAG_TEXT_WORKLOG, programName);
			this.texts.add(map);
		}


		this.adapter = new SimpleCustomableAdapter(this.getActivity(),
												this.texts,
												R.layout.fragment2worklog,
												new String[] { TAG_TEXT_WORKLOG },
												new int[] { R.id.textWorklog});
		setListAdapter(this.adapter);
		/**/
		Activity mainActivity = this.getActivity();
		Fragment2worklog.progressDialog = ProgressDialog.show(mainActivity,
				"",
				mainActivity.getString(R.string.creatingWorkout),
				true);
		WorkoutManagerSingleton.getInstance().setLevelChoice(0);
		new RetrieveProgramsTask().execute();
		View view = super.onCreateView(inflater, container, savedInstanceState);
		
		Log.d("Fragment2worklog", "public View onCreateView(...) end");
		return view;
	}
	
	public void setPrograms(List<String> programNames){
		Log.d("Fragment2worklog", "public void setList(List<String> items) called");

		if(programNames == null || programNames.size() <= 0){
		}else{
			this.texts = new ArrayList<HashMap<String, String>>();
			
			for(String programName:programNames){
				HashMap<String, String> map = new HashMap<String, String>();
				map.put(TAG_TEXT_WORKLOG, programName);
				this.texts.add(map);
			}
		}

		Activity activity = this.getActivity();
		if(activity != null){
			this.adapter = new SimpleCustomableAdapter(this.getActivity(),
					this.texts,
					R.layout.fragment2worklog,
					new String[] { TAG_TEXT_WORKLOG },
					new int[] { R.id.textWorklog});
			setListAdapter(this.adapter);
		}
		Fragment2worklog.progressDialog.dismiss();
		Log.d("Fragment2worklog", "public void setEntries(List<RssItem> entries) end");
	
	}
	private class RetrieveProgramsTask extends AsyncTask<Void, Void, List<String>> {
		//protected List<RssItem> entries = null;
		@Override
		protected List<String> doInBackground(Void... urls) {
			Log.d("Fragment2worklog", "protected List<String> doInBackground(Void... urls) called");
			List<String> programNames = WorkoutManagerSingleton.getInstance().getAvailableProgramNames();
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
			Log.d("DownloadXmlTask", "protected void onPostExecute(...) end");
		}
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		
		//WorkoutManagerSingleton.getInstance().setLevelChoice(0);
		/*
		if (savedInstanceState != null) {
			this.transactionDone = savedInstanceState.getString("transactionDone");
			this.doTransaction(this.transactionDone); //TODO mettre ca ailleurs
		}
		//*/
	}

	@Override
	public void onStart() {
		super.onStart();
		/*
		int levelChoice = WorkoutManagerSingleton.getInstance().getLevelChoice();
		if(levelChoice > 0){
			ListFragment newFragment = null;
			if(levelChoice == 1){
				newFragment = new Fragment2worklog_1subProg();
			}else if(levelChoice == 2){
				newFragment = new Fragment2worklog_2week();
			}else if(levelChoice == 3){
				newFragment = new Fragment2worklog_3day();
			}else if(levelChoice == 4){
				newFragment = new Fragment2worklog_4exercices();
			}
			FragmentTransaction transaction = getFragmentManager().beginTransaction();

			transaction.replace(this.getId(), newFragment);
			transaction.addToBackStack(null);

			transaction.commit();
		}
		//*/
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
			String lastSubProgramShortcutName = workoutManager.getLastSubProgramShortcutName();
			ListFragment newFragment = null;
			if(rowText.equals(lastSubProgramShortcutName)){
				workoutManager.selectLastSubProgram();
				newFragment = new Fragment2worklog_2week();
			}else{
				workoutManager.selectProgram(rowText);
				newFragment = new Fragment2worklog_1subProg();
			}
			
			FragmentTransaction transaction = getFragmentManager().beginTransaction();

			transaction.replace(this.getId(), newFragment);
			//transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
			transaction.addToBackStack("progToSubProg");

			transaction.commit();
		}
		Log.d("Fragment2worklog", "public void doTransaction(String rowText) end");
	}
}
