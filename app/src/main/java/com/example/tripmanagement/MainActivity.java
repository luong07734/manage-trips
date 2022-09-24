package com.example.tripmanagement;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.DialogInterface;
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
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
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
    RecyclerTouchListener touchListener;
    TextView tvDeleteAll;
    ImageView ivBackground;
    TextView tvNoTrip;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        // find view by id
        rvTrip = findViewById(R.id.trip_list_rv);
        btnAdd = findViewById(R.id.add_btn);
        tvDeleteAll = findViewById(R.id.tv_delete_all);
        ivBackground = findViewById(R.id.iv_background_image);
        tvNoTrip = findViewById(R.id.tv_no_trip);


        // recyclerView settings
        tripList = TripDao.getAll(this);
        displaySuitableViewsWhenListIsEmptyAndViceVersa();
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

        // swipe action
        touchListener = new RecyclerTouchListener(this, rvTrip);
        swipeToDisplayMenu();
        rvTrip.addOnItemTouchListener(touchListener);

        // delete all
        tvDeleteAll.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                showDeleteAllDialog();
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
                    showConfirmationDialog(dialog, name, destination, date, riskAssessment, description);
                } else {
                    // TODO
                }


            }
        });

    }

    private void showConfirmationDialog(Dialog parentDialog, String name, String destination, String date, String riskAssessment, String description) {
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

        btnOk.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (TripDao.insert(MainActivity.this, name, destination, date, riskAssessment, description)) {
                    Toast.makeText(MainActivity.this, "Add new trip successfully!!", Toast.LENGTH_SHORT).show();
                    tripList.clear();
                    tripList.addAll(TripDao.getAll(MainActivity.this));
                    tripListAdapter.notifyDataSetChanged();
                    displaySuitableViewsWhenListIsEmptyAndViceVersa();
                    secondDialog.cancel();
                    parentDialog.cancel();
                } else {
                    Toast.makeText(MainActivity.this, "Add new trip failed!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean allRequiredFieldsFilled(String name, String destination, String date) {
        return !name.isEmpty() && !destination.isEmpty() && !date.isEmpty();

    }

    private void showEditDialog(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_edit_trip, null);
        builder.setView(view);
        Dialog dialog = builder.create();
        dialog.show();

        TextInputEditText tietName = view.findViewById(R.id.edit_trip_name_tiet);
        TextInputEditText tietDestination = view.findViewById(R.id.edit_trip_destination_tiet);
        TextInputEditText tietDescription = view.findViewById(R.id.edit_trip_description_tiet);
        TextInputEditText tietDate = view.findViewById(R.id.edit_trip_date_tiet);
        RadioButton rbYes = view.findViewById(R.id.edit_radio_button_yes);
        RadioButton rbNo = view.findViewById(R.id.edit_radio_button_no);
        Button btnCancel = view.findViewById(R.id.edit_cancel_btn);
        Button btnUpdate = view.findViewById(R.id.update_trip_btn);

        Trip selectedTrip = tripList.get(position);
        tietName.setText(selectedTrip.getTripName());
        tietDescription.setText(selectedTrip.getDescription());
        tietDestination.setText(selectedTrip.getDestination());
        tietDate.setText(selectedTrip.getDate());
        if (selectedTrip.getRiskAssessment().equals("Yes")) {
            rbYes.setChecked(true);
        } else {
            rbNo.setChecked(true);
        }

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

        btnUpdate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (allRequiredFieldsFilled(tietName.getText().toString(), tietDestination.getText().toString(), tietDate.getText().toString())) {
                    selectedTrip.setTripName(tietName.getText().toString());
                    selectedTrip.setDestination(tietDestination.getText().toString());
                    selectedTrip.setDate(tietDate.getText().toString());
                    selectedTrip.setDescription(tietDescription.getText().toString());
                    selectedTrip.setRiskAssessment("Yes");
                    if (rbNo.isChecked()) {
                        selectedTrip.setRiskAssessment("No");
                    }
                    if (TripDao.update(MainActivity.this, selectedTrip)) {
                        Toast.makeText(MainActivity.this, "Update trip successfully!!", Toast.LENGTH_SHORT).show();
                        tripList.clear();
                        tripList.addAll(TripDao.getAll(MainActivity.this));
                        tripListAdapter.notifyDataSetChanged();
                        displaySuitableViewsWhenListIsEmptyAndViceVersa();
                        dialog.cancel();
                    } else {
                        Toast.makeText(MainActivity.this, "Update trip failed!!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // TODO
                }


            }
        });

    }

    private void swipeToDisplayMenu(){
        touchListener
                .setClickable(new RecyclerTouchListener.OnRowClickListener() {
                    @Override
                    public void onRowClicked(int position) {
                        Toast.makeText(getApplicationContext(), "on click new", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onIndependentViewClicked(int independentViewID, int position) {

                    }
                })
                .setSwipeOptionViews(R.id.delete_task, R.id.edit_task)
                .setSwipeable(R.id.rowFG, R.id.rowBG, new RecyclerTouchListener.OnSwipeOptionsClickListener() {
                    @Override
                    public void onSwipeOptionClicked(int viewID, int position) {
                        Trip selectedTrip = tripList.get(position);
                        switch (viewID) {
                            case R.id.delete_task:
                                AlertDialog.Builder alertDialogDelete = new AlertDialog.Builder(
                                        MainActivity.this);
                                // Setting Dialog Title
                                alertDialogDelete.setTitle("Alert Dialog");
                                // Setting OK Button
                                alertDialogDelete.setPositiveButton("YES",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (TripDao.delete(MainActivity.this, selectedTrip.getTripId())) {
                                                    Toast.makeText(getApplicationContext(), "Delete successfully", Toast.LENGTH_SHORT).show();
                                                    tripList.clear();
                                                    tripList.addAll(TripDao.getAll(MainActivity.this));
                                                    tripListAdapter.notifyDataSetChanged();
                                                    displaySuitableViewsWhenListIsEmptyAndViceVersa();

                                                } else {
                                                    Toast.makeText(getApplicationContext(), "Delete failed", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
// Setting Negative "NO" Btn
                                alertDialogDelete.setNegativeButton("NO",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();
                                            }
                                        });

// Showing Alert Dialog
                                alertDialogDelete.show();


                                break;
                            case R.id.edit_task:
                                showEditDialog(position);
                                break;

                        }
                    }
                });
    }

    private void showDeleteAllDialog(){
        AlertDialog.Builder alertDialogDelete = new AlertDialog.Builder(
                MainActivity.this);
        // Setting Dialog Title
        alertDialogDelete.setTitle("Alert Dialog");
        // Setting OK Button
        alertDialogDelete.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (TripDao.deleteAll(MainActivity.this)) {
                            Toast.makeText(getApplicationContext(), "Delete successfully", Toast.LENGTH_SHORT).show();
                            tripList.clear();
                            tripList.addAll(TripDao.getAll(MainActivity.this));
                            tripListAdapter.notifyDataSetChanged();
                            displaySuitableViewsWhenListIsEmptyAndViceVersa();

                        } else {
                            Toast.makeText(getApplicationContext(), "Delete failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
// Setting Negative "NO" Btn
        alertDialogDelete.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

// Showing Alert Dialog
        alertDialogDelete.show();
    }

    private void displaySuitableViewsWhenListIsEmptyAndViceVersa(){
        if(tripList.isEmpty()){
            tvNoTrip.setVisibility(View.VISIBLE);
            ivBackground.setVisibility(View.VISIBLE);
            tvDeleteAll.setVisibility(View.GONE);
        }else {
            tvNoTrip.setVisibility(View.GONE);
            ivBackground.setVisibility(View.GONE);
            tvDeleteAll.setVisibility(View.VISIBLE);
        }
    }

}

