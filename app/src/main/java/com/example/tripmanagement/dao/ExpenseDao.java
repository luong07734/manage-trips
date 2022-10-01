package com.example.tripmanagement.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.tripmanagement.DatabaseHelper;
import com.example.tripmanagement.model.Expense;
import com.example.tripmanagement.model.Trip;

import java.util.ArrayList;

public class ExpenseDao {

    public static ArrayList<Expense> getExpenseList(Context context, int _tripId) {
        ArrayList<Expense> listOfExpense = new ArrayList<>();
        DatabaseHelper helper = new DatabaseHelper(context);
        SQLiteDatabase database = helper.getReadableDatabase();
        Cursor cs = database.rawQuery("Select * from expenses where trip_id=?", new String[]{_tripId+""});
        cs.moveToFirst();
        while (!cs.isAfterLast()) {
            int expenseId = cs.getInt(0);
            String type = cs.getString(1);
            double amount = cs.getDouble(2);
            String time = cs.getString(3);
            String comment = cs.getString(4);
            int tripId = cs.getInt(5);


            Expense expense = new Expense(expenseId, type, amount, time, comment, tripId);
            listOfExpense.add(expense);
            cs.moveToNext();
        }
        cs.close();
        database.close();
        return listOfExpense;
    }

    public static boolean insert(Context context, String type, double amount, String time, String comment, int tripId){
        DatabaseHelper helper = new DatabaseHelper(context);
        SQLiteDatabase database = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("type", type);
        values.put("amount", amount);
        values.put("time", time);
        values.put("comments", comment);
        values.put("trip_id", tripId);
        long newRow = database.insert("expenses", null, values);
        return (newRow >0);
    }

    public static boolean update(Context context, Expense expense){
        DatabaseHelper helper = new DatabaseHelper(context);
        SQLiteDatabase database = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("type", expense.getType());
        values.put("amount", expense.getAmount());
        values.put("time", expense.getTime());
        values.put("comments", expense.getComment());
        values.put("trip_id", expense.getTripId());
        int row = database.update("expenses", values, "expenses_id=?", new String[]{expense.getExpenseId()+ ""});
        return (row >0);
    }

    public static boolean deleteAll(Context context){
        DatabaseHelper helper = new DatabaseHelper(context);
        SQLiteDatabase database = helper.getWritableDatabase();
        int row = database.delete("expenses", null,null);
        return (row >0);
    }

    public static boolean deleteExpensesOfATrip(Context context, int tripId){
        DatabaseHelper helper = new DatabaseHelper(context);
        SQLiteDatabase database = helper.getWritableDatabase();
        int row = database.delete("expenses", "trip_id=?",new String[]{tripId+""});
        return (row >0);
    }

    // delete an expense
    public static boolean delete(Context context, int expenseId){
        DatabaseHelper helper = new DatabaseHelper(context);
        SQLiteDatabase database = helper.getWritableDatabase();
        int row = database.delete("expenses", "expenses_id=?", new String[]{expenseId+""});
        return (row >0);
    }
}
