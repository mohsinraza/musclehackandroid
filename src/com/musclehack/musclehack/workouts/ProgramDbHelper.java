package com.musclehack.musclehack.workouts;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.musclehack.musclehack.workouts.ContractProgram.ContractExercise;
import com.musclehack.musclehack.workouts.ContractProgram.ContractSubProgram;
import com.musclehack.musclehack.workouts.ContractProgram.ContractWorkoutDay;
import com.musclehack.musclehack.workouts.ContractProgram.ContractWorkoutWeek;


public class ProgramDbHelper extends SQLiteOpenHelper {
	public static final int DATABASE_VERSION = 1;
	public static final String DATABASE_NAME = "programs.db";
	protected Context context;

	public ProgramDbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		Log.d("ProgramDbHelper","public ProgramDbHelper(Context context) called");
		this.context = context;
		Log.d("ProgramDbHelper","public ProgramDbHelper(Context context) end");
	}
	
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//db.execSQL(SQL_DELETE_ENTRIES);
		//onCreate(db);
	}
	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//onUpgrade(db, oldVersion, newVersion);
	}
	
	public void onCreate(SQLiteDatabase db) {
		Log.d("ProgramDbHelper","public void onCreate(SQLiteDatabase db) called");
		//Activity mainActivity = WorkoutManagerSingleton.getInstance().getMainActivity();
		//ProgressDialog progressDialog = ProgressDialog.show(mainActivity,
				//"",
				//mainActivity.getString(R.string.creatingWorkout),
				//true);
		db.execSQL(ContractProgram.CREATE_TABLE);
		db.execSQL(ContractSubProgram.CREATE_TABLE);
		db.execSQL(ContractWorkoutWeek.CREATE_TABLE);
		db.execSQL(ContractWorkoutDay.CREATE_TABLE);
		db.execSQL(ContractExercise.CREATE_TABLE);
		this.loadDefaultWorkout(db);
		//progressDialog.dismiss();
		Log.d("ProgramDbHelper","public void onCreate(SQLiteDatabase db) end");
	}
	
	public void loadDefaultWorkout(SQLiteDatabase db){
		Log.d("ProgramDbHelper","public void loadDefaultWorkout(SQLiteDatabase db) called");
		String[] repRanges = {"8-12", "7-10", "6-8"};
		ContentValues values = new ContentValues();
		values.put(ContractProgram.COLUMN_NAME_NAME, "THT5 VOLUME CYCLES");
		long newRowProgramId = db.insert(ContractProgram.TABLE_NAME, "null", values);
		for(String repRange:repRanges){
			String[] repRangeSep = repRange.split("-");
			String repRangeText = repRangeSep[0] + " to " + repRangeSep[1];
			values = new ContentValues();
			values.put(ContractSubProgram.COLUMN_NAME_NAME, "THT5 VOLUME " + repRange);
			values.put(ContractSubProgram.COLUMN_NAME_COMPLETED, false);
			values.put(ContractSubProgram.COLUMN_NAME_EXTERN_ID, newRowProgramId);
			long newRowSubProgramId = db.insert(ContractSubProgram.TABLE_NAME, "null", values);
			for(int i=0; i<10; i++){
				values = new ContentValues();
				values.put(ContractWorkoutWeek.COLUMN_NAME_NAME, "Week " + (i+1));
				values.put(ContractWorkoutWeek.COLUMN_NAME_EXTERN_ID, newRowSubProgramId);
				values.put(ContractWorkoutWeek.COLUMN_NAME_COMPLETED, false);
				long newRowWeekId = db.insert(ContractWorkoutWeek.TABLE_NAME, "null", values);
					values = new ContentValues();
					values.put(ContractWorkoutDay.COLUMN_NAME_NAME, "Monday - Shoulders & Traps");
					values.put(ContractWorkoutDay.COLUMN_NAME_COMPLETED, false);
					values.put(ContractWorkoutDay.COLUMN_NAME_DAY_OF_WEEK, 0);
					values.put(ContractWorkoutDay.COLUMN_NAME_EXTERN_ID, newRowWeekId);
					long newRowDayId = db.insert(ContractWorkoutDay.TABLE_NAME, "null", values);
						values = new ContentValues();
						values.put(ContractExercise.COLUMN_NAME_EXTERN_ID, newRowDayId);
						values.put(ContractExercise.COLUMN_NAME_COMPLETED, false);
						values.put(ContractExercise.COLUMN_NAME_NREP, 0);
						values.put(ContractExercise.COLUMN_NAME_WEIGHT, 0.f);
						values.put(ContractExercise.COLUMN_NAME_REPRANGE, repRangeText);
						values.put(ContractExercise.COLUMN_NAME_REST, "180");
						values.put(ContractExercise.COLUMN_NAME_NAME, "Overhead Barbell Press");
						for(int j=0; j<4; j++){
							db.insert(ContractExercise.TABLE_NAME, "null", values);
						}
						values.put(ContractExercise.COLUMN_NAME_NAME, "Overhead Dumbbell Press");
						values.put(ContractExercise.COLUMN_NAME_REST, "120");
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						values.put(ContractExercise.COLUMN_NAME_REPRANGE, "8 to 12");
						values.put(ContractExercise.COLUMN_NAME_NAME, "Dumbbell Lateral Raises");
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						values.put(ContractExercise.COLUMN_NAME_NAME, "Dumbbell Front Raises");
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						values.put(ContractExercise.COLUMN_NAME_REST, "240");
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						values.put(ContractExercise.COLUMN_NAME_NAME, "Shrugs");
						values.put(ContractExercise.COLUMN_NAME_REST, "120");
						values.put(ContractExercise.COLUMN_NAME_REPRANGE, "12 to 15");
						for(int j=0; j<4; j++){
							db.insert(ContractExercise.TABLE_NAME, "null", values);
						}
					values = new ContentValues();
					values.put(ContractWorkoutDay.COLUMN_NAME_NAME, "Tuesday - Legs");
					values.put(ContractWorkoutDay.COLUMN_NAME_COMPLETED, false);
					values.put(ContractWorkoutDay.COLUMN_NAME_DAY_OF_WEEK, 1);
					values.put(ContractWorkoutDay.COLUMN_NAME_EXTERN_ID, newRowWeekId);
					newRowDayId = db.insert(ContractWorkoutDay.TABLE_NAME, "null", values);
						values = new ContentValues();
						values.put(ContractExercise.COLUMN_NAME_EXTERN_ID, newRowDayId);
						values.put(ContractExercise.COLUMN_NAME_COMPLETED, false);
						values.put(ContractExercise.COLUMN_NAME_NREP, 0);
						values.put(ContractExercise.COLUMN_NAME_WEIGHT, 0.f);
						values.put(ContractExercise.COLUMN_NAME_REPRANGE, repRangeText);
						values.put(ContractExercise.COLUMN_NAME_REST, "180");
						values.put(ContractExercise.COLUMN_NAME_NAME, "Squats");
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						values.put(ContractExercise.COLUMN_NAME_NAME, "Leg Press");
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						values.put(ContractExercise.COLUMN_NAME_NAME, "Stiff Leg Deadlifts");
						values.put(ContractExercise.COLUMN_NAME_REPRANGE, "6 to 8");
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						values.put(ContractExercise.COLUMN_NAME_REST, "120");
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						values.put(ContractExercise.COLUMN_NAME_NAME, "Stiff Leg Extensions");
						values.put(ContractExercise.COLUMN_NAME_REPRANGE, "8 to 12");
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						values.put(ContractExercise.COLUMN_NAME_NAME, "Stiff Leg Curls");
						values.put(ContractExercise.COLUMN_NAME_REST, "180");
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						values.put(ContractExercise.COLUMN_NAME_NAME, "Calf Raises");
						values.put(ContractExercise.COLUMN_NAME_REST, "120");
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						db.insert(ContractExercise.TABLE_NAME, "null", values);
					values = new ContentValues();
					values.put(ContractWorkoutDay.COLUMN_NAME_NAME, "Wednesday - Tricpes, Biceps & Forearms");
					values.put(ContractWorkoutDay.COLUMN_NAME_COMPLETED, false);
					values.put(ContractWorkoutDay.COLUMN_NAME_DAY_OF_WEEK, 2);
					values.put(ContractWorkoutDay.COLUMN_NAME_EXTERN_ID, newRowWeekId);
					newRowDayId = db.insert(ContractWorkoutDay.TABLE_NAME, "null", values);
						values = new ContentValues();
						values.put(ContractExercise.COLUMN_NAME_EXTERN_ID, newRowDayId);
						values.put(ContractExercise.COLUMN_NAME_COMPLETED, false);
						values.put(ContractExercise.COLUMN_NAME_NREP, 0);
						values.put(ContractExercise.COLUMN_NAME_WEIGHT, 0.f);
						values.put(ContractExercise.COLUMN_NAME_REPRANGE, repRangeText);
						values.put(ContractExercise.COLUMN_NAME_REST, "120");
						values.put(ContractExercise.COLUMN_NAME_NAME, "Decline Tricep Extensions");
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						values.put(ContractExercise.COLUMN_NAME_REST, "180");
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						values.put(ContractExercise.COLUMN_NAME_REPRANGE, "8 to 12");
						values.put(ContractExercise.COLUMN_NAME_REST, "120");
						values.put(ContractExercise.COLUMN_NAME_NAME, "Tricep Cable Push Downs");
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						values.put(ContractExercise.COLUMN_NAME_REST, "180");
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						values.put(ContractExercise.COLUMN_NAME_REST, "120");
						values.put(ContractExercise.COLUMN_NAME_NAME, "Cable Bent-Over Triceps Extensions");
						values.put(ContractExercise.COLUMN_NAME_REPRANGE, "12 to 14");
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						values.put(ContractExercise.COLUMN_NAME_REST, "180");
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						values.put(ContractExercise.COLUMN_NAME_REST, "120");
						values.put(ContractExercise.COLUMN_NAME_NAME, "McManus Pushdown");
						values.put(ContractExercise.COLUMN_NAME_REPRANGE, "8 to 12");
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						values.put(ContractExercise.COLUMN_NAME_REST, "180");
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						values.put(ContractExercise.COLUMN_NAME_REST, "120");
						values.put(ContractExercise.COLUMN_NAME_NAME, "Cable Preacher Curls");
						values.put(ContractExercise.COLUMN_NAME_REPRANGE, repRangeText);
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						values.put(ContractExercise.COLUMN_NAME_REST, "180");
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						values.put(ContractExercise.COLUMN_NAME_REST, "120");
						values.put(ContractExercise.COLUMN_NAME_NAME, "Straight Barbell Curls");
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						values.put(ContractExercise.COLUMN_NAME_REST, "180");
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						values.put(ContractExercise.COLUMN_NAME_REST, "120");
						values.put(ContractExercise.COLUMN_NAME_NAME, "EZ/Curl Bar Curls");
						values.put(ContractExercise.COLUMN_NAME_REPRANGE, "8 to 12");
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						values.put(ContractExercise.COLUMN_NAME_NAME, "Seated Incline Dumbbell Curls");
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						values.put(ContractExercise.COLUMN_NAME_NAME, "Pinwheel Curls");
						values.put(ContractExercise.COLUMN_NAME_REPRANGE, "14 to 16");
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						values.put(ContractExercise.COLUMN_NAME_NAME, "Barbell Wrist Curls");
						values.put(ContractExercise.COLUMN_NAME_REPRANGE, "8 to 12");
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						db.insert(ContractExercise.TABLE_NAME, "null", values);
					values = new ContentValues();
					values.put(ContractWorkoutDay.COLUMN_NAME_NAME, "Thursday - Back");
					values.put(ContractWorkoutDay.COLUMN_NAME_COMPLETED, false);
					values.put(ContractWorkoutDay.COLUMN_NAME_DAY_OF_WEEK, 3);
					values.put(ContractWorkoutDay.COLUMN_NAME_EXTERN_ID, newRowWeekId);
					newRowDayId = db.insert(ContractWorkoutDay.TABLE_NAME, "null", values);
						values = new ContentValues();
						values.put(ContractExercise.COLUMN_NAME_EXTERN_ID, newRowDayId);
						values.put(ContractExercise.COLUMN_NAME_COMPLETED, false);
						values.put(ContractExercise.COLUMN_NAME_NREP, 0);
						values.put(ContractExercise.COLUMN_NAME_WEIGHT, 0.f);
						values.put(ContractExercise.COLUMN_NAME_REPRANGE, "4 to 6");
						values.put(ContractExercise.COLUMN_NAME_REST, "180");
						values.put(ContractExercise.COLUMN_NAME_NAME, "Deadlift");
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						values.put(ContractExercise.COLUMN_NAME_NAME, "Seated Cable Rows");
						values.put(ContractExercise.COLUMN_NAME_REPRANGE, "8 to 12");
						values.put(ContractExercise.COLUMN_NAME_REST, "120");
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						values.put(ContractExercise.COLUMN_NAME_REST, "180");
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						values.put(ContractExercise.COLUMN_NAME_NAME, "Unilateral One-arm Dumbbell Rows");
						values.put(ContractExercise.COLUMN_NAME_REST, "120");
						values.put(ContractExercise.COLUMN_NAME_REPRANGE, repRangeText);
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						db.insert(ContractExercise.TABLE_NAME, "null", values);
					values = new ContentValues();
					values.put(ContractWorkoutDay.COLUMN_NAME_NAME, "Friday - Chest & Abs");
					values.put(ContractWorkoutDay.COLUMN_NAME_COMPLETED, false);
					values.put(ContractWorkoutDay.COLUMN_NAME_DAY_OF_WEEK, 4);
					values.put(ContractWorkoutDay.COLUMN_NAME_EXTERN_ID, newRowWeekId);
					newRowDayId = db.insert(ContractWorkoutDay.TABLE_NAME, "null", values);
						values = new ContentValues();
						values.put(ContractExercise.COLUMN_NAME_EXTERN_ID, newRowDayId);
						values.put(ContractExercise.COLUMN_NAME_COMPLETED, false);
						values.put(ContractExercise.COLUMN_NAME_NREP, 0);
						values.put(ContractExercise.COLUMN_NAME_WEIGHT, 0.f);
						values.put(ContractExercise.COLUMN_NAME_REPRANGE, repRangeText);
						values.put(ContractExercise.COLUMN_NAME_REST, "120");
						values.put(ContractExercise.COLUMN_NAME_NAME, "Flat Barbell Bench Press");
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						values.put(ContractExercise.COLUMN_NAME_REST, "180");
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						values.put(ContractExercise.COLUMN_NAME_REST, "120");
						values.put(ContractExercise.COLUMN_NAME_NAME, "Incline Barbell Bench Press");
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						values.put(ContractExercise.COLUMN_NAME_REST, "180");
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						values.put(ContractExercise.COLUMN_NAME_REST, "120");
						values.put(ContractExercise.COLUMN_NAME_REPRANGE, "8 to 12");
						values.put(ContractExercise.COLUMN_NAME_NAME, "Pec Deck Or Cable Crossovers");
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						values.put(ContractExercise.COLUMN_NAME_REST, "180");
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						values.put(ContractExercise.COLUMN_NAME_REST, "120");
						values.put(ContractExercise.COLUMN_NAME_NAME, "Deep Dips");
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						values.put(ContractExercise.COLUMN_NAME_REST, "180");
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						values.put(ContractExercise.COLUMN_NAME_REST, "120");
						values.put(ContractExercise.COLUMN_NAME_NAME, "Kneeling Cable Crunches");
						values.put(ContractExercise.COLUMN_NAME_REPRANGE, "12 to 15");
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						values.put(ContractExercise.COLUMN_NAME_REST, "180");
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						values.put(ContractExercise.COLUMN_NAME_REST, "120");
						values.put(ContractExercise.COLUMN_NAME_REPRANGE, "8 to 12");
						values.put(ContractExercise.COLUMN_NAME_NAME, "Decline Sit-ups");
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						values.put(ContractExercise.COLUMN_NAME_REST, "180");
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						values.put(ContractExercise.COLUMN_NAME_REST, "120");
						values.put(ContractExercise.COLUMN_NAME_NAME, "Weighted Reverse Crunches");
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						db.insert(ContractExercise.TABLE_NAME, "null", values);
			}
		}
		
		values = new ContentValues();
		values.put(ContractProgram.COLUMN_NAME_NAME, "THT5 HIT CYCLES");
		newRowProgramId = db.insert(ContractProgram.TABLE_NAME, "null", values);
		for(String repRange:repRanges){
			String[] repRangeSep = repRange.split("-");
			String repRangeText = repRangeSep[0] + " to " + repRangeSep[1];
			values = new ContentValues();
			values.put(ContractSubProgram.COLUMN_NAME_NAME, "THT5 HIT " + repRange);
			values.put(ContractSubProgram.COLUMN_NAME_COMPLETED, false);
			values.put(ContractSubProgram.COLUMN_NAME_EXTERN_ID, newRowProgramId);
			long newRowSubProgramId = db.insert(ContractSubProgram.TABLE_NAME, "null", values);
			for(int i=0; i<10; i++){
				values = new ContentValues();
				values.put(ContractWorkoutWeek.COLUMN_NAME_NAME, "Week " + (i+1));
				values.put(ContractWorkoutWeek.COLUMN_NAME_COMPLETED, false);
				values.put(ContractWorkoutWeek.COLUMN_NAME_EXTERN_ID, newRowSubProgramId);
				long newRowWeekId = db.insert(ContractWorkoutWeek.TABLE_NAME, "null", values);
					String[] days = {"Monday", "Wednesday"};
					for(String day:days){
						values = new ContentValues();
						values.put(ContractWorkoutDay.COLUMN_NAME_NAME, day + " - Mass Workout");
						values.put(ContractWorkoutDay.COLUMN_NAME_COMPLETED, false);
						int dayOfWeek = day.equals(days[0])?0:2;
						values.put(ContractWorkoutDay.COLUMN_NAME_DAY_OF_WEEK, dayOfWeek);
						values.put(ContractWorkoutDay.COLUMN_NAME_EXTERN_ID, newRowWeekId);
						long newRowDayId = db.insert(ContractWorkoutDay.TABLE_NAME, "null", values);
							values = new ContentValues();
							values.put(ContractExercise.COLUMN_NAME_EXTERN_ID, newRowDayId);
							values.put(ContractExercise.COLUMN_NAME_COMPLETED, false);
							values.put(ContractExercise.COLUMN_NAME_NREP, 0);
							values.put(ContractExercise.COLUMN_NAME_WEIGHT, 0.f);
							values.put(ContractExercise.COLUMN_NAME_REPRANGE, repRangeText);
							values.put(ContractExercise.COLUMN_NAME_REST, "180");
							values.put(ContractExercise.COLUMN_NAME_NAME, "Squats or Leg Presses");
							db.insert(ContractExercise.TABLE_NAME, "null", values);
							db.insert(ContractExercise.TABLE_NAME, "null", values);
							values.put(ContractExercise.COLUMN_NAME_NAME, "Bench Press");
							values.put(ContractExercise.COLUMN_NAME_REST, "120");
							db.insert(ContractExercise.TABLE_NAME, "null", values);
							values.put(ContractExercise.COLUMN_NAME_REST, "180");
							db.insert(ContractExercise.TABLE_NAME, "null", values);
							values.put(ContractExercise.COLUMN_NAME_NAME, "Calf Raises");
							values.put(ContractExercise.COLUMN_NAME_REPRANGE, "12 to 15");
							values.put(ContractExercise.COLUMN_NAME_REST, "120");
							db.insert(ContractExercise.TABLE_NAME, "null", values);
							db.insert(ContractExercise.TABLE_NAME, "null", values);
							values.put(ContractExercise.COLUMN_NAME_NAME, "Kneeling Cable Crunches");
							db.insert(ContractExercise.TABLE_NAME, "null", values);
							db.insert(ContractExercise.TABLE_NAME, "null", values);
							values.put(ContractExercise.COLUMN_NAME_NAME, "Overhead Press");
							values.put(ContractExercise.COLUMN_NAME_REPRANGE, repRangeText);
							db.insert(ContractExercise.TABLE_NAME, "null", values);
							db.insert(ContractExercise.TABLE_NAME, "null", values);
							values.put(ContractExercise.COLUMN_NAME_NAME, "Seated Cable Rows");
							values.put(ContractExercise.COLUMN_NAME_REPRANGE, "8 to 12");
							db.insert(ContractExercise.TABLE_NAME, "null", values);
							values.put(ContractExercise.COLUMN_NAME_REST, "240");
							db.insert(ContractExercise.TABLE_NAME, "null", values);
							values.put(ContractExercise.COLUMN_NAME_NAME, "Cable Preacher Curl");
							values.put(ContractExercise.COLUMN_NAME_REPRANGE, repRangeText);
							values.put(ContractExercise.COLUMN_NAME_REST, "120");
							db.insert(ContractExercise.TABLE_NAME, "null", values);
							values.put(ContractExercise.COLUMN_NAME_REST, "180");
							db.insert(ContractExercise.TABLE_NAME, "null", values);
							values.put(ContractExercise.COLUMN_NAME_NAME, "Cable Bent-Over Triceps Extensions");
							values.put(ContractExercise.COLUMN_NAME_REPRANGE, "8 to 12");
							values.put(ContractExercise.COLUMN_NAME_REST, "120");
							db.insert(ContractExercise.TABLE_NAME, "null", values);
							values.put(ContractExercise.COLUMN_NAME_REST, "180");
							db.insert(ContractExercise.TABLE_NAME, "null", values);
							values.put(ContractExercise.COLUMN_NAME_NAME, "Shrug");
							values.put(ContractExercise.COLUMN_NAME_REPRANGE, "12 to 15");
							values.put(ContractExercise.COLUMN_NAME_REST, "120");
							db.insert(ContractExercise.TABLE_NAME, "null", values);
							db.insert(ContractExercise.TABLE_NAME, "null", values);
							values.put(ContractExercise.COLUMN_NAME_NAME, "DB Wrist Curl");
							values.put(ContractExercise.COLUMN_NAME_REPRANGE, "8 to 12");
							db.insert(ContractExercise.TABLE_NAME, "null", values);
					}
					
					values = new ContentValues();
					values.put(ContractWorkoutDay.COLUMN_NAME_NAME, "Friday - Consolidation Workout");
					values.put(ContractWorkoutDay.COLUMN_NAME_COMPLETED, false);
					values.put(ContractWorkoutDay.COLUMN_NAME_DAY_OF_WEEK, 4);
					values.put(ContractWorkoutDay.COLUMN_NAME_EXTERN_ID, newRowWeekId);
					long newRowDayId = db.insert(ContractWorkoutDay.TABLE_NAME, "null", values);
						values = new ContentValues();
						values.put(ContractExercise.COLUMN_NAME_EXTERN_ID, newRowDayId);
						values.put(ContractExercise.COLUMN_NAME_COMPLETED, false);
						values.put(ContractExercise.COLUMN_NAME_NREP, 0);
						values.put(ContractExercise.COLUMN_NAME_WEIGHT, 0.f);
						values.put(ContractExercise.COLUMN_NAME_REPRANGE, "4 to 6");
						values.put(ContractExercise.COLUMN_NAME_REST, "180");
						values.put(ContractExercise.COLUMN_NAME_NAME, "Deadlifts");
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						values.put(ContractExercise.COLUMN_NAME_NAME, "Deep Dips");
						values.put(ContractExercise.COLUMN_NAME_REPRANGE, repRangeText);
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						values.put(ContractExercise.COLUMN_NAME_NAME, "Overhead Dumbbell Press");
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						values.put(ContractExercise.COLUMN_NAME_NAME, "McManus Pushdown");
						values.put(ContractExercise.COLUMN_NAME_REPRANGE, "8 to 12");
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						values.put(ContractExercise.COLUMN_NAME_NAME, "Seated Cable Rows");
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						values.put(ContractExercise.COLUMN_NAME_NAME, "Barbell Curls");
						values.put(ContractExercise.COLUMN_NAME_REPRANGE, repRangeText);
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						values.put(ContractExercise.COLUMN_NAME_NAME, "Palms-Up Pull Downs");
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						values.put(ContractExercise.COLUMN_NAME_NAME, "Leg Presses");
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						values.put(ContractExercise.COLUMN_NAME_NAME, "Leg Extensions");
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						values.put(ContractExercise.COLUMN_NAME_NAME, "Leg Curls");
						db.insert(ContractExercise.TABLE_NAME, "null", values);
			}
		}
		Log.d("ProgramDbHelper","public void loadDefaultWorkout(SQLiteDatabase db) end");
	}

	public List<String> getAvailableProgramNames(){
		Log.d("ProgramDbHelper","public List<String> getAvailableProgramNames() called");
		List<String> programs = new ArrayList<String>();

		SQLiteDatabase db = this.getReadableDatabase();
		String[] projection = {
				ContractProgram.COLUMN_NAME_NAME
				};
		Cursor cursor = db.query(
				ContractProgram.TABLE_NAME,               // The table to query
				projection,                               // The columns to return
				null,                                     // The columns for the WHERE clause
				null,                                     // The values for the WHERE clause
				null,                                     // don't group the rows
				null,                                     // don't filter by row groups
				null                                      // The sort order
				);
		while(cursor.moveToNext()){
			String programName = cursor.getString(0);
			programs.add(programName);
		}

		cursor.close();
		Log.d("ProgramDbHelper","public List<String> getAvailableProgramNames() end");
		return programs;
	}

	public List<String> getAvailableSubProgramNames(String programName){
		Log.d("ProgramDbHelper","public List<String> getAvailableSubProgramNames(String programName) called");
		SQLiteDatabase db = this.getReadableDatabase();
		int idProgram = this.getIdProgram(programName);
		String[] projectionSubProgram = {
				ContractSubProgram.COLUMN_NAME_NAME,
				ContractSubProgram.COLUMN_NAME_EXTERN_ID
				};
		Cursor cursorSubProgram = db.query(ContractSubProgram.TABLE_NAME, projectionSubProgram,                               // The columns to return
								null, null, null, null, null);

		List<String> subPrograms = new ArrayList<String>();
		while(cursorSubProgram.moveToNext()){
			int idExtProgram = cursorSubProgram.getInt(1);
			if(idExtProgram == idProgram){
				String subProgramName = cursorSubProgram.getString(0);
				subPrograms.add(subProgramName);
			}
		}
		cursorSubProgram.close();
		Log.d("ProgramDbHelper","public List<String> getAvailableSubProgramNames(String programName) ends");
		return subPrograms;
	}

	protected int getIdProgram(String programName){
		Log.d("ProgramDbHelper", "protected int getIdProgram(String programName) called");
		SQLiteDatabase db = this.getReadableDatabase();
		String[] projectionProgram = {
				ContractProgram.COLUMN_NAME_ID,
				ContractProgram.COLUMN_NAME_NAME
				};
		Cursor cursorProgram = db.query(ContractProgram.TABLE_NAME,
										projectionProgram,
										null, null, null, null, null);
		int idProgram = 0;
		while(cursorProgram.moveToNext()){
			String currentProgramName = cursorProgram.getString(1);
			if(currentProgramName.equals(programName)){
				idProgram = cursorProgram.getInt(0);
			}
		}
		cursorProgram.close();
		Log.d("ProgramDbHelper", "protected int getIdProgram(String programName) end");
		return idProgram;
	}

	public boolean isSubProgramCompleted(String programName,
											String subProgramName){
		Log.d("ProgramDbHelper", "public boolean isSubProgramCompleted(...) called");
		SQLiteDatabase db = this.getReadableDatabase();
		int idSubProgram = this.getIdSubProgram(programName,
												subProgramName);
		String[] projectionSubProgram = {
				ContractSubProgram.COLUMN_NAME_ID,
				ContractSubProgram.COLUMN_NAME_COMPLETED
				};
		String[] selectionsArgs = {""+idSubProgram};
		Cursor cursorSubProgram = db.query(ContractSubProgram.TABLE_NAME,
											projectionSubProgram,                               // The columns to return
											ContractSubProgram.COLUMN_NAME_ID + "=?",
											selectionsArgs,
											null,
											null,null);
		boolean completed = false;
		if(cursorSubProgram.moveToNext()){
			completed = cursorSubProgram.getInt(1) > 0;
		}
		cursorSubProgram.close();
		Log.d("ProgramDbHelper", "public boolean isSubProgramCompleted(...) end");
		return completed;
		/*
		List<String> weeks = getAvailableWeeks(programName,
												subProgramName);
		for(String week:weeks){
			boolean weekCompleted = this.isWeekCompleted(programName,
														subProgramName,
														week);
			if(!weekCompleted){
				return false;
			}
		}
		return true;
		//*/
	}

	public List<String> getAvailableWeeks(String programName,
											String subProgramName){
		Log.d("ProgramDbHelper", "public List<String> getAvailableWeeks(...) called");
		SQLiteDatabase db = this.getReadableDatabase();
		int idSubProgram = this.getIdSubProgram(programName, subProgramName);
		String[] projectionWeek = {
				ContractWorkoutWeek.COLUMN_NAME_NAME,
				ContractWorkoutWeek.COLUMN_NAME_EXTERN_ID
				};
		Cursor cursorWeek = db.query(ContractWorkoutWeek.TABLE_NAME,
										projectionWeek,                               // The columns to return
										null, null, null, null, null);

		List<String> weeks = new ArrayList<String>();
		while(cursorWeek.moveToNext()){
			int idExtProgram = cursorWeek.getInt(1);
			if(idExtProgram == idSubProgram){
				String weekName = cursorWeek.getString(0);
				weeks.add(weekName);
			}
		}
		cursorWeek.close();
		Log.d("ProgramDbHelper", "public List<String> getAvailableWeeks(...) end");
		return weeks;
	}

	protected int getIdSubProgram(String programName,
									String subProgramName){
		Log.d("ProgramDbHelper", "protected int getIdSubProgram(...) called");
		SQLiteDatabase db = this.getReadableDatabase();
		int idProgram = this.getIdProgram(programName);
		String[] projectionSubProgram = {
				ContractSubProgram.COLUMN_NAME_ID,
				ContractSubProgram.COLUMN_NAME_NAME,
				ContractSubProgram.COLUMN_NAME_EXTERN_ID
				};
		Cursor cursorSubProgram = db.query(ContractSubProgram.TABLE_NAME,
											projectionSubProgram,                               // The columns to return
											null, null, null, null, null);
		int idSubProgram = 0;
		while(cursorSubProgram.moveToNext()){
			int idExtProgram = cursorSubProgram.getInt(2);
			if(idExtProgram == idProgram){
				String currentSubProgramName = cursorSubProgram.getString(1);
				if(currentSubProgramName.equals(subProgramName)){
					idSubProgram = cursorSubProgram.getInt(0);
				}
			}
		}
		cursorSubProgram.close();
		Log.d("ProgramDbHelper", "protected int getIdSubProgram(...) end");
		return idSubProgram;
	}
	
	public List<String> getAvailableDays(String programName,
										String subProgramName,
										String week){
		Log.d("ProgramDbHelper", "public List<String> getAvailableDays(...) called");
		SQLiteDatabase db = this.getReadableDatabase();
		int idWeek = this.getIdWeek(programName, subProgramName, week);
		String[] projectionDay = {
				ContractWorkoutDay.COLUMN_NAME_NAME,
				ContractWorkoutDay.COLUMN_NAME_EXTERN_ID
				};
		Cursor cursorDay = db.query(ContractWorkoutDay.TABLE_NAME,
									projectionDay,                               // The columns to return
									null, null, null, null, null);

		List<String> days = new ArrayList<String>();
		while(cursorDay.moveToNext()){
			int idExtWeek = cursorDay.getInt(1);
			if(idExtWeek == idWeek){
				String dayName = cursorDay.getString(0);
				days.add(dayName);
			}
		}
		cursorDay.close();
		Log.d("ProgramDbHelper", "public List<String> getAvailableDays(...) end");
		return days;
	}
	
	protected int getIdWeek(String programName,
							String subProgramName,
							String week){
		Log.d("ProgramDbHelper", "protected int getIdWeek(...) called");
		SQLiteDatabase db = this.getReadableDatabase();
		int idSubProgram = this.getIdSubProgram(programName, subProgramName);
		String[] projectionWeek = {
				ContractWorkoutWeek.COLUMN_NAME_ID,
				ContractWorkoutWeek.COLUMN_NAME_NAME,
				ContractWorkoutWeek.COLUMN_NAME_EXTERN_ID
				};
		Cursor cursorWeek = db.query(ContractWorkoutWeek.TABLE_NAME,
									projectionWeek,                               // The columns to return
									null, null, null, null, null);
		int idWeek = 0;
		while(cursorWeek.moveToNext()){
			int idExtSubProgram = cursorWeek.getInt(2);
			if(idExtSubProgram == idSubProgram){
				String currentWeek = cursorWeek.getString(1);
				if(currentWeek.equals(week)){
					idWeek = cursorWeek.getInt(0);
				}
			}
		}
		cursorWeek.close();
		Log.d("ProgramDbHelper", "protected int getIdWeek(...) end");
		return idWeek;
	}

	public boolean isWeekCompleted(String programName,
									String subProgramName,
									String week){
		Log.d("ProgramDbHelper", "public boolean isWeekCompleted(...) called");
		SQLiteDatabase db = this.getReadableDatabase();
		int idWeek = this.getIdWeek(programName,
									subProgramName,
									week);
		String[] projectionWeek = {
				ContractWorkoutWeek.COLUMN_NAME_ID,
				ContractWorkoutWeek.COLUMN_NAME_COMPLETED
				};
		String[] selectionsArgs = {""+idWeek};
		Cursor cursorWeek = db.query(ContractWorkoutWeek.TABLE_NAME,
									projectionWeek,                               // The columns to return
									ContractWorkoutWeek.COLUMN_NAME_ID + "=?",
									selectionsArgs,
									null,
									null,null);
		boolean completed = false;
		if(cursorWeek.moveToNext()){
			completed = cursorWeek.getInt(1) > 0;
		}
		cursorWeek.close();
		Log.d("ProgramDbHelper", "public boolean isWeekCompleted(...) end");
		return completed;
		/*
		List<String> days = getAvailableDays(programName,
											subProgramName,
											week);
		for(String day:days){
			boolean dayCompleted = this.isDayCompleted(programName,
														subProgramName,
														week,
														day);
			if(!dayCompleted){
				return false;
			}
		}
		return true;
		//*/
	}
	

	
	public List<Exercice> getAvailableExercices(String programName,
												String subProgramName,
												String week,
												String day){
		Log.d("ProgramDbHelper", "public List<Exercice> getAvailableExercices(...) called");
		int idDay = this.getIdDay(programName, subProgramName, week, day);
		List<Exercice> exercises = this.getAvailableExercices(programName,
															subProgramName,
															week,
															idDay);
		Log.d("ProgramDbHelper", "public List<Exercice> getAvailableExercices(...) end");
		return exercises;
	}

	public List<Exercice> getAvailableExercices(String programName,
												String subProgramName,
												String week,
												int idDay){
		Log.d("ProgramDbHelper", "public List<Exercice> getAvailableExercices id(...) called");
		List<Exercice> exercises = null;
		if(idDay != -1){
			SQLiteDatabase db = this.getReadableDatabase();
			String[] projectionExercice = {
					ContractExercise.COLUMN_NAME_ID,
					ContractExercise.COLUMN_NAME_NAME,
					ContractExercise.COLUMN_NAME_NREP,
					ContractExercise.COLUMN_NAME_WEIGHT,
					ContractExercise.COLUMN_NAME_REPRANGE,
					ContractExercise.COLUMN_NAME_REST,
					ContractExercise.COLUMN_NAME_EXTERN_ID
					};
			Cursor cursorExercise = db.query(ContractExercise.TABLE_NAME,
											projectionExercice,
											null,
											null,
											null,
											null,
											null);

			exercises = new ArrayList<Exercice>();
			while(cursorExercise.moveToNext()){
				int idExtDay = cursorExercise.getInt(6);
				if(idExtDay == idDay){
					int exerciceId = cursorExercise.getInt(0);
					String exerciseName = cursorExercise.getString(1);
					int nRep = cursorExercise.getInt(2);
					float weight = cursorExercise.getFloat(3);
					String repRange = cursorExercise.getString(4);
					int rest = cursorExercise.getInt(5);
					Exercice exercice = new Exercice(exerciceId, exerciseName, nRep, weight, repRange, rest);
					exercises.add(exercice);
				}
			}
			cursorExercise.close();
		}
		Log.d("ProgramDbHelper", "public List<Exercice> getAvailableExercices id(...) end");
		return exercises;
	}

	protected int getIdDay(String programName,
							String subProgramName,
							String week,
							String day){
		Log.d("ProgramDbHelper", "protected int getIdDay(...) called");
		SQLiteDatabase db = this.getReadableDatabase();
		int idWeek = this.getIdWeek(programName, subProgramName, week);
		String[] projectionDay = {
				ContractWorkoutDay.COLUMN_NAME_ID,
				ContractWorkoutDay.COLUMN_NAME_NAME,
				ContractWorkoutDay.COLUMN_NAME_EXTERN_ID
				};
		Cursor cursorDay = db.query(ContractWorkoutDay.TABLE_NAME, projectionDay,                               // The columns to return
								null, null, null, null, null);
		int idDay = 0;
		while(cursorDay.moveToNext()){
			int idExtWeek = cursorDay.getInt(2);
			if(idExtWeek == idWeek){
				String currentDay = cursorDay.getString(1);
				if(currentDay.equals(day)){
					idDay = cursorDay.getInt(0);
				}
			}
		}
		cursorDay.close();
		Log.d("ProgramDbHelper", "protected int getIdDay(...) end");
		return idDay;
	}
	
	public List<Exercice> getPreviousExercices(String programName,
												String subProgramName,
												String week,
												String day){
		Log.d("ProgramDbHelper", "public List<Exercice> getPreviousExercices(...) called");
		int idPreviousDay = this.getIdPreviousDay(programName, subProgramName, week, day);
		List<Exercice> exercises = this.getAvailableExercices(programName,
				subProgramName,
				week,
				idPreviousDay);
		Log.d("ProgramDbHelper", "public List<Exercice> getPreviousExercices(...) end");
		return exercises;
	}
	
	protected int getIdPreviousDay(String programName,
									String subProgramName,
									String week,
									String day){
		Log.d("ProgramDbHelper", "protected int getIdPreviousDay(...) called");
		SQLiteDatabase db = this.getReadableDatabase();
		int idWeek = this.getIdWeek(programName, subProgramName, week);
		String[] projectionDay = {
				ContractWorkoutDay.COLUMN_NAME_ID,
				ContractWorkoutDay.COLUMN_NAME_NAME,
				ContractWorkoutDay.COLUMN_NAME_EXTERN_ID
				};
		Cursor cursorDay = db.query(ContractWorkoutDay.TABLE_NAME, projectionDay,                               // The columns to return
								null, null, null, null, null);
		Stack<Integer> previousDays = new Stack<Integer>();
		List<Exercice> currentExercises = null;
		while(cursorDay.moveToNext()){
			int idExtWeek = cursorDay.getInt(2);
			if(idExtWeek == idWeek){
				String currentDay = cursorDay.getString(1);
				int currentIdDay = cursorDay.getInt(0);
				if(!currentDay.equals(day)){
					previousDays.addElement(currentIdDay);
				}else{
					currentExercises = this.getAvailableExercices(programName,
							subProgramName,
							week,
							currentIdDay);
					break;
				}
			}else if(idExtWeek == idWeek-1){
				int currentIdDay = cursorDay.getInt(0);
				previousDays.addElement(currentIdDay);
			}
		}
		cursorDay.close();
		int idPreviousDay = -1;
		for(Integer previousDay:previousDays){
			List<Exercice> exercises = this.getAvailableExercices(programName,
					subProgramName,
					week,
					previousDay);
			if(exercisesListEquals(exercises, currentExercises)){
				idPreviousDay = previousDay;
				break;
			}
		}
		Log.d("ProgramDbHelper", "protected int getIdPreviousDay(...) ends");
		return idPreviousDay;
	}
	
	static protected boolean exercisesListEquals(List<Exercice> exercises1, List<Exercice> exercises2){
		Log.d("ProgramDbHelper", "static protected boolean exercisesListEquals(List<Exercice> exercises1, List<Exercice> exercises2) called");
		if(exercises1 != null
		&& exercises2 != null
		&& exercises1.size() == exercises2.size()){
			int nExercises = exercises1.size();
			for(int i=0; i<nExercises; i++){
				String name1 = exercises1.get(i).getName();
				String name2 = exercises2.get(i).getName();
				if(!name1.equals(name2)){
					Log.d("ProgramDbHelper", "static protected boolean exercisesListEquals(List<Exercice> exercises1, List<Exercice> exercises2) end false 1");
					return false;
				}
			}
		}else{
			Log.d("ProgramDbHelper", "static protected boolean exercisesListEquals(List<Exercice> exercises1, List<Exercice> exercises2) end false 2");
			return false;
		}
		Log.d("ProgramDbHelper", "static protected boolean exercisesListEquals(List<Exercice> exercises1, List<Exercice> exercises2) end true3");
		return true;
	}
	
	public boolean isDayCompleted(String programName,
									String subProgramName,
									String week,
									String day){
		Log.d("ProgramDbHelper", "public boolean isDayCompleted(...) called");
		SQLiteDatabase db = this.getReadableDatabase();
		int idDay = this.getIdDay(programName,
									subProgramName,
									week,
									day);
		String[] projectionDay = {
				ContractWorkoutDay.COLUMN_NAME_ID,
				ContractWorkoutDay.COLUMN_NAME_COMPLETED
				};
		String[] selectionsArgs = {""+idDay};
		Cursor cursorDay = db.query(ContractWorkoutDay.TABLE_NAME,
									projectionDay,                               // The columns to return
									ContractWorkoutDay.COLUMN_NAME_ID + "=?",
									selectionsArgs,
									null,
									null,null);
		boolean completed = false;
		if(cursorDay.moveToNext()){
			completed = cursorDay.getInt(1) > 0;
		}
		cursorDay.close();
		Log.d("ProgramDbHelper", "public boolean isDayCompleted(...) end");
		return completed;
		/*
		List<Exercice> exercises = getAvailableExercices(programName,
														subProgramName,
														week,
														day);
		for(Exercice exercise:exercises){
			if(!exercise.isDone()){
				return false;
			}
		}
		return true;
		//*/
	}
	
	//*
	public boolean isExerciseCompleted(int exerciseId){
		Log.d("ProgramDbHelper", "public boolean isExerciseCompleted(int exerciseId) called");
		SQLiteDatabase db = this.getReadableDatabase();
		String[] projectionExercice = {
				ContractExercise.COLUMN_NAME_ID,
				ContractExercise.COLUMN_NAME_COMPLETED
				};
		Cursor cursorExercise = db.query(ContractExercise.TABLE_NAME,
										projectionExercice,
										null,
										null,
										null,
										null,
										null);
		//TODO optimize
		while(cursorExercise.moveToNext()){
			int currentExerciseId = cursorExercise.getInt(0);
			if(currentExerciseId == exerciseId){
				boolean completed = cursorExercise.getInt(1) > 0;
				/*
				String exerciseName = cursorExercise.getString(1);
				int nRep = cursorExercise.getInt(2);
				float weight = cursorExercise.getFloat(3);
				String repRange = cursorExercise.getString(4);
				int rest = cursorExercise.getInt(5);
				Exercice exercise = new Exercice(currentExerciseId,
												exerciseName,
												nRep,
												weight,
												repRange,
												rest);
				boolean completed = exercise.isDone();
				//*/
				Log.d("ProgramDbHelper", "public boolean isExerciseCompleted(int exerciseId) end completed");
				return completed;
			}
		}
		cursorExercise.close();
		Log.d("ProgramDbHelper", "public boolean isExerciseCompleted(int exerciseId) end false");
		return false;
	}
	
	protected void setExerciceInfo(String programName,
									String subProgramName,
									String week,
									String day,
									String exerciseId,
									String rest,
									String weight,
									String nReps){
		Log.d("ProgramDbHelper", "protected void setExerciceInfo(...) called");
		int exerciseIdInt = Integer.parseInt(exerciseId);
		boolean oldCompleted = this.isExerciseCompleted(exerciseIdInt);
		boolean completed = false;
		if(!weight.equals("") && !nReps.equals("")){
			float weightFloat = Float.parseFloat(weight);
			int nRepsInt = Integer.parseInt(nReps);
			completed = weightFloat > 0.f && nRepsInt > 0;
		}
		if(oldCompleted != completed){
			ContentValues values = new ContentValues();
			values.put(ContractExercise.COLUMN_NAME_REST, rest);
			values.put(ContractExercise.COLUMN_NAME_WEIGHT, weight);
			values.put(ContractExercise.COLUMN_NAME_NREP, nReps);
	
			values.put(ContractExercise.COLUMN_NAME_COMPLETED, completed);
			SQLiteDatabase db = this.getWritableDatabase();
			db.update(ContractExercise.TABLE_NAME,
						values, ContractExercise.COLUMN_NAME_ID + "=" + exerciseId,
						null);
			db.close();
			this.updateDayCompletedEventually(programName,
									subProgramName,
									week,
									day);
		}
		Log.d("ProgramDbHelper", "protected void setExerciceInfo(...) end");
	}
	
	protected void updateDayCompletedEventually(String programName,
										String subProgramName,
										String week,
										String day){
		Log.d("ProgramDbHelper", "protected void updateDayCompletedEventually(...) called");
		boolean oldCompleted = this.isDayCompleted(programName,
													subProgramName,
													week,
													day);
		boolean completed = true;
		List<Exercice> exercises = getAvailableExercices(programName,
														subProgramName,
														week,
														day);
		for(Exercice exercise:exercises){
			if(!exercise.isDone()){
				completed = false;
				break;
			}
		}
		if(oldCompleted != completed){
			ContentValues values = new ContentValues();
			values.put(ContractWorkoutDay.COLUMN_NAME_COMPLETED, completed);
			SQLiteDatabase db = this.getWritableDatabase();
			int idDay = this.getIdDay(programName,
										subProgramName,
										week,
										day);
			db.update(ContractWorkoutDay.TABLE_NAME,
						values, ContractWorkoutDay.COLUMN_NAME_ID + "=" + idDay,
						null);
			db.close();
			this.updateWeekCompletedEventually(programName,
												subProgramName,
												week);
			Log.d("ProgramDbHelper", "protected void updateDayCompletedEventually(...) end");
		}
	}
	//-------------------------------------------------------------
	protected void updateWeekCompletedEventually(String programName,
												String subProgramName,
												String week){
		Log.d("ProgramDbHelper", "protected void updateWeekCompletedEventually(...) called");
		boolean oldCompleted = this.isWeekCompleted(programName,
													subProgramName,
													week);
		boolean completed = true;
		List<String> days = getAvailableDays(programName,
												subProgramName,
												week);
		for(String day:days){
			boolean dayCompleted = this.isDayCompleted(programName, subProgramName, week, day);
			if(!dayCompleted){
				completed = false;
				break;
			}
		}
		if(oldCompleted != completed){
			ContentValues values = new ContentValues();
			values.put(ContractWorkoutWeek.COLUMN_NAME_COMPLETED, completed);
			SQLiteDatabase db = this.getWritableDatabase();
			int idWeek = this.getIdWeek(programName,
										subProgramName,
										week);
			db.update(ContractWorkoutWeek.TABLE_NAME,
						values, ContractWorkoutWeek.COLUMN_NAME_ID + "=" + idWeek,
						null);
			db.close();
			this.updateSubProgramCompletedEventually(programName,
													subProgramName);
		}
		Log.d("ProgramDbHelper", "protected void updateWeekCompletedEventually(...) end");
	}
	//-------------------------------------------------------------
	protected void updateSubProgramCompletedEventually(String programName,
														String subProgramName){
		Log.d("ProgramDbHelper", "protected void updateSubProgramCompletedEventually(...) called");
		boolean oldCompleted = this.isSubProgramCompleted(programName,
														subProgramName);
		boolean completed = true;
		List<String> weeks = getAvailableWeeks(programName,
												subProgramName);
		for(String week:weeks){
			boolean weekCompleted = this.isWeekCompleted(programName,
														subProgramName,
														week);
			if(!weekCompleted){
				completed = false;
				break;
			}
		}
		if(oldCompleted != completed){
			ContentValues values = new ContentValues();
			values.put(ContractSubProgram.COLUMN_NAME_COMPLETED, completed);
			SQLiteDatabase db = this.getWritableDatabase();
			int idSubProgram = this.getIdSubProgram(programName,
													subProgramName);
			db.update(ContractSubProgram.TABLE_NAME,
						values, ContractSubProgram.COLUMN_NAME_ID + "=" + idSubProgram,
						null);
			db.close();
		}
		Log.d("ProgramDbHelper", "protected void updateSubProgramCompletedEventually(...) end");
	}

}

