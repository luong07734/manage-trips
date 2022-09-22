package com.example.tripmanagement.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.tripmanagement.DatabaseHelper;
import com.example.tripmanagement.model.Trip;

import java.util.ArrayList;
import java.util.Date;

public class TripDao {

    // get all trips from the local SQLite database
    public static ArrayList<Trip> getAll(Context context) {
        ArrayList<Trip> listOfTrips = new ArrayList<>();
        DatabaseHelper helper = new DatabaseHelper(context);
        SQLiteDatabase database = helper.getReadableDatabase();
        Cursor cs = database.rawQuery("Select * from trips", null);
        cs.moveToFirst();
        while (!cs.isAfterLast()) {
            int tripId = cs.getInt(0);
            String tripName = cs.getString(1);
            String destination = cs.getString(2);
            String date = cs.getString(3);
            String riskAssessment = cs.getString(4);
            String description = cs.getString(5);

            Trip trip = new Trip(tripId, tripName, destination, date, riskAssessment, description);
            listOfTrips.add(trip);
            cs.moveToNext();
        }
        cs.close();
        database.close();
        return listOfTrips;
    }

}
