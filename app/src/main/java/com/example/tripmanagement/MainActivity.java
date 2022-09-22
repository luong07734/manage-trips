package com.example.tripmanagement;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.example.tripmanagement.adapter.TripListAdapter;
import com.example.tripmanagement.dao.TripDao;
import com.example.tripmanagement.model.Trip;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import android.widget.Button;
import android.widget.DatePicker;

import android.app.DatePickerDialog;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    RecyclerView rvTrip;
    TripListAdapter tripListAdapter;
    ArrayList<Trip> tripList = new ArrayList<>();
    FloatingActionButton btnAdd;

    Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // find view by id
        rvTrip = findViewById(R.id.trip_list_rv);
        btnAdd = findViewById(R.id.add_btn);


        // recyclerView settings
        tripList = TripDao.getAll(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        tripListAdapter = new TripListAdapter(this, tripList);
        rvTrip.setAdapter(tripListAdapter);
        rvTrip.setLayoutManager(linearLayoutManager);

        // add button onclick
        btnAdd.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                showAddDialog();
            }
        });


    }

    private void showAddDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_trip, null);
        builder.setView(view);
        Dialog dialog = builder.create();
        dialog.show();
        // calendar settings

        TextInputEditText tietDate = view.findViewById(R.id.trip_date_tiet);

        Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                calendar.set(Calendar.YEAR, i);
                calendar.set(Calendar.MONTH, i1);
                calendar.set(Calendar.DAY_OF_MONTH, i2);

                updateCalenderOnTextField();
            }

            private void updateCalenderOnTextField(){
                String format ="dd/MM/yyyy";
                SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
                tietDate.setText(sdf.format(calendar.getTime()));
            }
        };


        // date textfield onclick
        tietDate.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                new DatePickerDialog(MainActivity.this, date, calendar.get(Calendar.YEAR),  calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }


}