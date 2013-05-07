package com.musclehack.musclehack.workouts;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.musclehack.musclehack.workouts.ContractProgram.ContractSubProgram;
import com.musclehack.musclehack.workouts.ContractProgram.ContractWorkoutDay;
import com.musclehack.musclehack.workouts.ContractProgram.ContractWorkoutWeek;

public class ProgramDbHelper extends SQLiteOpenHelper {
	public static final int DATABASE_VERSION = 1;
	public static final String DATABASE_NAME = "programs.db";

	public ProgramDbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
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
		if(cursor.moveToFirst()){
			programs.add(cursor.getString(0));
			while(cursor.moveToNext()){
				String programName = cursor.getString(0);
				programs.add(programName);
			}
		}
		return programs;
		
	}
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//db.execSQL(SQL_DELETE_ENTRIES);
		//onCreate(db);
	}
	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//onUpgrade(db, oldVersion, newVersion);
	}
}

