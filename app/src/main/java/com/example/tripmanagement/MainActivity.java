package com.example.tripmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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

        rvTrip.addOnItemTouchListener(
                new RecyclerItemClickListener(MainActivity.this, rvTrip ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        // do whatever
                        Toast.makeText(MainActivity.this, "on click ", Toast.LENGTH_SHORT).show();
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                        Toast.makeText(MainActivity.this, "on long click ", Toast.LENGTH_SHORT).show();
                    }
                })
        );

        // swipe to delete
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT ) {
            private final ColorDrawable background = new ColorDrawable(getResources().getColor(R.color.teal_200));

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                Toast.makeText(MainActivity.this, "on Move", Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                Toast.makeText(MainActivity.this, "on Swiped ", Toast.LENGTH_SHORT).show();
                //Remove swiped item from list and notify the RecyclerView
                int position = viewHolder.getAdapterPosition();
//                arrayList.remove(position);
//                adapter.notifyDataSetChanged();

            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
//                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
//                    // Get RecyclerView item from the ViewHolder
//                    View itemView = viewHolder.itemView;
//
//                    Paint p = new Paint();
//                    if (dX > 0) {
//                        /* Set your color for positive displacement */
//
//                        // Draw Rect with varying right side, equal to displacement dX
//                        c.drawRect((float) itemView.getLeft(), (float) itemView.getTop(), dX,
//                                (float) itemView.getBottom(), p);
//                    } else {
//                        /* Set your color for negative displacement */
//
//                        // Draw Rect with varying left side, equal to the item's right side plus negative displacement dX
//                        c.drawRect((float) itemView.getRight() + dX, (float) itemView.getTop(),
//                                (float) itemView.getRight(), (float) itemView.getBottom(), p);
//                    }
//
//                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
//                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

                View itemView = viewHolder.itemView;

                if (dX > 0) {
                    background.setBounds(itemView.getLeft(), itemView.getTop(), itemView.getLeft() + ((int) dX), itemView.getBottom());
                } else if (dX < 0) {
                    background.setBounds(itemView.getRight() + ((int) dX), itemView.getTop(), itemView.getRight(), itemView.getBottom());
                } else {
                    background.setBounds(0, 0, 0, 0);
                }

                background.draw(c);

            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(rvTrip);


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
                }
                else{
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

        btnOk.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                if(TripDao.insert(MainActivity.this, name, destination, date, riskAssessment, description )){
                    Toast.makeText(MainActivity.this, "Add new trip successfully!!", Toast.LENGTH_SHORT).show();
                    secondDialog.cancel();
                    parentDialog.cancel();
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

