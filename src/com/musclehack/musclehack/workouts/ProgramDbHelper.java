package com.musclehack.musclehack.workouts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.musclehack.musclehack.workouts.ContractProgram.ContractExercise;
import com.musclehack.musclehack.workouts.ContractProgram.ContractWorkoutDay;
import com.musclehack.musclehack.workouts.ContractProgram.ContractWorkoutWeek;
import com.musclehack.musclehack.workouts.ContractProgram.DeleteV01;


public class ProgramDbHelper extends SQLiteOpenHelper {
	public static final int DATABASE_VERSION = 2;
	public static final String DATABASE_NAME = "programs.db";
	protected Context context;

	public ProgramDbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		Log.d("ProgramDbHelper","public ProgramDbHelper(Context context) called");
		this.context = context;
		ProgramDbHelper.backupDatabase();
		Log.d("ProgramDbHelper","public ProgramDbHelper(Context context) end");
	}
	
	@Override
	public void onOpen(SQLiteDatabase db) {
	    super.onOpen(db);
	    if (!db.isReadOnly()) {
	        // Enable foreign key constraints
	        db.execSQL("PRAGMA foreign_keys=ON;");
	    }
	}
	
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.d("ProgramDbHelper", "public void onUpgrade(…) called");
		if(oldVersion == 1){
			Log.d("ProgramDbHelper", "Deleting version "
									+ oldVersion
									+ " to create "
									+ newVersion);
			Log.d("ProgramDbHelper", DeleteV01.DROP_TABLE);
			db.execSQL(DeleteV01.DROP_TABLE);
			this.onCreate(db);
		}
		Log.d("ProgramDbHelper", "public void onUpgrade(…) end");
	}
	
	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//onUpgrade(db, oldVersion, newVersion);
	}
	
	public static void backupDatabase() {
	
		/*
		//Open your local db as the input stream
		String inFileName = "/data/data/com.musclehack.musclehack/databases/programs.db";
		File dbFile = new File(inFileName);
		FileInputStream fis;
		try {
			fis = new FileInputStream(dbFile);
			Calendar c = Calendar.getInstance(); 
			int seconds = c.get(Calendar.SECOND);
			int min = c.get(Calendar.MINUTE);
			int hour = c.get(Calendar.HOUR);
			String backupDatabaseName = "programs_" + hour + "h" + min + "m" + seconds + ".db";	
			String outFileName = Environment.getExternalStorageDirectory() + "/" + backupDatabaseName;
			//Open the empty db as the output stream
			OutputStream output = new FileOutputStream(outFileName);
			//transfer bytes from the inputfile to the outputfile
			byte[] buffer = new byte[1024];
			int length;
			while ((length = fis.read(buffer))>0){
			    output.write(buffer, 0, length);
			}
			//Close the streams
			output.flush();
			output.close();
			fis.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//*/
	}
	
	public void onCreate(SQLiteDatabase db) {
		Log.d("ProgramDbHelper","public void onCreate(SQLiteDatabase db) called");
		db.execSQL(ContractProgram.CREATE_TABLE);
		db.execSQL(ContractWorkoutWeek.CREATE_TABLE);
		db.execSQL(ContractWorkoutDay.CREATE_TABLE);
		db.execSQL(ContractExercise.CREATE_TABLE);
		this.loadDefaultWorkout(db);
		Log.d("ProgramDbHelper","public void onCreate(SQLiteDatabase db) end");
	}
	
	public void loadDefaultWorkout(SQLiteDatabase db){
		Log.d("ProgramDbHelper","public void loadDefaultWorkout(SQLiteDatabase db) called");
		String[] repRanges = {"8-12", "7-10", "6-8"};
		ContentValues values = new ContentValues();
		int nWeeks = 10;
		int orderExercise = 0;
		//TODO add order
		for(String repRange:repRanges){
			String[] repRangeSep = repRange.split("-");
			String repRangeText = repRangeSep[0] + " to " + repRangeSep[1];
			values = new ContentValues();
			values.put(ContractProgram.COLUMN_NAME_NAME, "THT5 VOLUME " + repRange);
			values.put(ContractProgram.COLUMN_NAME_COMPLETED, false);
			long newRowProgramId = db.insert(ContractProgram.TABLE_NAME, "null", values);
			for(int i=0; i<nWeeks; i++){
				values = new ContentValues();
				values.put(ContractWorkoutWeek.COLUMN_NAME_NAME, "Week " + (i+1));
				values.put(ContractWorkoutWeek.COLUMN_NAME_EXTERN_ID, newRowProgramId);
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
						values.put(ContractExercise.COLUMN_NAME_ORDER, orderExercise++);
						values.put(ContractExercise.COLUMN_NAME_COMPLETED, false);
						values.put(ContractExercise.COLUMN_NAME_NREP, 0);
						values.put(ContractExercise.COLUMN_NAME_WEIGHT, 0.f);
						values.put(ContractExercise.COLUMN_NAME_REPRANGE, repRangeText);
						values.put(ContractExercise.COLUMN_NAME_REST, "180");
						values.put(ContractExercise.COLUMN_NAME_NAME, "Overhead Barbell Press");
						for(int j=0; j<4; j++){
							db.insert(ContractExercise.TABLE_NAME, "null", values);
							values.put(ContractExercise.COLUMN_NAME_ORDER, orderExercise++);
						}
						values.put(ContractExercise.COLUMN_NAME_NAME, "Overhead Dumbbell Press");
						values.put(ContractExercise.COLUMN_NAME_REST, "120");
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						values.put(ContractExercise.COLUMN_NAME_ORDER, orderExercise++);
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						values.put(ContractExercise.COLUMN_NAME_ORDER, orderExercise++);
						values.put(ContractExercise.COLUMN_NAME_REPRANGE, "8 to 12");
						values.put(ContractExercise.COLUMN_NAME_NAME, "Dumbbell Lateral Raises");
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						values.put(ContractExercise.COLUMN_NAME_ORDER, orderExercise++);
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						values.put(ContractExercise.COLUMN_NAME_ORDER, orderExercise++);
						values.put(ContractExercise.COLUMN_NAME_NAME, "Dumbbell Front Raises");
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						values.put(ContractExercise.COLUMN_NAME_ORDER, orderExercise++);
						values.put(ContractExercise.COLUMN_NAME_REST, "240");
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						values.put(ContractExercise.COLUMN_NAME_ORDER, orderExercise++);
						values.put(ContractExercise.COLUMN_NAME_NAME, "Shrugs");
						values.put(ContractExercise.COLUMN_NAME_REST, "120");
						values.put(ContractExercise.COLUMN_NAME_REPRANGE, "12 to 15");
						for(int j=0; j<4; j++){
							db.insert(ContractExercise.TABLE_NAME, "null", values);
							values.put(ContractExercise.COLUMN_NAME_ORDER, orderExercise++);
						}
					values = new ContentValues();
					values.put(ContractWorkoutDay.COLUMN_NAME_NAME, "Tuesday - Legs");
					values.put(ContractWorkoutDay.COLUMN_NAME_COMPLETED, false);
					values.put(ContractWorkoutDay.COLUMN_NAME_DAY_OF_WEEK, 1);
					values.put(ContractWorkoutDay.COLUMN_NAME_EXTERN_ID, newRowWeekId);
					newRowDayId = db.insert(ContractWorkoutDay.TABLE_NAME, "null", values);
						values = new ContentValues();
						orderExercise = 0;
						values.put(ContractExercise.COLUMN_NAME_EXTERN_ID, newRowDayId);
						values.put(ContractExercise.COLUMN_NAME_ORDER, orderExercise++);
						values.put(ContractExercise.COLUMN_NAME_COMPLETED, false);
						values.put(ContractExercise.COLUMN_NAME_NREP, 0);
						values.put(ContractExercise.COLUMN_NAME_WEIGHT, 0.f);
						values.put(ContractExercise.COLUMN_NAME_REPRANGE, repRangeText);
						values.put(ContractExercise.COLUMN_NAME_REST, "180");
						values.put(ContractExercise.COLUMN_NAME_NAME, "Squats");
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						values.put(ContractExercise.COLUMN_NAME_ORDER, orderExercise++);
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						values.put(ContractExercise.COLUMN_NAME_ORDER, orderExercise++);
						values.put(ContractExercise.COLUMN_NAME_NAME, "Leg Press");
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						values.put(ContractExercise.COLUMN_NAME_ORDER, orderExercise++);
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						values.put(ContractExercise.COLUMN_NAME_ORDER, orderExercise++);
						values.put(ContractExercise.COLUMN_NAME_NAME, "Stiff Leg Deadlifts");
						values.put(ContractExercise.COLUMN_NAME_REPRANGE, "6 to 8");
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						values.put(ContractExercise.COLUMN_NAME_ORDER, orderExercise++);
						values.put(ContractExercise.COLUMN_NAME_REST, "120");
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						values.put(ContractExercise.COLUMN_NAME_ORDER, orderExercise++);
						values.put(ContractExercise.COLUMN_NAME_NAME, "Stiff Leg Extensions");
						values.put(ContractExercise.COLUMN_NAME_REPRANGE, "8 to 12");
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						values.put(ContractExercise.COLUMN_NAME_ORDER, orderExercise++);
						values.put(ContractExercise.COLUMN_NAME_NAME, "Stiff Leg Curls");
						values.put(ContractExercise.COLUMN_NAME_REST, "180");
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						values.put(ContractExercise.COLUMN_NAME_ORDER, orderExercise++);
						values.put(ContractExercise.COLUMN_NAME_NAME, "Calf Raises");
						values.put(ContractExercise.COLUMN_NAME_REST, "120");
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						values.put(ContractExercise.COLUMN_NAME_ORDER, orderExercise++);
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						values.put(ContractExercise.COLUMN_NAME_ORDER, orderExercise++);
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						values.put(ContractExercise.COLUMN_NAME_ORDER, orderExercise++);
					values = new ContentValues();
					values.put(ContractWorkoutDay.COLUMN_NAME_NAME, "Wednesday - Triceps, Biceps & Forearms");
					values.put(ContractWorkoutDay.COLUMN_NAME_COMPLETED, false);
					values.put(ContractWorkoutDay.COLUMN_NAME_DAY_OF_WEEK, 2);
					values.put(ContractWorkoutDay.COLUMN_NAME_EXTERN_ID, newRowWeekId);
					newRowDayId = db.insert(ContractWorkoutDay.TABLE_NAME, "null", values);
						orderExercise = 0;
						values = new ContentValues();
						values.put(ContractExercise.COLUMN_NAME_EXTERN_ID, newRowDayId);
						values.put(ContractExercise.COLUMN_NAME_ORDER, orderExercise++);
						values.put(ContractExercise.COLUMN_NAME_COMPLETED, false);
						values.put(ContractExercise.COLUMN_NAME_NREP, 0);
						values.put(ContractExercise.COLUMN_NAME_WEIGHT, 0.f);
						values.put(ContractExercise.COLUMN_NAME_REPRANGE, repRangeText);
						values.put(ContractExercise.COLUMN_NAME_REST, "120");
						values.put(ContractExercise.COLUMN_NAME_NAME, "Decline Tricep Extensions");
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						values.put(ContractExercise.COLUMN_NAME_ORDER, orderExercise++);
						values.put(ContractExercise.COLUMN_NAME_REST, "180");
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						values.put(ContractExercise.COLUMN_NAME_ORDER, orderExercise++);
						values.put(ContractExercise.COLUMN_NAME_REPRANGE, "8 to 12");
						values.put(ContractExercise.COLUMN_NAME_REST, "120");
						values.put(ContractExercise.COLUMN_NAME_NAME, "Tricep Cable Push Downs");
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						values.put(ContractExercise.COLUMN_NAME_ORDER, orderExercise++);
						values.put(ContractExercise.COLUMN_NAME_REST, "180");
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						values.put(ContractExercise.COLUMN_NAME_ORDER, orderExercise++);
						values.put(ContractExercise.COLUMN_NAME_REST, "120");
						values.put(ContractExercise.COLUMN_NAME_NAME, "Cable Bent-Over Triceps Extensions");
						values.put(ContractExercise.COLUMN_NAME_REPRANGE, "12 to 14");
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						values.put(ContractExercise.COLUMN_NAME_ORDER, orderExercise++);
						values.put(ContractExercise.COLUMN_NAME_REST, "180");
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						values.put(ContractExercise.COLUMN_NAME_ORDER, orderExercise++);
						values.put(ContractExercise.COLUMN_NAME_REST, "120");
						values.put(ContractExercise.COLUMN_NAME_NAME, "McManus Pushdown");
						values.put(ContractExercise.COLUMN_NAME_REPRANGE, "8 to 12");
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						values.put(ContractExercise.COLUMN_NAME_ORDER, orderExercise++);
						values.put(ContractExercise.COLUMN_NAME_REST, "180");
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						values.put(ContractExercise.COLUMN_NAME_ORDER, orderExercise++);
						values.put(ContractExercise.COLUMN_NAME_REST, "120");
						values.put(ContractExercise.COLUMN_NAME_NAME, "Cable Preacher Curls");
						values.put(ContractExercise.COLUMN_NAME_REPRANGE, repRangeText);
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						values.put(ContractExercise.COLUMN_NAME_ORDER, orderExercise++);
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						values.put(ContractExercise.COLUMN_NAME_ORDER, orderExercise++);
						values.put(ContractExercise.COLUMN_NAME_REST, "180");
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						values.put(ContractExercise.COLUMN_NAME_ORDER, orderExercise++);
						values.put(ContractExercise.COLUMN_NAME_REST, "120");
						values.put(ContractExercise.COLUMN_NAME_NAME, "Straight Barbell Curls");
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						values.put(ContractExercise.COLUMN_NAME_ORDER, orderExercise++);
						values.put(ContractExercise.COLUMN_NAME_REST, "180");
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						values.put(ContractExercise.COLUMN_NAME_ORDER, orderExercise++);
						values.put(ContractExercise.COLUMN_NAME_REST, "120");
						values.put(ContractExercise.COLUMN_NAME_NAME, "EZ/Curl Bar Curls");
						values.put(ContractExercise.COLUMN_NAME_REPRANGE, "8 to 12");
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						values.put(ContractExercise.COLUMN_NAME_ORDER, orderExercise++);
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						values.put(ContractExercise.COLUMN_NAME_ORDER, orderExercise++);
						values.put(ContractExercise.COLUMN_NAME_NAME, "Seated Incline Dumbbell Curls");
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						values.put(ContractExercise.COLUMN_NAME_ORDER, orderExercise++);
						values.put(ContractExercise.COLUMN_NAME_NAME, "Pinwheel Curls");
						values.put(ContractExercise.COLUMN_NAME_REPRANGE, "14 to 16");
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						values.put(ContractExercise.COLUMN_NAME_ORDER, orderExercise++);
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						values.put(ContractExercise.COLUMN_NAME_ORDER, orderExercise++);
						values.put(ContractExercise.COLUMN_NAME_NAME, "Barbell Wrist Curls");
						values.put(ContractExercise.COLUMN_NAME_REPRANGE, "8 to 12");
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						values.put(ContractExercise.COLUMN_NAME_ORDER, orderExercise++);
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						values.put(ContractExercise.COLUMN_NAME_ORDER, orderExercise++);
					values = new ContentValues();
					values.put(ContractWorkoutDay.COLUMN_NAME_NAME, "Thursday - Back");
					values.put(ContractWorkoutDay.COLUMN_NAME_COMPLETED, false);
					values.put(ContractWorkoutDay.COLUMN_NAME_DAY_OF_WEEK, 3);
					values.put(ContractWorkoutDay.COLUMN_NAME_EXTERN_ID, newRowWeekId);
					newRowDayId = db.insert(ContractWorkoutDay.TABLE_NAME, "null", values);
						values = new ContentValues();
						orderExercise = 0;
						values.put(ContractExercise.COLUMN_NAME_EXTERN_ID, newRowDayId);
						values.put(ContractExercise.COLUMN_NAME_ORDER, orderExercise++);
						values.put(ContractExercise.COLUMN_NAME_COMPLETED, false);
						values.put(ContractExercise.COLUMN_NAME_NREP, 0);
						values.put(ContractExercise.COLUMN_NAME_WEIGHT, 0.f);
						values.put(ContractExercise.COLUMN_NAME_REPRANGE, "4 to 6");
						values.put(ContractExercise.COLUMN_NAME_REST, "180");
						values.put(ContractExercise.COLUMN_NAME_NAME, "Deadlift");
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						values.put(ContractExercise.COLUMN_NAME_ORDER, orderExercise++);
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						values.put(ContractExercise.COLUMN_NAME_ORDER, orderExercise++);
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						values.put(ContractExercise.COLUMN_NAME_ORDER, orderExercise++);
						values.put(ContractExercise.COLUMN_NAME_NAME, "Seated Cable Rows");
						values.put(ContractExercise.COLUMN_NAME_REPRANGE, "8 to 12");
						values.put(ContractExercise.COLUMN_NAME_REST, "120");
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						values.put(ContractExercise.COLUMN_NAME_ORDER, orderExercise++);
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						values.put(ContractExercise.COLUMN_NAME_ORDER, orderExercise++);
						values.put(ContractExercise.COLUMN_NAME_REST, "180");
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						values.put(ContractExercise.COLUMN_NAME_ORDER, orderExercise++);
						values.put(ContractExercise.COLUMN_NAME_NAME, "Unilateral One-arm Dumbbell Rows");
						values.put(ContractExercise.COLUMN_NAME_REST, "120");
						values.put(ContractExercise.COLUMN_NAME_REPRANGE, repRangeText);
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						values.put(ContractExercise.COLUMN_NAME_ORDER, orderExercise++);
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						values.put(ContractExercise.COLUMN_NAME_ORDER, orderExercise++);
					values = new ContentValues();
					values.put(ContractWorkoutDay.COLUMN_NAME_NAME, "Friday - Chest & Abs");
					values.put(ContractWorkoutDay.COLUMN_NAME_COMPLETED, false);
					values.put(ContractWorkoutDay.COLUMN_NAME_DAY_OF_WEEK, 4);
					values.put(ContractWorkoutDay.COLUMN_NAME_EXTERN_ID, newRowWeekId);
					newRowDayId = db.insert(ContractWorkoutDay.TABLE_NAME, "null", values);
						orderExercise = 0;
						values = new ContentValues();
						values.put(ContractExercise.COLUMN_NAME_EXTERN_ID, newRowDayId);
						values.put(ContractExercise.COLUMN_NAME_ORDER, orderExercise++);
						values.put(ContractExercise.COLUMN_NAME_COMPLETED, false);
						values.put(ContractExercise.COLUMN_NAME_NREP, 0);
						values.put(ContractExercise.COLUMN_NAME_WEIGHT, 0.f);
						values.put(ContractExercise.COLUMN_NAME_REPRANGE, repRangeText);
						values.put(ContractExercise.COLUMN_NAME_REST, "120");
						values.put(ContractExercise.COLUMN_NAME_NAME, "Flat Barbell Bench Press");
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						values.put(ContractExercise.COLUMN_NAME_ORDER, orderExercise++);
						values.put(ContractExercise.COLUMN_NAME_REST, "180");
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						values.put(ContractExercise.COLUMN_NAME_ORDER, orderExercise++);
						values.put(ContractExercise.COLUMN_NAME_REST, "120");
						values.put(ContractExercise.COLUMN_NAME_NAME, "Pec Deck Or Cable Crossovers");
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						values.put(ContractExercise.COLUMN_NAME_ORDER, orderExercise++);
						values.put(ContractExercise.COLUMN_NAME_REST, "180");
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						values.put(ContractExercise.COLUMN_NAME_ORDER, orderExercise++);
						values.put(ContractExercise.COLUMN_NAME_REST, "120");
						values.put(ContractExercise.COLUMN_NAME_REPRANGE, "8 to 12");
						values.put(ContractExercise.COLUMN_NAME_NAME, "Deep Dips");
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						values.put(ContractExercise.COLUMN_NAME_ORDER, orderExercise++);
						values.put(ContractExercise.COLUMN_NAME_REST, "180");
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						values.put(ContractExercise.COLUMN_NAME_ORDER, orderExercise++);
						values.put(ContractExercise.COLUMN_NAME_REST, "120");
						values.put(ContractExercise.COLUMN_NAME_NAME, "Incline Barbell Bench Press");
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						values.put(ContractExercise.COLUMN_NAME_ORDER, orderExercise++);
						values.put(ContractExercise.COLUMN_NAME_REST, "180");
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						values.put(ContractExercise.COLUMN_NAME_ORDER, orderExercise++);
						values.put(ContractExercise.COLUMN_NAME_REST, "120");
						values.put(ContractExercise.COLUMN_NAME_NAME, "Kneeling Cable Crunches");
						values.put(ContractExercise.COLUMN_NAME_REPRANGE, "12 to 15");
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						values.put(ContractExercise.COLUMN_NAME_ORDER, orderExercise++);
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						values.put(ContractExercise.COLUMN_NAME_ORDER, orderExercise++);
						values.put(ContractExercise.COLUMN_NAME_REST, "180");
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						values.put(ContractExercise.COLUMN_NAME_ORDER, orderExercise++);
						values.put(ContractExercise.COLUMN_NAME_REST, "120");
						values.put(ContractExercise.COLUMN_NAME_REPRANGE, "8 to 12");
						values.put(ContractExercise.COLUMN_NAME_NAME, "Decline Sit-ups");
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						values.put(ContractExercise.COLUMN_NAME_ORDER, orderExercise++);
						values.put(ContractExercise.COLUMN_NAME_REST, "180");
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						values.put(ContractExercise.COLUMN_NAME_ORDER, orderExercise++);
						values.put(ContractExercise.COLUMN_NAME_REST, "120");
						values.put(ContractExercise.COLUMN_NAME_NAME, "Weighted Reverse Crunches");
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						values.put(ContractExercise.COLUMN_NAME_ORDER, orderExercise++);
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						values.put(ContractExercise.COLUMN_NAME_ORDER, orderExercise++);
			}
		}
		
		//values = new ContentValues();
		//values.put(ContractProgram.COLUMN_NAME_NAME, "THT5 HIT CYCLES");
		//newRowProgramId = db.insert(ContractProgram.TABLE_NAME, "null", values);
		for(String repRange:repRanges){
			String[] repRangeSep = repRange.split("-");
			String repRangeText = repRangeSep[0] + " to " + repRangeSep[1];
			values = new ContentValues();
			values.put(ContractProgram.COLUMN_NAME_NAME, "THT5 HIT " + repRange);
			values.put(ContractProgram.COLUMN_NAME_COMPLETED, false);
			long newRowProgramId = db.insert(ContractProgram.TABLE_NAME, "null", values);
			for(int i=0; i<nWeeks; i++){
				values = new ContentValues();
				values.put(ContractWorkoutWeek.COLUMN_NAME_NAME, "Week " + (i+1));
				values.put(ContractWorkoutWeek.COLUMN_NAME_COMPLETED, false);
				values.put(ContractWorkoutWeek.COLUMN_NAME_EXTERN_ID, newRowProgramId);
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
							orderExercise++;
							values = new ContentValues();
							values.put(ContractExercise.COLUMN_NAME_EXTERN_ID, newRowDayId);
							values.put(ContractExercise.COLUMN_NAME_COMPLETED, false);
							values.put(ContractExercise.COLUMN_NAME_NREP, 0);
							values.put(ContractExercise.COLUMN_NAME_WEIGHT, 0.f);
							values.put(ContractExercise.COLUMN_NAME_REPRANGE, repRangeText);
							values.put(ContractExercise.COLUMN_NAME_REST, "180");
							values.put(ContractExercise.COLUMN_NAME_NAME, "Squats or Leg Presses");
							values.put(ContractExercise.COLUMN_NAME_ORDER, orderExercise++);
							db.insert(ContractExercise.TABLE_NAME, "null", values);
							values.put(ContractExercise.COLUMN_NAME_ORDER, orderExercise++);
							db.insert(ContractExercise.TABLE_NAME, "null", values);
							values.put(ContractExercise.COLUMN_NAME_NAME, "Bench Press");
							values.put(ContractExercise.COLUMN_NAME_REST, "120");
							values.put(ContractExercise.COLUMN_NAME_ORDER, orderExercise++);
							db.insert(ContractExercise.TABLE_NAME, "null", values);
							values.put(ContractExercise.COLUMN_NAME_REST, "180");
							values.put(ContractExercise.COLUMN_NAME_ORDER, orderExercise++);
							db.insert(ContractExercise.TABLE_NAME, "null", values);
							values.put(ContractExercise.COLUMN_NAME_NAME, "Calf Raises");
							values.put(ContractExercise.COLUMN_NAME_REPRANGE, "12 to 15");
							values.put(ContractExercise.COLUMN_NAME_REST, "120");
							values.put(ContractExercise.COLUMN_NAME_ORDER, orderExercise++);
							db.insert(ContractExercise.TABLE_NAME, "null", values);
							values.put(ContractExercise.COLUMN_NAME_ORDER, orderExercise++);
							db.insert(ContractExercise.TABLE_NAME, "null", values);
							values.put(ContractExercise.COLUMN_NAME_NAME, "Kneeling Cable Crunches");
							values.put(ContractExercise.COLUMN_NAME_ORDER, orderExercise++);
							db.insert(ContractExercise.TABLE_NAME, "null", values);
							values.put(ContractExercise.COLUMN_NAME_ORDER, orderExercise++);
							db.insert(ContractExercise.TABLE_NAME, "null", values);
							values.put(ContractExercise.COLUMN_NAME_NAME, "Overhead Press");
							values.put(ContractExercise.COLUMN_NAME_REPRANGE, repRangeText);
							values.put(ContractExercise.COLUMN_NAME_ORDER, orderExercise++);
							db.insert(ContractExercise.TABLE_NAME, "null", values);
							values.put(ContractExercise.COLUMN_NAME_ORDER, orderExercise++);
							db.insert(ContractExercise.TABLE_NAME, "null", values);
							values.put(ContractExercise.COLUMN_NAME_NAME, "Seated Cable Rows");
							values.put(ContractExercise.COLUMN_NAME_REPRANGE, "8 to 12");
							values.put(ContractExercise.COLUMN_NAME_ORDER, orderExercise++);
							db.insert(ContractExercise.TABLE_NAME, "null", values);
							values.put(ContractExercise.COLUMN_NAME_REST, "240");
							values.put(ContractExercise.COLUMN_NAME_ORDER, orderExercise++);
							db.insert(ContractExercise.TABLE_NAME, "null", values);
							values.put(ContractExercise.COLUMN_NAME_NAME, "Cable Preacher Curl");
							values.put(ContractExercise.COLUMN_NAME_REPRANGE, repRangeText);
							values.put(ContractExercise.COLUMN_NAME_REST, "120");
							values.put(ContractExercise.COLUMN_NAME_ORDER, orderExercise++);
							db.insert(ContractExercise.TABLE_NAME, "null", values);
							values.put(ContractExercise.COLUMN_NAME_REST, "180");
							values.put(ContractExercise.COLUMN_NAME_ORDER, orderExercise++);
							db.insert(ContractExercise.TABLE_NAME, "null", values);
							values.put(ContractExercise.COLUMN_NAME_NAME, "Tricep Cable Push Downs");
							values.put(ContractExercise.COLUMN_NAME_REPRANGE, "8 to 12");
							values.put(ContractExercise.COLUMN_NAME_REST, "120");
							values.put(ContractExercise.COLUMN_NAME_ORDER, orderExercise++);
							db.insert(ContractExercise.TABLE_NAME, "null", values);
							values.put(ContractExercise.COLUMN_NAME_REST, "180");
							values.put(ContractExercise.COLUMN_NAME_ORDER, orderExercise++);
							db.insert(ContractExercise.TABLE_NAME, "null", values);
							values.put(ContractExercise.COLUMN_NAME_NAME, "Shrug");
							values.put(ContractExercise.COLUMN_NAME_REPRANGE, "12 to 15");
							values.put(ContractExercise.COLUMN_NAME_REST, "120");
							values.put(ContractExercise.COLUMN_NAME_ORDER, orderExercise++);
							db.insert(ContractExercise.TABLE_NAME, "null", values);
							values.put(ContractExercise.COLUMN_NAME_ORDER, orderExercise++);
							db.insert(ContractExercise.TABLE_NAME, "null", values);
							values.put(ContractExercise.COLUMN_NAME_NAME, "DB Wrist Curl");
							values.put(ContractExercise.COLUMN_NAME_REPRANGE, "8 to 12");
							values.put(ContractExercise.COLUMN_NAME_ORDER, orderExercise++);
							db.insert(ContractExercise.TABLE_NAME, "null", values);
					}
					
					values = new ContentValues();
					values.put(ContractWorkoutDay.COLUMN_NAME_NAME, "Friday - Consolidation Workout");
					values.put(ContractWorkoutDay.COLUMN_NAME_COMPLETED, false);
					values.put(ContractWorkoutDay.COLUMN_NAME_DAY_OF_WEEK, 4);
					values.put(ContractWorkoutDay.COLUMN_NAME_EXTERN_ID, newRowWeekId);
					long newRowDayId = db.insert(ContractWorkoutDay.TABLE_NAME, "null", values);
						orderExercise = 0;
						values = new ContentValues();
						values.put(ContractExercise.COLUMN_NAME_EXTERN_ID, newRowDayId);
						values.put(ContractExercise.COLUMN_NAME_COMPLETED, false);
						values.put(ContractExercise.COLUMN_NAME_NREP, 0);
						values.put(ContractExercise.COLUMN_NAME_WEIGHT, 0.f);
						values.put(ContractExercise.COLUMN_NAME_REPRANGE, "4 to 6");
						values.put(ContractExercise.COLUMN_NAME_REST, "180");
						values.put(ContractExercise.COLUMN_NAME_NAME, "Deadlifts");
						values.put(ContractExercise.COLUMN_NAME_ORDER, orderExercise++);
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						values.put(ContractExercise.COLUMN_NAME_ORDER, orderExercise++);
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						values.put(ContractExercise.COLUMN_NAME_NAME, "Deep Dips");
						values.put(ContractExercise.COLUMN_NAME_REPRANGE, repRangeText);
						values.put(ContractExercise.COLUMN_NAME_ORDER, orderExercise++);
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						values.put(ContractExercise.COLUMN_NAME_ORDER, orderExercise++);
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						values.put(ContractExercise.COLUMN_NAME_NAME, "Overhead Dumbbell Press");
						values.put(ContractExercise.COLUMN_NAME_ORDER, orderExercise++);
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						values.put(ContractExercise.COLUMN_NAME_ORDER, orderExercise++);
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						values.put(ContractExercise.COLUMN_NAME_NAME, "Cable Bent-Over Triceps Extensions");
						values.put(ContractExercise.COLUMN_NAME_REPRANGE, "8 to 12");
						values.put(ContractExercise.COLUMN_NAME_ORDER, orderExercise++);
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						values.put(ContractExercise.COLUMN_NAME_ORDER, orderExercise++);
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						values.put(ContractExercise.COLUMN_NAME_NAME, "Seated Cable Rows");
						values.put(ContractExercise.COLUMN_NAME_ORDER, orderExercise++);
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						values.put(ContractExercise.COLUMN_NAME_NAME, "Barbell Curls");
						values.put(ContractExercise.COLUMN_NAME_REPRANGE, repRangeText);
						values.put(ContractExercise.COLUMN_NAME_ORDER, orderExercise++);
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						values.put(ContractExercise.COLUMN_NAME_ORDER, orderExercise++);
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						values.put(ContractExercise.COLUMN_NAME_NAME, "Palms-Up Pull Downs");
						values.put(ContractExercise.COLUMN_NAME_ORDER, orderExercise++);
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						values.put(ContractExercise.COLUMN_NAME_NAME, "Leg Presses");
						values.put(ContractExercise.COLUMN_NAME_ORDER, orderExercise++);
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						values.put(ContractExercise.COLUMN_NAME_ORDER, orderExercise++);
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						values.put(ContractExercise.COLUMN_NAME_NAME, "Leg Extensions");
						values.put(ContractExercise.COLUMN_NAME_ORDER, orderExercise++);
						db.insert(ContractExercise.TABLE_NAME, "null", values);
						values.put(ContractExercise.COLUMN_NAME_NAME, "Leg Curls");
						values.put(ContractExercise.COLUMN_NAME_ORDER, orderExercise++);
						db.insert(ContractExercise.TABLE_NAME, "null", values);
			}
		}
		Log.d("ProgramDbHelper","public void loadDefaultWorkout(SQLiteDatabase db) end");
	}

	public List<String> getAvailableProgramNames(){
		Log.d("ProgramDbHelper","public List<String> getAvailableProgramNames() called");
		List<String> programs = new ArrayList<String>();
		String[] projection = {
				ContractProgram.COLUMN_NAME_NAME
				};
		SQLiteDatabase db = this.getReadableDatabase();
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
		//db.close();
		Log.d("ProgramDbHelper","public List<String> getAvailableProgramNames() end");
		return programs;
	}

	protected int getIdProgram(String programName){
		Log.d("ProgramDbHelper", "protected int getIdProgram(String programName) called");
		String[] projectionProgram = {
				ContractProgram.COLUMN_NAME_ID,
				ContractProgram.COLUMN_NAME_NAME
				};
		SQLiteDatabase db = this.getReadableDatabase();
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
		//db.close();
		Log.d("ProgramDbHelper", "protected int getIdProgram(String programName) end");
		return idProgram;
	}

	public boolean isProgramCompleted(String programName){
		Log.d("ProgramDbHelper", "public boolean isProgramCompleted(...) called");
		String[] projectionProgram = {
							ContractProgram.COLUMN_NAME_NAME,
							ContractProgram.COLUMN_NAME_COMPLETED
							};
		String[] selectionsArgs = {""+programName};
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursorProgram = db.query(ContractProgram.TABLE_NAME,
							projectionProgram,                               // The columns to return
							ContractProgram.COLUMN_NAME_NAME + "=?",
							selectionsArgs,
							null,
							null,
							null);
		boolean completed = false;
		if(cursorProgram.moveToNext()){
			completed = cursorProgram.getInt(1) > 0;
		}
		cursorProgram.close();
		//db.close();
		Log.d("ProgramDbHelper", "public boolean isProgramCompleted(...) end");
		return completed;
	}
	

	public List<String> getAvailableWeeks(String programName){
		Log.d("ProgramDbHelper", "public List<String> getAvailableWeeks(...) called");
		int idProgram = this.getIdProgram(programName);
		String[] projectionWeek = {
				ContractWorkoutWeek.COLUMN_NAME_NAME,
				ContractWorkoutWeek.COLUMN_NAME_EXTERN_ID
				};
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursorWeek = db.query(ContractWorkoutWeek.TABLE_NAME,
										projectionWeek,                               // The columns to return
										null, null, null, null, null);

		List<String> weeks = new ArrayList<String>();
		while(cursorWeek.moveToNext()){
			int idExtProgram = cursorWeek.getInt(1);
			if(idExtProgram == idProgram){
				String weekName = cursorWeek.getString(0);
				weeks.add(weekName);
			}
		}
		cursorWeek.close();
		//db.close();
		Log.d("ProgramDbHelper", "public List<String> getAvailableWeeks(...) end");
		return weeks;
	}
	
	public String getFirstWeek(String programName){
		Log.d("ProgramDbHelper", "public String getFirstWeek(...) called");
		int idProgram = this.getIdProgram(programName);
		String[] projectionWeek = {
				ContractWorkoutWeek.COLUMN_NAME_NAME,
				ContractWorkoutWeek.COLUMN_NAME_EXTERN_ID
				};
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursorWeek = db.query(ContractWorkoutWeek.TABLE_NAME,
										projectionWeek,                               // The columns to return
										null, null, null, null, null);

		List<String> weeks = new ArrayList<String>();
		String firstWeek = "";
		if(cursorWeek.moveToNext()){
			firstWeek = cursorWeek.getString(0);
		}
		cursorWeek.close();
		//db.close();
		Log.d("ProgramDbHelper", "public String getFirstWeek(...) end");
		return firstWeek;
	}
	
	public List<Day> getAvailableDays(String programName,
										String week){
		Log.d("ProgramDbHelper", "public List<String> getAvailableDays(...1)  called");
		int idWeek = this.getIdWeek(programName, week);
		List<Day> days = this.getAvailableDays(idWeek);
		Log.d("ProgramDbHelper", "public List<String> getAvailableDays(...1) end");
		return days;
	}
	
	public List<Day> getAvailableDays(int idWeek){
		Log.d("ProgramDbHelper", "public List<String> getAvailableDays(...2) called");
		String[] projectionDay = {
				ContractWorkoutDay.COLUMN_NAME_NAME,
				ContractWorkoutDay.COLUMN_NAME_DAY_OF_WEEK,
				ContractWorkoutDay.COLUMN_NAME_EXTERN_ID
				};
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursorDay = db.query(ContractWorkoutDay.TABLE_NAME,
				projectionDay,                               // The columns to return
				null,
				null,
				null,
				null,
				ContractWorkoutDay.COLUMN_NAME_DAY_OF_WEEK);
		
		List<Day> days = new ArrayList<Day>();
		while(cursorDay.moveToNext()){
			int idExtWeek = cursorDay.getInt(2);
			if(idExtWeek == idWeek){
				String workoutName = cursorDay.getString(0);
				int dayOfTheWeek = cursorDay.getInt(1);
				Day day = new Day(workoutName, dayOfTheWeek);
				days.add(day);
			}
		}
		cursorDay.close();
		//db.close();
		Log.d("ProgramDbHelper", "public List<String> getAvailableDays(...2) end");
		return days;
	}
	
	public List<Day> getAvailableDaysSorted(int idWeek){
		Log.d("ProgramDbHelper", "public List<String> getAvailableDaysSorted(...) called");
		List<Day> days = this.getAvailableDays(idWeek);
		int nDays = days.size();
		for(int i=0; i<nDays; i++){
			int min = 0;
			Day minDay = days.get(min);
			for(int j=i+1; j<nDays; j++){
				Day currentDay = days.get(j);
				if(minDay.getDayOfTheWeek()
						> currentDay.getDayOfTheWeek()){
					minDay = currentDay;
					min = j;
				}
			}
			Day tmp = days.get(i);
			days.set(i, minDay);
			days.set(min, tmp);
		}
		Log.d("ProgramDbHelper", "public List<String> getAvailableDaysSorted(...) end");
		return days;
	}
	
	protected int getIdWeek(String programName,
							String week){
		Log.d("ProgramDbHelper", "protected int getIdWeek(...1) called");
		
		int idProgram = this.getIdProgram(programName);
		String[] projectionWeek = {
				ContractWorkoutWeek.COLUMN_NAME_ID,
				ContractWorkoutWeek.COLUMN_NAME_NAME,
				ContractWorkoutWeek.COLUMN_NAME_EXTERN_ID
				};
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursorWeek = db.query(ContractWorkoutWeek.TABLE_NAME,
									projectionWeek,                               // The columns to return
									null, null, null, null, null);
		int idWeek = 0;
		while(cursorWeek.moveToNext()){
			int idExtProgram = cursorWeek.getInt(2);
			if(idExtProgram == idProgram){
				String currentWeek = cursorWeek.getString(1);
				if(currentWeek.equals(week)){
					idWeek = cursorWeek.getInt(0);
				}
			}
		}
		cursorWeek.close();
		//db.close();
		Log.d("ProgramDbHelper", "protected int getIdWeek(...1) end");
		return idWeek;
	}
	protected List<Integer> getIdWeeks(String programName){
		Log.d("ProgramDbHelper", "protected int getIdWeek(...) called");
		SQLiteDatabase db = this.getReadableDatabase();
		int idProgram = this.getIdProgram(programName);
		String[] projectionWeek = {
		ContractWorkoutWeek.COLUMN_NAME_ID,
		ContractWorkoutWeek.COLUMN_NAME_EXTERN_ID
		};
		Cursor cursorWeek = db.query(ContractWorkoutWeek.TABLE_NAME,
							projectionWeek,                               // The columns to return
							null, null, null, null, null);
		List<Integer> idWeeks = new ArrayList<Integer>();
		while(cursorWeek.moveToNext()){
			int idExtProgram = cursorWeek.getInt(1);
			if(idExtProgram == idProgram){
				int idWeek = cursorWeek.getInt(0);
				idWeeks.add(idWeek);
			}
		}
		cursorWeek.close();
		//db.close();
		Log.d("ProgramDbHelper", "protected int getIdWeek(...) end");
		return idWeeks;
	}
	
	public boolean isWeekCompleted(String programName,
									String week){
		Log.d("ProgramDbHelper", "public boolean isWeekCompleted(...) called");
		int idWeek = this.getIdWeek(programName,
									week);
		String[] projectionWeek = {
				ContractWorkoutWeek.COLUMN_NAME_ID,
				ContractWorkoutWeek.COLUMN_NAME_COMPLETED
				};
		String[] selectionsArgs = {""+idWeek};
		SQLiteDatabase db = this.getReadableDatabase();
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
		//db.close();
		Log.d("ProgramDbHelper", "public boolean isWeekCompleted(...) end");
		return completed;
	}
	

	
	public List<Exercice> getAvailableExercices(String programName,
												String week,
												String day){
		Log.d("ProgramDbHelper", "public List<Exercice> getAvailableExercices(...) called");
		String rawQuery = "SELECT * FROM "
				+ "(SELECT id_day FROM "
				+ "(SELECT id_week FROM program P"
				+ " INNER JOIN week W"
				+ " ON P.name = '" + programName + "'"
				+ " AND P.id_program = W.id_program"
				+ " AND W.name = '" + week + "'"
				+ ") AS w1 INNER JOIN day"
				+ " ON w1.id_week = day.id_week"
				+ " AND day.name = '" + day + "'"
				+ ") AS d1 INNER JOIN exercice E "
				+ " ON E.id_day = d1.id_day"
				+ " ORDER BY E.order_exercise";
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursorExercise = db.rawQuery(rawQuery, null);
		List<Exercice> exercises = new ArrayList<Exercice>();
		while(cursorExercise.moveToNext()){
			int exerciceId = cursorExercise.getInt(1);
			String exerciseName = cursorExercise.getString(3);
			int nRep = cursorExercise.getInt(5);
			float weight = cursorExercise.getFloat(6);
			String repRange = cursorExercise.getString(7);
			int rest = cursorExercise.getInt(8);
			Exercice exercice = new Exercice(exerciceId, exerciseName, nRep, weight, repRange, rest);
			exercises.add(exercice);
		}
		cursorExercise.close();
		//db.close();
		Log.d("ProgramDbHelper", "public List<Exercice> getAvailableExercices(...) end");
		return exercises;
	}

	public List<Exercice> getAvailableExercices(String programName,
												String week,
												int idDay){
		Log.d("ProgramDbHelper", "public List<Exercice> getAvailableExercices id(...) called");
		List<Exercice> exercises = null;
		if(idDay != -1){
			String rawQuery = "SELECT * FROM "
					+ "exercice E "
					+ "WHERE E.id_day = " + idDay
					+ " ORDER BY E.order_exercise";
			SQLiteDatabase db = this.getReadableDatabase();
			Cursor cursorExercise = db.rawQuery(rawQuery, null);
			exercises = new ArrayList<Exercice>();
			while(cursorExercise.moveToNext()){
				int exerciceId = cursorExercise.getInt(0);
				String exerciseName = cursorExercise.getString(1);
				int nRep = cursorExercise.getInt(4);
				float weight = cursorExercise.getFloat(5);
				String repRange = cursorExercise.getString(6);
				int rest = cursorExercise.getInt(7);
				Exercice exercice = new Exercice(exerciceId, exerciseName, nRep, weight, repRange, rest);
				exercises.add(exercice);
			}
			cursorExercise.close();
			//db.close();
		}
		Log.d("ProgramDbHelper", "public List<Exercice> getAvailableExercices id(...) end");
		return exercises;
	}

	protected int getIdDay(String programName,
							String week,
							String day){
		Log.d("ProgramDbHelper", "protected int getIdDay(...) called");
		int idWeek = this.getIdWeek(programName, week);
		String[] projectionDay = {
				ContractWorkoutDay.COLUMN_NAME_ID,
				ContractWorkoutDay.COLUMN_NAME_NAME,
				ContractWorkoutDay.COLUMN_NAME_EXTERN_ID
				};
		SQLiteDatabase db = this.getReadableDatabase();
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
		//db.close();
		Log.d("ProgramDbHelper", "protected int getIdDay(...) end");
		return idDay;
	}
	
	public List<Exercice> getPreviousExercices(String programName,
												String week,
												Day day){
		Log.d("ProgramDbHelper", "public List<Exercice> getPreviousExercices(...) called");
		int idPreviousDay = this.getIdPreviousDay(programName, week, day);
		List<Exercice> exercises = null;
		if(idPreviousDay != -1){
			exercises = this.getAvailableExercices(programName,
					week,
					idPreviousDay);
		}
		Log.d("ProgramDbHelper", "public List<Exercice> getPreviousExercices(...) end");
		return exercises;
	}
	
	protected int getIdPreviousDay(String programName,
									String week,
									Day day){
		Log.d("ProgramDbHelper", "protected int getIdPreviousDay(...) called");
		
		int idWeek = this.getIdWeek(programName, week);
		//Look for a day this week with the same exercises
		List<Day> previousDays 
		= this.getAvailableDaysSorted(idWeek);
		Collections.reverse(previousDays);
		Log.d("ProgramDbHelper", "previous days got");
		List<Exercice> currentExercises = this.getAvailableExercices(programName,
				week,
				day.getWorkoutName());
		Log.d("ProgramDbHelper", "current exercises got");
		Day previousDay = null;
		int idPreviousDay = -1;
		for(Day otherDay:previousDays){
			if(otherDay.getDayOfTheWeek() <  day.getDayOfTheWeek()){
				List<Exercice> exercises = this.getAvailableExercices(programName,
						week,
						otherDay.getWorkoutName());
				if(exercisesListEquals(exercises, currentExercises)){
					previousDay = otherDay;
					idPreviousDay = this.getIdDay(
							programName,
							week,
							previousDay.getWorkoutName());
					break;
				}
			}
		}
		Log.d("ProgramDbHelper", "other previous day searched");
		if(previousDay == null){
			//Loof kor the previous week if any
			List<Integer> idWeeks = this.getIdWeeks(programName);
			List<String> weekNames = this.getAvailableWeeks(programName);
			int idPreviousWeek = -1;
			String previousWeekName = null;
			if(idWeeks.get(0) != idWeek){
				for(int i=0; i<idWeeks.size()-1; i++){
					int nextId = idWeeks.get(i+1);
					if(nextId == idWeek){
						idPreviousWeek = idWeeks.get(i);
						previousWeekName = weekNames.get(i);
						break;
					}
				}
			}
			Log.d("ProgramDbHelper", "idPreviousWeek:" + idPreviousWeek);
			if(idPreviousWeek != -1){
				previousDays 
				= this.getAvailableDaysSorted(idPreviousWeek);
				Collections.reverse(previousDays);
				for(Day otherDay:previousDays){
					List<Exercice> exercises = this.getAvailableExercices(programName,
							week,
							otherDay.getWorkoutName());
					if(exercisesListEquals(exercises, currentExercises)){
						previousDay = otherDay;
						idPreviousDay = this.getIdDay(
								programName,
								previousWeekName,
								previousDay.getWorkoutName());
						break;
					}
				}
			}
			
		}
		Log.d("ProgramDbHelper", "other previous day searched in previous wwek");
		return idPreviousDay;
		/*
		
		
		
		int idPreviousDay = -1;
		if(idPreviousWeek != -1){
			
		}
		
		
		
		String[] projectionDay = {
				ContractWorkoutDay.COLUMN_NAME_ID,
				ContractWorkoutDay.COLUMN_NAME_NAME,
				ContractWorkoutDay.COLUMN_NAME_EXTERN_ID
				};
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursorDay = db.query(ContractWorkoutDay.TABLE_NAME, projectionDay,                               // The columns to return
								null, null, null, null,
								ContractWorkoutDay.COLUMN_NAME_DAY_OF_WEEK);
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
		//db.close();
		int idPreviousDay = -1;
		for(Integer previousDay:previousDays){
			List<Exercice> exercises = this.getAvailableExercices(programName,
					week,
					previousDay);
			if(exercisesListEquals(exercises, currentExercises)){
				idPreviousDay = previousDay;
				break;
			}
		}
		Log.d("ProgramDbHelper", "protected int getIdPreviousDay(...) ends");
		return idPreviousDay;
		//*/
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
									String week,
									String day){
		Log.d("ProgramDbHelper", "public boolean isDayCompleted(...) called");
		int idDay = this.getIdDay(programName,
									week,
									day);
		String[] projectionDay = {
				ContractWorkoutDay.COLUMN_NAME_ID,
				ContractWorkoutDay.COLUMN_NAME_COMPLETED
				};
		String[] selectionsArgs = {""+idDay};
		Log.d("ProgramDbHelper", "query...");
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursorDay = db.query(ContractWorkoutDay.TABLE_NAME,
									projectionDay,                               // The columns to return
									ContractWorkoutDay.COLUMN_NAME_ID + "=?",
									selectionsArgs,
									null,
									null,null);
		Log.d("ProgramDbHelper", "query done");
		boolean completed = false;
		if(cursorDay.moveToNext()){
			completed = cursorDay.getInt(1) > 0;
		}
		Log.d("ProgramDbHelper", "closing...");
		cursorDay.close();
		//db.close();
		Log.d("ProgramDbHelper", "public boolean isDayCompleted(...) end");
		return completed;
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
		while(cursorExercise.moveToNext()){
			int currentExerciseId = cursorExercise.getInt(0);
			if(currentExerciseId == exerciseId){
				boolean completed = cursorExercise.getInt(1) > 0;
				Log.d("ProgramDbHelper", "public boolean isExerciseCompleted(int exerciseId) end completed");
				return completed;
			}
		}
		cursorExercise.close();
		//db.close();
		Log.d("ProgramDbHelper", "public boolean isExerciseCompleted(int exerciseId) end false");
		return false;
	}
	
	protected void setExerciceInfo(String programName,
									String week,
									String day,
									String exerciseId,
									String rest,
									String weight,
									String nReps){
		Log.d("ProgramDbHelper", "protected void setExerciceInfo(...) called");
		int exerciseIdInt = Integer.parseInt(exerciseId);
		//boolean oldCompleted = this.isExerciseCompleted(exerciseIdInt);
		boolean completed = false;
		if(!weight.equals("") && !nReps.equals("")){
			float weightFloat = Float.parseFloat(weight);
			int nRepsInt = Integer.parseInt(nReps);
			completed = weightFloat > 0.f && nRepsInt > 0;
		}
		//if(oldCompleted != completed || completed){
		ContentValues values = new ContentValues();
		values.put(ContractExercise.COLUMN_NAME_REST, rest);
		values.put(ContractExercise.COLUMN_NAME_WEIGHT, weight);
		values.put(ContractExercise.COLUMN_NAME_NREP, nReps);

		values.put(ContractExercise.COLUMN_NAME_COMPLETED, completed);
		SQLiteDatabase db = this.getWritableDatabase();
		int nRows = db.update(ContractExercise.TABLE_NAME,
					values, ContractExercise.COLUMN_NAME_ID + "=" + exerciseId,
					null);
		//db.close();
		this.updateDayCompletedEventually(programName,
								week,
								day);
		//}
		Log.d("ProgramDbHelper", "protected void setExerciceInfo(...) end");
	}
	
	protected void updateDayCompletedEventually(String programName,
										String week,
										String day){
		Log.d("ProgramDbHelper", "protected void updateDayCompletedEventually(...) called");
		boolean oldCompleted = this.isDayCompleted(programName,
													week,
													day);
		boolean completed = true;
		List<Exercice> exercises = getAvailableExercices(programName,
														week,
														day);
		Log.d("ProgramDbHelper", "exercises got");
		for(Exercice exercise:exercises){
			if(!exercise.isDone()){
				completed = false;
				break;
			}
		}
		if(oldCompleted != completed){
			ContentValues values = new ContentValues();
			values.put(ContractWorkoutDay.COLUMN_NAME_COMPLETED, completed);
			Log.d("ProgramDbHelper", "getting writtable database...");
			SQLiteDatabase db = this.getWritableDatabase();
			Log.d("ProgramDbHelper", "writtable database got");
			int idDay = this.getIdDay(programName,
										week,
										day);
			db.update(ContractWorkoutDay.TABLE_NAME,
						values, ContractWorkoutDay.COLUMN_NAME_ID + "=" + idDay,
						null);
			Log.d("ProgramDbHelper", "update done");
			//db.close();
			this.updateWeekCompletedEventually(programName,
												week);
			Log.d("ProgramDbHelper", "protected void updateDayCompletedEventually(...) end");
		}
	}
	//-------------------------------------------------------------
	protected void updateWeekCompletedEventually(String programName,
												String week){
		Log.d("ProgramDbHelper", "protected void updateWeekCompletedEventually(...) called");
		boolean oldCompleted = this.isWeekCompleted(programName,
													week);
		boolean completed = true;
		List<Day> days = getAvailableDays(programName,
												week);
		for(Day day:days){
			String workoutName = day.getWorkoutName();
			boolean dayCompleted = this.isDayCompleted(programName, week, workoutName);
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
										week);
			db.update(ContractWorkoutWeek.TABLE_NAME,
						values, ContractWorkoutWeek.COLUMN_NAME_ID + "=" + idWeek,
						null);
			//db.close();
			this.updateProgramCompletedEventually(programName);
		}
		Log.d("ProgramDbHelper", "protected void updateWeekCompletedEventually(...) end");
	}
	//-------------------------------------------------------------
	protected void updateProgramCompletedEventually(String programName){
		Log.d("ProgramDbHelper", "protected void updateProgramCompletedEventually(...) called");
		boolean oldCompleted = this.isProgramCompleted(programName);
		boolean completed = true;
		List<String> weeks = getAvailableWeeks(programName);
		for(String week:weeks){
			boolean weekCompleted = this.isWeekCompleted(programName,
														week);
			if(!weekCompleted){
				completed = false;
				break;
			}
		}
		if(oldCompleted != completed){
			ContentValues values = new ContentValues();
			values.put(ContractProgram.COLUMN_NAME_COMPLETED, completed);
			SQLiteDatabase db = this.getWritableDatabase();
			int idProgram = this.getIdProgram(programName);
			db.update(ContractProgram.TABLE_NAME,
						values, ContractProgram.COLUMN_NAME_ID + "=" + idProgram,
						null);
			//db.close();
		}
		Log.d("ProgramDbHelper", "protected void updateProgramCompletedEventually(...) end");
	}
	//-------------------------------------------------------------
	public boolean isWorkoutNameAvailable(String name){
		List<String> programNames = this.getAvailableProgramNames();
		boolean available = !programNames.contains(name);
		return available;
	}
	//-------------------------------------------------------------
	long createProgram(String name){
		Log.d("ProgramDbHelper", "public long createProgram(...) called");
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(ContractProgram.COLUMN_NAME_NAME, name);
		values.put(ContractProgram.COLUMN_NAME_COMPLETED, false);
		long newRowProgramId = db.insert(ContractProgram.TABLE_NAME, "null", values);
		Log.d("ProgramDbHelper", "public long createProgram(...) called");
		return newRowProgramId;
	}
	//-------------------------------------------------------------
	long createProgram(String name, int nWeeks){
		Log.d("ProgramDbHelper", "public long createProgram(...) called");
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(ContractProgram.COLUMN_NAME_NAME, name);
		values.put(ContractProgram.COLUMN_NAME_COMPLETED, false);
		long newRowProgramId = db.insert(ContractProgram.TABLE_NAME, "null", values);
		for(int i=0; i<nWeeks; i++){
			values = new ContentValues();
			values.put(ContractWorkoutWeek.COLUMN_NAME_NAME, "Week " + (i+1));
			values.put(ContractWorkoutWeek.COLUMN_NAME_EXTERN_ID, newRowProgramId);
			values.put(ContractWorkoutWeek.COLUMN_NAME_COMPLETED, false);
			db.insert(ContractWorkoutWeek.TABLE_NAME, "null", values);
		}
		Log.d("ProgramDbHelper", "public long createProgram(...) called");
		return newRowProgramId;
	}
	//-------------------------------------------------------------
	public void createProgramFromExistingOne(
			String name,
			int nWeeks,
			String existingProgramName){
		Log.d("ProgramDbHelper", "public void createProgramFromExistingOne(...) called");
		long newRowProgramId = this.createProgram(name);
		String rawQuery = "SELECT * FROM "
				+ "(SELECT day.id_day, day.name, day.day_of_week FROM "
				+ "(SELECT id_week FROM program P"
				+ " INNER JOIN week W"
				+ " ON P.name = '" + existingProgramName + "'"
				+ " AND P.id_program = W.id_program"
				+ " LIMIT 1)"
				+ " AS w1 INNER JOIN day"
				+ " ON w1.id_week = day.id_week"
				+ ") AS d1 INNER JOIN exercice E "
				+ " ON E.id_day = d1.id_day";
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursorExercises = db.rawQuery(rawQuery, null);

		HashMap<Integer, List<Exercice>> exercises
		= new HashMap<Integer, List<Exercice>>();
		HashMap<Integer, String> workoutNames
		= new HashMap<Integer, String>();
		List<Exercice> exercisesOfDay = null;
		while(cursorExercises.moveToNext()){
			int day_of_week = cursorExercises.getInt(2);
			if(!exercises.containsKey(day_of_week)){
				String dayName = cursorExercises.getString(1);
				exercisesOfDay = new ArrayList<Exercice>();
				exercises.put(day_of_week, exercisesOfDay);
				workoutNames.put(day_of_week, dayName);
			}else{
				exercisesOfDay = exercises.get(day_of_week);
			}
			String exerciseName = cursorExercises.getString(5);
			//int nRep = cursorExercises.getInt(7);
			//float weight = cursorExercises.getFloat(8);
			String repRange = cursorExercises.getString(9);
			int rest = cursorExercises.getInt(10);
			Exercice exercice = new Exercice(-1, exerciseName, 0, 0, repRange, rest);
			exercisesOfDay.add(exercice);
		}
		for(int i=0; i<nWeeks; i++){
			ContentValues values = new ContentValues();
			values.put(ContractWorkoutWeek.COLUMN_NAME_NAME, "Week " + (i+1));
			values.put(ContractWorkoutWeek.COLUMN_NAME_EXTERN_ID, newRowProgramId);
			values.put(ContractWorkoutWeek.COLUMN_NAME_COMPLETED, false);
			long newRowWeekId = db.insert(ContractWorkoutWeek.TABLE_NAME, "null", values);
			for(Entry<Integer, List<Exercice>> entry : exercises.entrySet()) {
				int dayOfTheWeek = entry.getKey();
				String workoutName = workoutNames.get(dayOfTheWeek);
				List<Exercice> valueExercises = entry.getValue();
				values = new ContentValues();
				values.put(ContractWorkoutDay.COLUMN_NAME_NAME, workoutName);
				values.put(ContractWorkoutDay.COLUMN_NAME_COMPLETED, false);
				values.put(ContractWorkoutDay.COLUMN_NAME_DAY_OF_WEEK, dayOfTheWeek); //TODO
				values.put(ContractWorkoutDay.COLUMN_NAME_EXTERN_ID, newRowWeekId);
				long newRowDayId = db.insert(ContractWorkoutDay.TABLE_NAME, "null", values);
				int order=0;
				for(Exercice exercise : valueExercises){
					values = new ContentValues();
					values.put(ContractExercise.COLUMN_NAME_EXTERN_ID, newRowDayId);
					values.put(ContractExercise.COLUMN_NAME_ORDER, order++);
					values.put(ContractExercise.COLUMN_NAME_COMPLETED, false);
					values.put(ContractExercise.COLUMN_NAME_NREP, 0);
					values.put(ContractExercise.COLUMN_NAME_WEIGHT, 0.f);
					values.put(ContractExercise.COLUMN_NAME_REPRANGE, exercise.repRange);
					values.put(ContractExercise.COLUMN_NAME_REST, exercise.rest);
					values.put(ContractExercise.COLUMN_NAME_NAME, exercise.name);
					db.insert(ContractExercise.TABLE_NAME, "null", values);
				}
			}
		}
		cursorExercises.close();
		Log.d("ProgramDbHelper", "public void createProgramFromExistingOne(...) end");
		
	}
	//-------------------------------------------------------------
	public void deleteProgram(String programName){
		Log.d("ProgramDbHelper", "public void deleteProgram(...) called");
		SQLiteDatabase db = this.getWritableDatabase();
		//db.execSQL("PRAGMA foreign_keys=ON");
		//db.setForeignKeyConstraintsEnabled(true);
		String deleteQuery
		= "DELETE FROM " + ContractProgram.TABLE_NAME
		+ " WHERE " + ContractProgram.COLUMN_NAME_NAME
		+ "='" + programName + "'";
		db.execSQL(deleteQuery);
		Log.d("ProgramDbHelper", "public void deleteProgram(...) end");
	}
	//-------------------------------------------------------------
		public void deleteDay(
				String programName,
				int dayOfTheWeek){
			Log.d("ProgramDbHelper", "public void deleteDay(...) called");
			SQLiteDatabase db = this.getWritableDatabase();
			String deleteQuery
			= "DELETE FROM " + ContractWorkoutDay.TABLE_NAME
			+ " WHERE " + ContractWorkoutDay.COLUMN_NAME_DAY_OF_WEEK
			+ "='" + dayOfTheWeek + "'"
			+ " AND "+ ContractWorkoutDay.COLUMN_NAME_ID
			+ " IN (SELECT id_day FROM "
			+ "(SELECT id_week FROM program P"
			+ " INNER JOIN week W"
			+ " ON P.name = '" + programName + "'"
			+ " AND P.id_program = W.id_program"
			+ ") AS w1 INNER JOIN day"
			+ " ON w1.id_week = day.id_week"
			+ ") ";
			
			db.execSQL(deleteQuery);
			Log.d("ProgramDbHelper", "public void deleteDay(...) end");
		}
		//-------------------------------------------------------------
		public void deleteDay(
				String programName,
				String dayName){
			Log.d("ProgramDbHelper", "public void deleteDay(...) called");
			SQLiteDatabase db = this.getWritableDatabase();
			String deleteQuery
			= "DELETE FROM " + ContractWorkoutDay.TABLE_NAME
			+ " WHERE " + ContractWorkoutDay.COLUMN_NAME_NAME
			+ "='" + dayName + "'"
			+ " AND "+ ContractWorkoutDay.COLUMN_NAME_ID
			+ " IN (SELECT id_day FROM "
			+ "(SELECT id_week FROM program P"
			+ " INNER JOIN week W"
			+ " ON P.name = '" + programName + "'"
			+ " AND P.id_program = W.id_program"
			+ ") AS w1 INNER JOIN day"
			+ " ON w1.id_week = day.id_week"
			+ ") ";
			
			db.execSQL(deleteQuery);
			Log.d("ProgramDbHelper", "public void deleteDay(...) end");
		}
	//-------------------------------------------------------------
	public void createDay(
			String programName,
			String dayName,
			int dayOfTheWeek){
		Log.d("ProgramDbHelper", "public void createDay(...) called");
		SQLiteDatabase db = this.getWritableDatabase();
		List<Integer> idWeeks = this.getIdWeeks(programName);
		for(Integer idWeek:idWeeks){
			ContentValues values = new ContentValues();
			values.put(ContractWorkoutDay.COLUMN_NAME_NAME, dayName);
			values.put(ContractWorkoutDay.COLUMN_NAME_COMPLETED, false);
			values.put(ContractWorkoutDay.COLUMN_NAME_DAY_OF_WEEK, dayOfTheWeek);
			values.put(ContractWorkoutDay.COLUMN_NAME_EXTERN_ID, idWeek);
			long newRowDayId = db.insert(ContractWorkoutDay.TABLE_NAME, "null", values);
		}
		Log.d("ProgramDbHelper", "public void createDay(...) end");
	}
	//-------------------------------------------------------------
	public void createDayFromExistingOne(
			String programName,
			String dayName,
			int dayOfTheWeek,
			String existingOne){
		Log.d("ProgramDbHelper", "public void createDayFromExistingOne(...) called");
		String firstWeekName = this.getFirstWeek(programName);
		List<Exercice> exercises = this.getAvailableExercices(
				programName,
				firstWeekName,
				existingOne);
		this.createDayFromExistingOne(
				programName,
				dayName,
				dayOfTheWeek,
				exercises);
		Log.d("ProgramDbHelper", "public void createDayFromExistingOne(...) end");
	}
	//-------------------------------------------------------------
		public void createDayFromExistingOne(
				String programName,
				String dayName,
				int dayOfTheWeek,
				List<Exercice> exercises){
			Log.d("ProgramDbHelper", "public void createDayFromExistingOne 2(...) called");
			SQLiteDatabase db = this.getWritableDatabase();
			List<Integer> idWeeks = this.getIdWeeks(programName);
			for(Integer idWeek:idWeeks){
				ContentValues values = new ContentValues();
				values.put(ContractWorkoutDay.COLUMN_NAME_NAME, dayName);
				values.put(ContractWorkoutDay.COLUMN_NAME_COMPLETED, false);
				values.put(ContractWorkoutDay.COLUMN_NAME_DAY_OF_WEEK, dayOfTheWeek);
				values.put(ContractWorkoutDay.COLUMN_NAME_EXTERN_ID, idWeek);
				long newRowDayId = db.insert(ContractWorkoutDay.TABLE_NAME, "null", values);
				int orderExercise = 0;
				for(Exercice exercise: exercises){
					values = new ContentValues();
					values.put(ContractExercise.COLUMN_NAME_EXTERN_ID, newRowDayId);
					values.put(ContractExercise.COLUMN_NAME_ORDER, orderExercise++);
					values.put(ContractExercise.COLUMN_NAME_COMPLETED, false);
					values.put(ContractExercise.COLUMN_NAME_NREP, 0);
					values.put(ContractExercise.COLUMN_NAME_WEIGHT, 0.f);
					String repRange = exercise.getRepRange();
					values.put(ContractExercise.COLUMN_NAME_REPRANGE, repRange);
					int rest = exercise.getRest();
					values.put(ContractExercise.COLUMN_NAME_REST, rest);
					String exerciseName = exercise.getName();
					values.put(ContractExercise.COLUMN_NAME_NAME, exerciseName);
					db.insert(ContractExercise.TABLE_NAME, "null", values);
				}
			}
			//db.close();
			Log.d("ProgramDbHelper", "public void createDayFromExistingOne 2(...) end");
		}
	//-------------------------------------------------------------
	public void setExercices(
			String programName,
			Day day,
			List<Exercice> exercises){
		Log.d("ProgramDbHelper", "protected void setExercices(...) called");
		//List<Integer> idWeeks = this.getIdWeeks(programName);
		this.deleteDay(programName, day.dayOfTheWeek);
		this.createDayFromExistingOne(
				programName,
				day.workoutName,
				day.dayOfTheWeek,
				exercises);
		Log.d("ProgramDbHelper", "protected void setExercices(...) end");
	}
}

