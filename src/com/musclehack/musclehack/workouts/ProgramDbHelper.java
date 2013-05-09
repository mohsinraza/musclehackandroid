package com.musclehack.musclehack.workouts;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.musclehack.musclehack.workouts.ContractProgram.ContractExercise;
import com.musclehack.musclehack.workouts.ContractProgram.ContractSubProgram;
import com.musclehack.musclehack.workouts.ContractProgram.ContractWorkoutDay;
import com.musclehack.musclehack.workouts.ContractProgram.ContractWorkoutWeek;

public class ProgramDbHelper extends SQLiteOpenHelper {
	public static final int DATABASE_VERSION = 1;
	public static final String DATABASE_NAME = "programs.db";

	public ProgramDbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//db.execSQL(SQL_DELETE_ENTRIES);
		//onCreate(db);
	}
	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//onUpgrade(db, oldVersion, newVersion);
	}
	
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(ContractProgram.CREATE_TABLE);
		db.execSQL(ContractSubProgram.CREATE_TABLE);
		db.execSQL(ContractWorkoutWeek.CREATE_TABLE);
		db.execSQL(ContractWorkoutDay.CREATE_TABLE);
		this.loadDefaultWorkout(db);
	}
	
	public void loadDefaultWorkout(SQLiteDatabase db){
		ContentValues values = new ContentValues();
		values.put(ContractProgram.COLUMN_NAME_NAME, "THT5 VOLUME CYCLES");
		long newRowProgramId = db.insert(ContractProgram.TABLE_NAME, "null", values);
		values = new ContentValues();
		values.put(ContractSubProgram.COLUMN_NAME_NAME, "THT5 VOLUME 8-12");
		values.put(ContractSubProgram.COLUMN_NAME_EXTERN_ID, newRowProgramId);
		long newRowSubProgramId = db.insert(ContractSubProgram.TABLE_NAME, "null", values);
		for(int i=0; i<10; i++){
			values = new ContentValues();
			values.put(ContractWorkoutWeek.COLUMN_NAME_NAME, "Week " + (i+1));
			values.put(ContractWorkoutWeek.COLUMN_NAME_EXTERN_ID, newRowSubProgramId);
			long newRowWeekId = db.insert(ContractWorkoutWeek.TABLE_NAME, "null", values);
			values = new ContentValues();
			values.put(ContractWorkoutDay.COLUMN_NAME_NAME, "Monday - Shoulders & Traps");
			values.put(ContractWorkoutDay.COLUMN_NAME_DAY_OF_WEEK, 0);
			values.put(ContractWorkoutDay.COLUMN_NAME_EXTERN_ID, newRowWeekId);
			long newRowDayId = db.insert(ContractWorkoutDay.TABLE_NAME, "null", values);
			values = new ContentValues();
			values.put(ContractExercise.COLUMN_NAME_EXTERN_ID, newRowDayId);
			values.put(ContractExercise.COLUMN_NAME_NREP, 0);
			values.put(ContractExercise.COLUMN_NAME_WEIGHT, 0.f);
			values.put(ContractExercise.COLUMN_NAME_REPRANGE, "8 to 12");
			values.put(ContractExercise.COLUMN_NAME_REST, "3");
			values.put(ContractExercise.COLUMN_NAME_NAME, "Overhead Barbell Press");
			db.insert(ContractExercise.TABLE_NAME, "null", values);
			values.put(ContractExercise.COLUMN_NAME_NAME, "Overhead Barbell Press");
			db.insert(ContractExercise.TABLE_NAME, "null", values);
			values.put(ContractExercise.COLUMN_NAME_NAME, "Overhead Barbell Press");
			db.insert(ContractExercise.TABLE_NAME, "null", values);
			values.put(ContractExercise.COLUMN_NAME_NAME, "Overhead Barbell Press");
			db.insert(ContractExercise.TABLE_NAME, "null", values);
			values.put(ContractExercise.COLUMN_NAME_NAME, "Overhead Dumbbell Press");
			values.put(ContractExercise.COLUMN_NAME_REST, "2");
			db.insert(ContractExercise.TABLE_NAME, "null", values);
			values.put(ContractExercise.COLUMN_NAME_NAME, "Overhead Dumbbell Press");
			db.insert(ContractExercise.TABLE_NAME, "null", values);
			values.put(ContractExercise.COLUMN_NAME_NAME, "Dumbbell Lateral Raises");
			db.insert(ContractExercise.TABLE_NAME, "null", values);
			values.put(ContractExercise.COLUMN_NAME_NAME, "Dumbbell Lateral Raises");
			db.insert(ContractExercise.TABLE_NAME, "null", values);
			values.put(ContractExercise.COLUMN_NAME_NAME, "Dumbbell Front Raises");
			db.insert(ContractExercise.TABLE_NAME, "null", values);
			values.put(ContractExercise.COLUMN_NAME_NAME, "Dumbbell Front Raises");
			values.put(ContractExercise.COLUMN_NAME_REST, "4");
			db.insert(ContractExercise.TABLE_NAME, "null", values);
		}
		values = new ContentValues();
		values.put(ContractSubProgram.COLUMN_NAME_NAME, "THT5 VOLUME 7-10");
		values.put(ContractSubProgram.COLUMN_NAME_EXTERN_ID, newRowProgramId);
		newRowSubProgramId = db.insert(ContractSubProgram.TABLE_NAME, "null", values);
		values = new ContentValues();
		values.put(ContractSubProgram.COLUMN_NAME_NAME, "THT5 VOLUME 6-8");
		values.put(ContractSubProgram.COLUMN_NAME_EXTERN_ID, newRowProgramId);
		newRowSubProgramId = db.insert(ContractSubProgram.TABLE_NAME, "null", values);
		
		
		values = new ContentValues();
		values.put(ContractProgram.COLUMN_NAME_NAME, "THT5 HIT CYCLES");
		newRowProgramId = db.insert(ContractProgram.TABLE_NAME, "null", values);
	}

	public List<String> getAvailableProgramNames(){
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
		return programs;
	}

	public List<String> getAvailableSubProgramNames(String programName){
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
		return subPrograms;
	}
	
	protected int getIdProgram(String programName){
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
		return idProgram;
	}

	public List<String> getAvailableWeeks(String programName, String subProgramName){
		SQLiteDatabase db = this.getReadableDatabase();
		int idSubProgram = this.getIdSubProgram(programName, subProgramName);
		String[] projectionWeek = {
				ContractWorkoutWeek.COLUMN_NAME_NAME,
				ContractWorkoutWeek.COLUMN_NAME_EXTERN_ID
				};
		Cursor cursorWeek = db.query(ContractWorkoutWeek.TABLE_NAME, projectionWeek,                               // The columns to return
								null, null, null, null, null);

		List<String> weeks = new ArrayList<String>();
		while(cursorWeek.moveToNext()){
			int idExtProgram = cursorWeek.getInt(1);
			if(idExtProgram == idSubProgram){
				String weekName = cursorWeek.getString(0);
				weeks.add(weekName);
			}
		}
		return weeks;
	}

	protected int getIdSubProgram(String programName, String subProgramName){
		SQLiteDatabase db = this.getReadableDatabase();
		int idProgram = this.getIdProgram(programName);
		String[] projectionSubProgram = {
				ContractSubProgram.COLUMN_NAME_ID,
				ContractSubProgram.COLUMN_NAME_NAME,
				ContractSubProgram.COLUMN_NAME_EXTERN_ID
				};
		Cursor cursorSubProgram = db.query(ContractSubProgram.TABLE_NAME, projectionSubProgram,                               // The columns to return
								null, null, null, null, null);
		int idSubProgram = 0;
		while(cursorSubProgram.moveToNext()){
			int idExtProgram = cursorSubProgram.getInt(2);
			if(idExtProgram == idProgram){
				idSubProgram = cursorSubProgram.getInt(0);
			}
		}
		return idSubProgram;
	}
	
	public List<String> getAvailableDays(String programName, String subProgramName, String week){
		SQLiteDatabase db = this.getReadableDatabase();
		int idWeek = this.getIdWeek(programName, subProgramName, week);
		String[] projectionDay = {
				ContractWorkoutDay.COLUMN_NAME_NAME,
				ContractWorkoutDay.COLUMN_NAME_EXTERN_ID
				};
		Cursor cursorDay = db.query(ContractWorkoutDay.TABLE_NAME, projectionDay,                               // The columns to return
								null, null, null, null, null);

		List<String> days = new ArrayList<String>();
		while(cursorDay.moveToNext()){
			int idExtWeek = cursorDay.getInt(1);
			if(idExtWeek == idWeek){
				String dayName = cursorDay.getString(0);
				days.add(dayName);
			}
		}
		return days;
	}
	
	protected int getIdWeek(String programName, String subProgramName, String week){
		SQLiteDatabase db = this.getReadableDatabase();
		int idSubProgram = this.getIdSubProgram(programName, subProgramName);
		String[] projectionWeek = {
				ContractWorkoutWeek.COLUMN_NAME_ID,
				ContractWorkoutWeek.COLUMN_NAME_NAME,
				ContractWorkoutWeek.COLUMN_NAME_EXTERN_ID
				};
		Cursor cursorWeek = db.query(ContractWorkoutWeek.TABLE_NAME, projectionWeek,                               // The columns to return
								null, null, null, null, null);
		int idWeek = 0;
		while(cursorWeek.moveToNext()){
			int idExtSubProgram = cursorWeek.getInt(2);
			if(idExtSubProgram == idSubProgram){
				idWeek = cursorWeek.getInt(0);
			}
		}
		return idWeek;
	}
	
	public List<Exercice> getAvailableExercices(String programName, String subProgramName, String week,  String day){
		SQLiteDatabase db = this.getReadableDatabase();
		int idDay = this.getIdDay(programName, subProgramName, week, day);
		String[] projectionExercice = {
				ContractExercise.COLUMN_NAME_NAME,
				ContractExercise.COLUMN_NAME_NREP,
				ContractExercise.COLUMN_NAME_WEIGHT,
				ContractExercise.COLUMN_NAME_REPRANGE,
				ContractExercise.COLUMN_NAME_REST,
				ContractExercise.COLUMN_NAME_EXTERN_ID
				};
		Cursor cursorExercice = db.query(ContractExercise.TABLE_NAME, projectionExercice,                               // The columns to return
								null, null, null, null, null);

		List<Exercice> exercices = new ArrayList<Exercice>();
		while(cursorExercice.moveToNext()){
			int idExtDay = cursorExercice.getInt(1);
			if(idExtDay == idDay){
				String exerciseName = cursorExercice.getString(0);
				int nRep = cursorExercice.getInt(1);
				float weight = cursorExercice.getFloat(2);
				String repRange = cursorExercice.getString(3);
				String rest = cursorExercice.getString(4);
				Exercice exercice = new Exercice(exerciseName, nRep, weight, repRange, rest);
				exercices.add(exercice);
			}
		}
		return exercices;
	}

	protected int getIdDay(String programName, String subProgramName, String week, String day){
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
				idDay = cursorDay.getInt(0);
			}
		}
		return idDay;
	}
}

