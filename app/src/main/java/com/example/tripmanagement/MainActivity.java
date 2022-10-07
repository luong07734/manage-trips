package com.example.tripmanagement;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.example.tripmanagement.adapter.TripListAdapter;
import com.example.tripmanagement.dao.ExpenseDao;
import com.example.tripmanagement.dao.TripDao;
import com.example.tripmanagement.model.Trip;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;

import android.app.DatePickerDialog;
import android.widget.ImageView;
import android.widget.RadioButton;

import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
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
    SearchView svSearch;
    ImageView btnFilter;
    TextView tvFilter;
    String format = "MM/dd/yyyy";

    String query = "";
    ArrayList<Trip> filteredTripList = new ArrayList<>();
    String[] filterDateItems = new String[]{"All time", "Past 7 days", "Past 30 days", "Custom time period"};
    String[] filterRiskItems = new String[]{"All", "Yes", "No",};
    int filterRiskAssessment = 0;
    int filterDate = 0;
    String customStartDate = "";
    String customDueDate = "";

    int searchBy = 0; // trip name


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
        svSearch = findViewById(R.id.sv_search);
        btnFilter = findViewById(R.id.iv_search_picker);
        tvFilter = findViewById(R.id.filter_add_btn);

        // recyclerView settings
        tripList = TripDao.getAll(this);
        Collections.reverse(tripList);
        displaySuitableViewsWhenListIsEmptyAndViceVersa();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        tripListAdapter = new TripListAdapter(this, tripList);
        rvTrip.setAdapter(tripListAdapter);
        rvTrip.setLayoutManager(linearLayoutManager);

        filterList(query);
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
        tvDeleteAll.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                showDeleteAllDialog();
            }
        });

        // search
        svSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                query = s;
                filterList(s);
                return false;
            }
        });

        // filter
        btnFilter.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                chooseSearchType();
            }
        });

        tvFilter.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                showFilterDialog();
            }
        });

    }

    private void showFilterDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_filter, null);
        builder.setView(view);
        Dialog dialog = builder.create();
        dialog.show();


        AppCompatSpinner spnDate = view.findViewById(R.id.sp_date);
        AppCompatSpinner spnRiskAssessment = view.findViewById(R.id.sp_risk);
        Button btnCancel = view.findViewById(R.id.filter_cancel_btn);
        Button btnOk = view.findViewById(R.id.filter_confirm_btn);

        TextView tvToTitle = view.findViewById(R.id.tv_to_title);
        TextView tvFromTitle = view.findViewById(R.id.tv_from_title);
        TextInputLayout startLayout = view.findViewById(R.id.filter_start_date_til);
        TextInputLayout endLayout = view.findViewById(R.id.filter_due_date_til);


        TextInputEditText tietStartDate = view.findViewById(R.id.filter_start_date_tiet);
        TextInputEditText tietDueDate = view.findViewById(R.id.filter_due_date_tiet);


        tvToTitle.setVisibility(View.GONE);
        tvFromTitle.setVisibility(View.GONE);
        startLayout.setVisibility(View.GONE);
        endLayout.setVisibility(View.GONE);
        tietStartDate.setText(customStartDate);
        tietDueDate.setText(customDueDate);


        final int[] date = {filterDate};
        final int[] risk = {filterRiskAssessment};
        final String[] period = new String[]{customStartDate, customDueDate};
        ArrayAdapter<String> dateAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_activated_1, filterDateItems);
        spnDate.setAdapter(dateAdapter);
        spnDate.setSelection(filterDate);
        spnDate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int pos = adapterView.getSelectedItemPosition();
                date[0] = pos;
                if (pos == 3) {
//                    showDatePickerDialog(period);
                    tvToTitle.setVisibility(View.VISIBLE);
                    tvFromTitle.setVisibility(View.VISIBLE);
                    startLayout.setVisibility(View.VISIBLE);
                    endLayout.setVisibility(View.VISIBLE);


                    Calendar calendar = Calendar.getInstance();
                    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                            calendar.set(Calendar.YEAR, i);
                            calendar.set(Calendar.MONTH, i1);
                            calendar.set(Calendar.DAY_OF_MONTH, i2);

                            updateCalendarOnTextField();
                        }

                        private void updateCalendarOnTextField() {
                            SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
                            tietStartDate.setText(sdf.format(calendar.getTime()));
                            period[0] = tietStartDate.getText().toString();
                        }
                    };

                    Calendar calendar2 = Calendar.getInstance();
                    DatePickerDialog.OnDateSetListener date2 = new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                            calendar.set(Calendar.YEAR, i);
                            calendar.set(Calendar.MONTH, i1);
                            calendar.set(Calendar.DAY_OF_MONTH, i2);

                            updateCalendarOnTextField();
                        }

                        private void updateCalendarOnTextField() {
                            SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
                            tietDueDate.setText(sdf.format(calendar.getTime()));
                            period[1] = tietDueDate.getText().toString();
                        }
                    };

                    tietStartDate.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {
                            new DatePickerDialog(MainActivity.this, date, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                                    calendar.get(Calendar.DAY_OF_MONTH)).show();
                        }
                    });

                    tietDueDate.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {
                            new DatePickerDialog(MainActivity.this, date2, calendar2.get(Calendar.YEAR), calendar2.get(Calendar.MONTH),
                                    calendar2.get(Calendar.DAY_OF_MONTH)).show();
                        }
                    });
                } else {
                    tvToTitle.setVisibility(View.GONE);
                    tvFromTitle.setVisibility(View.GONE);
                    startLayout.setVisibility(View.GONE);
                    endLayout.setVisibility(View.GONE);
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });

        ArrayAdapter<String> riskAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_activated_1, filterRiskItems);
        spnRiskAssessment.setAdapter(riskAdapter);
        spnRiskAssessment.setSelection(filterRiskAssessment);
        spnRiskAssessment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int pos = adapterView.getSelectedItemPosition();
                risk[0] = pos;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        btnCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if (date[0] != 3) {
                    filterDate = date[0];
                    filterRiskAssessment = risk[0];
                    customStartDate = "";
                    customDueDate = "";
                    filterList(query);
                    dialog.cancel();
                } else if (!tietStartDate.getText().toString().isEmpty() && !tietStartDate.getText().toString().isEmpty()) {
                    filterDate = date[0];
                    filterRiskAssessment = risk[0];
                    customStartDate = period[0];
                    customDueDate = period[1];
                    filterList(query);
                    dialog.cancel();
                } else {
                    Toast.makeText(MainActivity.this, "Please fill all the required fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    private void chooseSearchType() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Search by");

        // add a radio button list
        String[] options = {"Trip Name", "Trip Destination"};
        final int[] choice = {0};
        builder.setSingleChoiceItems(options, searchBy, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // user checked an item
                Toast.makeText(MainActivity.this, which + "", Toast.LENGTH_SHORT).show();
                choice[0] = which;
            }
        });

        // add OK and Cancel buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // user clicked OK
                searchBy = choice[0];
                if (searchBy == 0) {
                    svSearch.setQueryHint("Search by trip name");
                } else {
                    svSearch.setQueryHint("Search by trip destination");
                }
                filterList(query);
                dialog.cancel();

            }
        });
        builder.setNegativeButton("Cancel", null);

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private boolean filterByDate(Trip trip) {
        Date currentDate = Calendar.getInstance().getTime();

        Calendar c = Calendar.getInstance();
        c.setTime(currentDate);
        c.add(Calendar.DAY_OF_YEAR, -7);
        Date sevenPreviousDate = c.getTime();

        Calendar c1 = Calendar.getInstance();
        c1.setTime(currentDate);
        c1.add(Calendar.DAY_OF_YEAR, -30);
        Date thirtyPreviousDate = c1.getTime();

        Date tripDate = currentDate;
        try {
            tripDate = new SimpleDateFormat(format).parse(trip.getDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (filterDate == 1) {
            return !sevenPreviousDate.after(tripDate) && !currentDate.before(tripDate);
        } else if (filterDate == 2) {
            return !thirtyPreviousDate.after(tripDate) && !currentDate.before(tripDate);
        } else if (filterDate == 3) {
            Date startDate = currentDate;
            Date dueDate = currentDate;
            try {
                startDate = new SimpleDateFormat(format).parse(customStartDate);
                dueDate = new SimpleDateFormat(format).parse(customDueDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return !startDate.after(tripDate) && !dueDate.before(tripDate);
        } else {
            return true;
        }
    }

    private boolean filterByRiskAssessment(Trip trip) {
        if (filterRiskAssessment == 0) {
            return true;
        } else {
            return filterRiskItems[filterRiskAssessment].equals(trip.getRiskAssessment());
        }
    }

    private void filterList(String text) {
        filteredTripList.clear();
        if (searchBy == 0) {
            for (Trip trip : tripList) {
                if (trip.getTripName().toLowerCase().contains(text.toLowerCase())) {
                    if (filterByRiskAssessment(trip)) {
                        if (filterByDate(trip)) {
                            filteredTripList.add(trip);
                        }

                    }

                }
            }
        } else {
            for (Trip trip : tripList) {
                if (trip.getDestination().toLowerCase().contains(text.toLowerCase())) {
                    if (filterByRiskAssessment(trip)) {
                        if (filterByDate(trip)) {
                            filteredTripList.add(trip);
                        }
                    }
                }
            }
        }

        tripListAdapter.setFilteredList(filteredTripList);
        if (filteredTripList.isEmpty()) {
            Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show();
        }
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

                updateCalendarOnTextField();
            }

            private void updateCalendarOnTextField() {
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
                    Toast.makeText(MainActivity.this, "Please fill all the required fields", Toast.LENGTH_SHORT).show();
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
                    Collections.reverse(tripList);
                    tripListAdapter.notifyDataSetChanged();
                    filterList(query);
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

    private void showEditDialog(Trip selectedTrip) {
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
                        Collections.reverse(tripList);
                        tripListAdapter.notifyDataSetChanged();
                        filterList(query);
                        displaySuitableViewsWhenListIsEmptyAndViceVersa();
                        dialog.cancel();
                    } else {
                        Toast.makeText(MainActivity.this, "Update trip failed!!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Please fill all the required fields", Toast.LENGTH_SHORT).show();
                }


            }
        });

    }

    private void swipeToDisplayMenu() {
        touchListener
                .setClickable(new RecyclerTouchListener.OnRowClickListener() {
                    @Override
                    public void onRowClicked(int position) {
                        Intent intent = new Intent(MainActivity.this, ExpenseActivity.class);
                        Trip selectedTrip = filteredTripList.get(position);
                        intent.putExtra("trip_id", selectedTrip.getTripId());
                        intent.putExtra("trip_name", selectedTrip.getTripName());
                        intent.putExtra("trip_destination", selectedTrip.getDestination());
                        intent.putExtra("trip_date", selectedTrip.getDate());
                        intent.putExtra("trip_description", selectedTrip.getDescription());
                        intent.putExtra("trip_risk_assessment", selectedTrip.getRiskAssessment());
                        startActivity(intent);
                    }

                    @Override
                    public void onIndependentViewClicked(int independentViewID, int position) {

                    }
                })
                .setSwipeOptionViews(R.id.delete_task, R.id.edit_task)
                .setSwipeable(R.id.rowFG, R.id.rowBG, new RecyclerTouchListener.OnSwipeOptionsClickListener() {
                    @Override
                    public void onSwipeOptionClicked(int viewID, int position) {
                        Trip selectedTrip = filteredTripList.get(position);
                        Log.i("test1", filteredTripList.toString());
                        switch (viewID) {
                            case R.id.delete_task:
                                AlertDialog.Builder alertDialogDelete = new AlertDialog.Builder(
                                        MainActivity.this);
                                // Setting Dialog Title
                                alertDialogDelete.setTitle("Delete the trip");
                                alertDialogDelete.setMessage("Are you sure you want to delete this trip?");
                                // Setting OK Button
                                alertDialogDelete.setPositiveButton("YES",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (TripDao.delete(MainActivity.this, selectedTrip.getTripId())) {
                                                    // delete expense
                                                    ExpenseDao.deleteExpensesOfATrip(MainActivity.this, selectedTrip.getTripId());
                                                    Toast.makeText(getApplicationContext(), "Delete successfully", Toast.LENGTH_SHORT).show();
                                                    tripList.clear();
                                                    tripList.addAll(TripDao.getAll(MainActivity.this));
                                                    Collections.reverse(tripList);
                                                    tripListAdapter.notifyDataSetChanged();
                                                    filterList(query);
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
                                showEditDialog(selectedTrip);
                                break;

                        }
                    }
                });
    }

    private void showDeleteAllDialog() {
        AlertDialog.Builder alertDialogDelete = new AlertDialog.Builder(
                MainActivity.this);
        // Setting Dialog Title
        alertDialogDelete.setTitle("Delete all the trips");
        alertDialogDelete.setMessage("Are you sure you want to delete all the trips?");
        // Setting OK Button
        alertDialogDelete.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (TripDao.deleteAll(MainActivity.this)) {
                            // delete all expense
                            ExpenseDao.deleteAll(MainActivity.this);
                            Toast.makeText(getApplicationContext(), "Delete successfully", Toast.LENGTH_SHORT).show();
                            tripList.clear();
                            tripList.addAll(TripDao.getAll(MainActivity.this));
                            Collections.reverse(tripList);
                            tripListAdapter.notifyDataSetChanged();
                            filterList(query);
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

    private void displaySuitableViewsWhenListIsEmptyAndViceVersa() {
        if (tripList.isEmpty()) {
            tvNoTrip.setVisibility(View.VISIBLE);
            ivBackground.setVisibility(View.VISIBLE);
            tvDeleteAll.setVisibility(View.GONE);
        } else {
            tvNoTrip.setVisibility(View.GONE);
            ivBackground.setVisibility(View.GONE);
            tvDeleteAll.setVisibility(View.VISIBLE);
        }
    }

}

