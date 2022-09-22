package com.example.tripmanagement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.tripmanagement.adapter.TripListAdapter;
import com.example.tripmanagement.dao.TripDao;
import com.example.tripmanagement.model.Trip;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView rvTrip;
    TripListAdapter tripListAdapter;
    ArrayList<Trip> tripList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rvTrip = findViewById(R.id.trip_list_rv);

        tripList = TripDao.getAll(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        tripListAdapter = new TripListAdapter(this, tripList);
        rvTrip.setAdapter(tripListAdapter);
        rvTrip.setLayoutManager(linearLayoutManager);

    }
}