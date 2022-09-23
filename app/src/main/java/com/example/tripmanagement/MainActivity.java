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
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

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
        btnAdd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                showAddDialog();
            }
        });


    }

    private void showAddDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_trip, null);
        builder.setView(view);
        Dialog dialog = builder.create();
        dialog.show();
        // calendar settings

        TextInputEditText tietName = view.findViewById(R.id.trip_name_tiet);
        TextInputEditText tietDestination = view.findViewById(R.id.trip_destination_tiet);
        TextInputEditText tietDescription = view.findViewById(R.id.trip_description_tiet);
        TextInputEditText tietDate = view.findViewById(R.id.trip_date_tiet);
        RadioButton rbYes = view.findViewById(R.id.radio_button_yes);
        RadioButton rbNo = view.findViewById(R.id.radio_button_no);
        Button btnCancel = view.findViewById(R.id.cancel_btn);
        Button btnAdd = view.findViewById(R.id.add_trip_btn);

        Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                calendar.set(Calendar.YEAR, i);
                calendar.set(Calendar.MONTH, i1);
                calendar.set(Calendar.DAY_OF_MONTH, i2);

                updateCalenderOnTextField();
            }

            private void updateCalenderOnTextField() {
                String format = "dd/MM/yyyy";
                SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
                tietDate.setText(sdf.format(calendar.getTime()));
            }
        };


        // date textfield onclick
        tietDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                new DatePickerDialog(MainActivity.this, date, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        // cancel onclick
        btnCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String name = tietName.getText().toString();
                String destination = tietDestination.getText().toString();
                String date = tietDate.getText().toString();
                String description = tietDescription.getText().toString();
                String riskAssessment = "Yes";
                if (rbNo.isChecked()) {
                    riskAssessment = "No";
                }

                if (allRequiredFieldsFilled(name, destination, date)) {
                    showConfirmationDialog(name, destination, date, riskAssessment, description);
                }
                else{
                    // TODO
                }


            }
        });

    }

    private void showConfirmationDialog(String name, String destination, String date, String riskAssessment, String description) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_trip_confirm, null);
        builder.setView(view);
        Dialog secondDialog = builder.create();
        secondDialog.show();

        Button btnCancel = view.findViewById(R.id.btn_cancel_confirm);
        Button btnOk = view.findViewById(R.id.btn_ok_confirm);
        TextView tvName = view.findViewById(R.id.tv_confirm_name);
        TextView tvDestination = view.findViewById(R.id.tv_confirm_destination);
        TextView tvDate = view.findViewById(R.id.tv_confirm_date);
        TextView tvDescription = view.findViewById(R.id.tv_confirm_description);
        TextView tvRiskAssessment = view.findViewById(R.id.tv_confirm_risk_assessment);

        tvName.setText(name);
        tvDestination.setText(destination);
        tvDate.setText(date);
        tvRiskAssessment.setText(riskAssessment);
        tvDescription.setText(description);

        btnCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                secondDialog.cancel();
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                if(TripDao.insert(MainActivity.this, name, destination, date, riskAssessment, description )){
                    Toast.makeText(MainActivity.this, "Add new trip successfully!!", Toast.LENGTH_SHORT).show();
                    secondDialog.cancel();
                    tripList.clear();
                    tripList.addAll(TripDao.getAll(MainActivity.this));
                    tripListAdapter.notifyDataSetChanged();
                }else {
                    Toast.makeText(MainActivity.this, "Add new trip failed!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean allRequiredFieldsFilled(String name, String destination, String date) {
        return !name.isEmpty() && !destination.isEmpty() && !date.isEmpty();

    }


}