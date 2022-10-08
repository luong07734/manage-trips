package com.example.tripmanagement;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    // create database
    public DatabaseHelper(Context context) {
        super(context, "trip_database", null, 1);
    }


    // create tables
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql_create_trip_table = "Create table trips(trip_id integer primary key autoincrement," +
                "name text not null, destination text not null, date text, risk_assessment text not null, description text)";
        String sql_create_expense_table = "Create table expenses(expenses_id integer primary key autoincrement," +
                "type text not null, amount real not null, time text not null, comments text, trip_id integer )";
        String[] statements = new String[]{sql_create_trip_table, sql_create_expense_table};

        for (String sql : statements) {
            sqLiteDatabase.execSQL(sql);
        }

    }

    // invoke when version changed
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("Drop table if exists trips");
        sqLiteDatabase.execSQL("Drop table if exists expenses");
        onCreate(sqLiteDatabase);
    }
}
