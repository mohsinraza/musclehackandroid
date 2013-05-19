package com.musclehack.musclehack.workouts;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

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
		db.execSQL(ContractExercise.CREATE_TABLE);
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
					values.put(ContractExercise.COLUMN_NAME_REST, "180");
					values.put(ContractExercise.COLUMN_NAME_NAME, "Overhead Barbell Press");
					for(int j=0; j<4; j++){
						db.insert(ContractExercise.TABLE_NAME, "null", values);
					}
					values.put(ContractExercise.COLUMN_NAME_NAME, "Overhead Dumbbell Press");
					values.put(ContractExercise.COLUMN_NAME_REST, "120");
					db.insert(ContractExercise.TABLE_NAME, "null", values);
					db.insert(ContractExercise.TABLE_NAME, "null", values);
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
				values.put(ContractWorkoutDay.COLUMN_NAME_DAY_OF_WEEK, 1);
				values.put(ContractWorkoutDay.COLUMN_NAME_EXTERN_ID, newRowWeekId);
				newRowDayId = db.insert(ContractWorkoutDay.TABLE_NAME, "null", values);
					values = new ContentValues();
					values.put(ContractExercise.COLUMN_NAME_EXTERN_ID, newRowDayId);
					values.put(ContractExercise.COLUMN_NAME_NREP, 0);
					values.put(ContractExercise.COLUMN_NAME_WEIGHT, 0.f);
					values.put(ContractExercise.COLUMN_NAME_REPRANGE, "8 to 12");
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
				values.put(ContractWorkoutDay.COLUMN_NAME_DAY_OF_WEEK, 3);
				values.put(ContractWorkoutDay.COLUMN_NAME_EXTERN_ID, newRowWeekId);
				newRowDayId = db.insert(ContractWorkoutDay.TABLE_NAME, "null", values);
					values = new ContentValues();
					values.put(ContractExercise.COLUMN_NAME_EXTERN_ID, newRowDayId);
					values.put(ContractExercise.COLUMN_NAME_NREP, 0);
					values.put(ContractExercise.COLUMN_NAME_WEIGHT, 0.f);
					values.put(ContractExercise.COLUMN_NAME_REPRANGE, "8 to 12");
					values.put(ContractExercise.COLUMN_NAME_REST, "120");
					values.put(ContractExercise.COLUMN_NAME_NAME, "Decline Tricep Extensions");
					db.insert(ContractExercise.TABLE_NAME, "null", values);
					values.put(ContractExercise.COLUMN_NAME_REST, "180");
					db.insert(ContractExercise.TABLE_NAME, "null", values);
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
				values.put(ContractWorkoutDay.COLUMN_NAME_DAY_OF_WEEK, 4);
				values.put(ContractWorkoutDay.COLUMN_NAME_EXTERN_ID, newRowWeekId);
				newRowDayId = db.insert(ContractWorkoutDay.TABLE_NAME, "null", values);
					values = new ContentValues();
					values.put(ContractExercise.COLUMN_NAME_EXTERN_ID, newRowDayId);
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
					db.insert(ContractExercise.TABLE_NAME, "null", values);
					db.insert(ContractExercise.TABLE_NAME, "null", values);
				values = new ContentValues();
				values.put(ContractWorkoutDay.COLUMN_NAME_NAME, "Friday - Chest & Abs");
				values.put(ContractWorkoutDay.COLUMN_NAME_DAY_OF_WEEK, 5);
				values.put(ContractWorkoutDay.COLUMN_NAME_EXTERN_ID, newRowWeekId);
				newRowDayId = db.insert(ContractWorkoutDay.TABLE_NAME, "null", values);
					values = new ContentValues();
					values.put(ContractExercise.COLUMN_NAME_EXTERN_ID, newRowDayId);
					values.put(ContractExercise.COLUMN_NAME_NREP, 0);
					values.put(ContractExercise.COLUMN_NAME_WEIGHT, 0.f);
					values.put(ContractExercise.COLUMN_NAME_REPRANGE, "8 to 12");
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

	public boolean isSubProgramCompleted(String programName,
											String subProgramName){
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
	}

	public List<String> getAvailableWeeks(String programName,
											String subProgramName){
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
		return weeks;
	}

	protected int getIdSubProgram(String programName,
									String subProgramName){
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
		return idSubProgram;
	}
	
	public List<String> getAvailableDays(String programName,
										String subProgramName,
										String week){
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
		return days;
	}
	
	protected int getIdWeek(String programName,
							String subProgramName,
							String week){
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
		return idWeek;
	}

	public boolean isWeekCompleted(String programName,
									String subProgramName,
									String week){
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
	}
	

	
	public List<Exercice> getAvailableExercices(String programName,
												String subProgramName,
												String week,
												String day){
		int idDay = this.getIdDay(programName, subProgramName, week, day);
		List<Exercice> exercises = this.getAvailableExercices(programName,
															subProgramName,
															week,
															idDay);
		return exercises;
	}

	public List<Exercice> getAvailableExercices(String programName,
												String subProgramName,
												String week,
												int idDay){
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
		}
		return exercises;
	}

	protected int getIdDay(String programName,
							String subProgramName,
							String week,
							String day){
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
		return idDay;
	}
	
	public List<Exercice> getPreviousExercices(String programName,
												String subProgramName,
												String week,
												String day){
		int idPreviousDay = this.getIdPreviousDay(programName, subProgramName, week, day);
		List<Exercice> exercises = this.getAvailableExercices(programName,
				subProgramName,
				week,
				idPreviousDay);
		return exercises;
	}
	
	protected int getIdPreviousDay(String programName,
									String subProgramName,
									String week,
									String day){
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
		return idPreviousDay;
	}
	
	static protected boolean exercisesListEquals(List<Exercice> exercises1, List<Exercice> exercises2){
		if(exercises1 != null
		&& exercises2 != null
		&& exercises1.size() == exercises2.size()){
			int nExercises = exercises1.size();
			for(int i=0; i<nExercises; i++){
				String name1 = exercises1.get(i).getName();
				String name2 = exercises2.get(i).getName();
				if(!name1.equals(name2)){
					return false;
				}
			}
		}else{
			return false;
		}
		return true;
	}
	
	public boolean isDayCompleted(String programName,
			String subProgramName,
			String week,
			String day){
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
	}
	
	//*
	public boolean isExerciseCompleted(int exerciseId){
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
		//TODO optimize
		while(cursorExercise.moveToNext()){
			int currentExerciseId = cursorExercise.getInt(0);
			if(currentExerciseId == exerciseId){
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
				return completed;
			}
		}
		return false;
	}
	//*/
	
	protected void setExerciceInfo(String exerciseId,
									String rest,
									String weight,
									String nReps){
		ContentValues values = new ContentValues();
		values.put(ContractExercise.COLUMN_NAME_REST, rest);
		values.put(ContractExercise.COLUMN_NAME_WEIGHT, weight);
		values.put(ContractExercise.COLUMN_NAME_NREP, nReps);
		SQLiteDatabase db = this.getWritableDatabase();
		db.update(ContractExercise.TABLE_NAME,
					values, ContractExercise.COLUMN_NAME_ID + "=" + exerciseId,
					null);
	}
	
	/*
	protected int getIdExercise(String programName,
								String subProgramName,
								String week,
								String day,
								String exerciceName){
		SQLiteDatabase db = this.getReadableDatabase();
		int idDay = this.getIdDay(programName, subProgramName, week, day);
		String[] projectionExercice = {
				ContractExercise.COLUMN_NAME_ID,
				ContractExercise.COLUMN_NAME_NAME,
				ContractExercise.COLUMN_NAME_EXTERN_ID
				};
		Cursor cursorExercise = db.query(ContractExercise.TABLE_NAME, projectionExercice,                               // The columns to return
								null, null, null, null, null);
		int idExercice = 0;
		while(cursorExercise.moveToNext()){
			int idExtDay = cursorExercise.getInt(2);
			if(idExtDay == idDay){
				String currentExercice = cursorExercise.getString(1);
				if(currentExercice.equals(exerciceName)){
					idExercice = cursorExercise.getInt(0);
				}
			}
		}
		return idExercice;
	}
	//*/
	
}

