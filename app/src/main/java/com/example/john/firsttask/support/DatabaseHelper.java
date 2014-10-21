package com.example.john.firsttask.support;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by john on 10.10.14.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "task_database.db";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_SCENARIO = "scenario_table";
    public static final String TABLE_CASE = "case_table";
    public static final String UID = "_id";
    public static final String CASE_ID = "caseId";
    public static final String TEXT = "text";
    public static final String IMAGE_URL = "image_url";
    public static final String YES_CASE_ID = "yes_id";
    public static final String NO_CASE_ID = "no_id";

    private static final String CREATE_TABLE_SCENARIO = "CREATE TABLE IF NOT EXISTS " + TABLE_SCENARIO
            + " (" + UID + " INTEGER PRIMARY KEY AUTOINCREMENT," + TEXT + " VARCHAR(255),"
            + CASE_ID + " VARCHAR(255));";

    private static final String CREATE_TABLE_CASE = "CREATE TABLE IF NOT EXISTS " + TABLE_CASE +
            " (" + UID + " INTEGER PRIMARY KEY AUTOINCREMENT," + TEXT + " VARCHAR(255)," + IMAGE_URL +
            " VARCHAR(255)," + NO_CASE_ID + " VARCHAR(255)," + YES_CASE_ID + " VARCHAR(255));";

    private static final String DELETE_SCENARIO = "DROP TABLE IF EXISTS " + TABLE_SCENARIO;
    private static final String DELETE_CASE = "DROP TABLE IF EXISTS " + TABLE_CASE;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_SCENARIO);
        db.execSQL(CREATE_TABLE_CASE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DELETE_SCENARIO);
        db.execSQL(DELETE_CASE);
        onCreate(db);
    }


}

