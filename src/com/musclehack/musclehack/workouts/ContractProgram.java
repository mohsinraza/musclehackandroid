package com.musclehack.musclehack.workouts;

import android.provider.BaseColumns;

public abstract class ContractProgram implements BaseColumns {
	public static final String TABLE_NAME = "program";
	public static final String COLUMN_NAME_ID = "id_program";
	public static final String COLUMN_NAME_NAME = "name";
	public static final String COLUMN_NAME_COMPLETED = "completed";
	public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" 
												+ COLUMN_NAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
												+ COLUMN_NAME_NAME + " TEXT, "
												+ COLUMN_NAME_COMPLETED + " BOOLEAN);";
	public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME + ";";

	public static abstract class DeleteV01{
		public static final String DROP_TABLE =
				ContractExercise.DROP_TABLE
				+ ContractWorkoutDay.DROP_TABLE
				+ ContractWorkoutWeek.DROP_TABLE
				+ ContractSubProgram.DROP_TABLE
				+ ContractProgram.DROP_TABLE;
	}
	
	public static abstract class ContractSubProgram implements BaseColumns {
		//This is an old table used in the fist version of the database
		public static final String TABLE_NAME = "subprogram";
		/*
		public static final String COLUMN_NAME_ID = "id_subprogram";
		public static final String COLUMN_NAME_NAME = "name";
		public static final String COLUMN_NAME_COMPLETED = "completed";
		public static final String COLUMN_NAME_EXTERN_ID = "id_program";
		public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" 
													+ COLUMN_NAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
													+ COLUMN_NAME_NAME + " TEXT,"
													+ COLUMN_NAME_COMPLETED + " BOOLEAN,"
													+ COLUMN_NAME_EXTERN_ID + " INTEGER NOT NULL,"
													+ "FOREIGN KEY(" + COLUMN_NAME_EXTERN_ID + ") REFERENCES "
													+ ContractProgram.TABLE_NAME + "(" + ContractProgram.COLUMN_NAME_ID + "));";
		//*/
		public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME + ";";
	}

	public static abstract class ContractWorkoutWeek implements BaseColumns {
		public static final String TABLE_NAME = "week";
		public static final String COLUMN_NAME_ID = "id_week";
		public static final String COLUMN_NAME_NAME = "name";
		public static final String COLUMN_NAME_COMPLETED = "completed";
		public static final String COLUMN_NAME_EXTERN_ID = "id_program";
		public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" 
													+ COLUMN_NAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
													+ COLUMN_NAME_NAME + " TEXT,"
													+ COLUMN_NAME_COMPLETED + " BOOLEAN,"
													+ COLUMN_NAME_EXTERN_ID + " INTEGER NOT NULL,"
													+ "FOREIGN KEY(" + COLUMN_NAME_EXTERN_ID + ") REFERENCES "
													+ ContractProgram.TABLE_NAME + "(" + ContractProgram.COLUMN_NAME_ID + "));";
		public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME + ";";
	}

	public static abstract class ContractWorkoutDay implements BaseColumns {
		public static final String TABLE_NAME = "day";
		public static final String COLUMN_NAME_ID = "id_day";
		public static final String COLUMN_NAME_NAME = "name";
		public static final String COLUMN_NAME_COMPLETED = "completed";
		public static final String COLUMN_NAME_DAY_OF_WEEK = "day_of_week";
		public static final String COLUMN_NAME_EXTERN_ID = "id_week";
		public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" 
													+ COLUMN_NAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
													+ COLUMN_NAME_NAME + " TEXT,"
													+ COLUMN_NAME_COMPLETED + " BOOLEAN,"
													+ COLUMN_NAME_DAY_OF_WEEK + " INTEGER,"
													+ COLUMN_NAME_EXTERN_ID + " INTEGER NOT NULL,"
													+ "FOREIGN KEY(" + COLUMN_NAME_EXTERN_ID + ") REFERENCES "
													+ ContractWorkoutWeek.TABLE_NAME + "(" + ContractWorkoutWeek.COLUMN_NAME_ID + "));";
		public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME + ";";
	}

	public static abstract class ContractExercise implements BaseColumns {
		public static final String TABLE_NAME = "exercice";
		public static final String COLUMN_NAME_ID = "id_exercice";
		public static final String COLUMN_NAME_NAME = "name";
		public static final String COLUMN_NAME_COMPLETED = "completed";
		public static final String COLUMN_NAME_NREP = "nrep";
		public static final String COLUMN_NAME_WEIGHT = "weight";
		public static final String COLUMN_NAME_REPRANGE = "reprange";
		public static final String COLUMN_NAME_REST = "rest";
		public static final String COLUMN_NAME_EXTERN_ID = "id_day";
		public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" 
													+ COLUMN_NAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
													+ COLUMN_NAME_NAME + " TEXT,"
													+ COLUMN_NAME_COMPLETED + " BOOLEAN,"
													+ COLUMN_NAME_NREP + " INTEGER,"
													+ COLUMN_NAME_WEIGHT + " REAL,"
													+ COLUMN_NAME_REPRANGE + " TEXT,"
													+ COLUMN_NAME_REST + " INTEGER,"
													+ COLUMN_NAME_EXTERN_ID + " INTEGER NOT NULL,"
													+ "FOREIGN KEY(" + COLUMN_NAME_EXTERN_ID + ") REFERENCES "
													+ ContractWorkoutDay.TABLE_NAME + "(" + ContractWorkoutDay.COLUMN_NAME_ID + "));";
		public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME + ";";
	}
}

