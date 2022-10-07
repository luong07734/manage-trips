package com.example.tripmanagement;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.tripmanagement.adapter.ExpenseListAdapter;
import com.example.tripmanagement.adapter.TripListAdapter;
import com.example.tripmanagement.dao.ExpenseDao;
import com.example.tripmanagement.dao.TripDao;
import com.example.tripmanagement.model.Expense;
import com.example.tripmanagement.model.Trip;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Locale;

public class ExpenseActivity extends AppCompatActivity {
    RecyclerView rvExpense;
    ExpenseListAdapter expenseListAdapter;
    TextView tvAdd;
    ImageView btnBack;
    ArrayList<Expense> expenseList = new ArrayList<>();
    TextView tvNameInfo;
    TextView tvDestinationInfo;
    TextView tvDateInfo;
    TextView tvRiskAssessmentInfo;
    TextView tvDescriptionInfo;
    int tripId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense);

        Intent intent = this.getIntent();
        tripId = intent.getIntExtra("trip_id", 0);
        String tripName = intent.getStringExtra("trip_name");
        String tripDestination = intent.getStringExtra("trip_destination");
        String tripDate = intent.getStringExtra("trip_date");
        String tripDescription = intent.getStringExtra("trip_description");
        String tripRiskAssessment = intent.getStringExtra("trip_risk_assessment");


        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        tvAdd = findViewById(R.id.expense_add_btn);
        rvExpense = findViewById(R.id.expense_list_rv);
        btnBack = findViewById(R.id.back_btn);
        tvNameInfo = findViewById(R.id.tv_name_info);
        tvDestinationInfo = findViewById(R.id.tv_destination_info);
        tvDateInfo = findViewById(R.id.tv_date_info);
        tvRiskAssessmentInfo = findViewById(R.id.tv_risk_assessment_info);
        tvDescriptionInfo = findViewById(R.id.tv_description_info);

        // recycler view
        expenseList = ExpenseDao.getExpenseList(this, tripId);
        Collections.reverse(expenseList);
//        displaySuitableViewsWhenListIsEmptyAndViceVersa();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        expenseListAdapter = new ExpenseListAdapter(this, expenseList);
        rvExpense.setLayoutManager(linearLayoutManager);
        rvExpense.setAdapter(expenseListAdapter);

        // setup textviews
        tvNameInfo.setText(tripName);
        tvDestinationInfo.setText(tripDestination);
        tvDateInfo.setText(tripDate);
        tvDescriptionInfo.setText(tripDescription);
        tvRiskAssessmentInfo.setText(tripRiskAssessment);


        tvAdd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                showAddDialog();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


    }

    private void showAddDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_expense, null);
        builder.setView(view);
        Dialog dialog = builder.create();
        dialog.show();

        TextInputEditText tietType = view.findViewById(R.id.expense_type_tiet);
        TextInputEditText tietAmount = view.findViewById(R.id.expense_amount_tiet);
        TextInputEditText tietDate = view.findViewById(R.id.expense_date_tiet);
        TextInputEditText tietTime = view.findViewById(R.id.expense_time_tiet);
        TextInputEditText tietComment = view.findViewById(R.id.expense_comment_tiet);
        Button btnCancel = view.findViewById(R.id.expense_cancel_btn);
        Button btnAdd = view.findViewById(R.id.add_expense_btn);

        Calendar calendar = Calendar.getInstance();
        String dateFormat = "MM/dd/yyyy";
        String timeFormat = "HH:mm";
        SimpleDateFormat dateSdf = new SimpleDateFormat(dateFormat, Locale.getDefault());
        tietDate.setText(dateSdf.format(calendar.getTime()));
        SimpleDateFormat timeSdf = new SimpleDateFormat(timeFormat, Locale.getDefault());
        tietTime.setText(timeSdf.format(calendar.getTime()));


        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                calendar.set(Calendar.YEAR, i);
                calendar.set(Calendar.MONTH, i1);
                calendar.set(Calendar.DAY_OF_MONTH, i2);

                updateDateOnTextField();
            }

            private void updateDateOnTextField() {

                SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.getDefault());
                tietDate.setText(sdf.format(calendar.getTime()));
            }
        };

        tietDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                new DatePickerDialog(ExpenseActivity.this, date, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        TimePickerDialog.OnTimeSetListener time = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                calendar.set(Calendar.HOUR_OF_DAY, i);
                calendar.set(Calendar.MINUTE, i1);
                updateTimeOnTextField();
            }

            private void updateTimeOnTextField() {

                SimpleDateFormat sdf = new SimpleDateFormat(timeFormat, Locale.getDefault());
                tietTime.setText(sdf.format(calendar.getTime()));
            }
        };

        tietTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(ExpenseActivity.this, time, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
                timePickerDialog.show();
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
                if (allRequiredFieldsFilled(tietType.getText().toString(), tietAmount.getText().toString())) {
                    String type = tietType.getText().toString();
                    Double amount = Double.parseDouble(tietAmount.getText().toString());
                    String time = tietDate.getText().toString() + " " + tietTime.getText().toString();
                    String comment = tietComment.getText().toString();

                    if (ExpenseDao.insert(ExpenseActivity.this, type, amount, time, comment, tripId)) {
                        Toast.makeText(ExpenseActivity.this, "Add new trip successfully!!", Toast.LENGTH_SHORT).show();
                        expenseList.clear();
                        expenseList.addAll(ExpenseDao.getExpenseList(ExpenseActivity.this, tripId));
                        Collections.reverse(expenseList);
                        expenseListAdapter.notifyDataSetChanged();
//                        displaySuitableViewsWhenListIsEmptyAndViceVersa();
                        dialog.cancel();

                    } else {
                        Toast.makeText(ExpenseActivity.this, "Add new trip failed!!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ExpenseActivity.this, "Please fill all the required fields", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

    private boolean allRequiredFieldsFilled(String type, String amount) {
        return !type.isEmpty() && !amount.isEmpty();

    }


}