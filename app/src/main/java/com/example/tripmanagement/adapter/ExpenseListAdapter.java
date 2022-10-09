package com.example.tripmanagement.adapter;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tripmanagement.R;
import com.example.tripmanagement.dao.ExpenseDao;
import com.example.tripmanagement.model.Expense;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Locale;
import java.util.Objects;

public class ExpenseListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final Context context;
    private final ArrayList<Expense> expenseList;

    public ExpenseListAdapter(Context _context, ArrayList<Expense> _expenseList) {
        this.expenseList = _expenseList;
        this.context = _context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView txtType;
        private final TextView txtAmount;
        private final TextView txtComment;
        private final TextView txtTime;
        private final ImageView btnDelete;
        private final ImageView btnEdit;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtType = itemView.findViewById(R.id.expense_type_tv);
            txtAmount = itemView.findViewById(R.id.expense_amount_tv);
            txtComment = itemView.findViewById(R.id.expense_comment_tv);
            txtTime = itemView.findViewById(R.id.expense_time_tv);
            btnDelete = itemView.findViewById(R.id.expense_delete_btn);
            btnEdit = itemView.findViewById(R.id.expense_edit_btn);

        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View expenseView = inflater.inflate(R.layout.row_expense, parent, false);

        return new ViewHolder(expenseView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Expense expense = expenseList.get(position);
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.txtType.setText(expense.getType());
        viewHolder.txtAmount.setText("£ " + expense.getAmount());
        viewHolder.txtTime.setText(expense.getTime());
        viewHolder.txtComment.setText(expense.getComment());

        // delete expense onclick
        viewHolder.btnDelete.setOnClickListener(view -> {
            AlertDialog.Builder alertDialogDelete = new AlertDialog.Builder(
                    view.getContext());
            // Setting Dialog Title
            alertDialogDelete.setTitle(R.string.delete_the_expense);
            alertDialogDelete.setMessage(R.string.confirm_datele_expense);
            // Setting OK Button
            alertDialogDelete.setPositiveButton("YES",
                    (dialog, which) -> {
                        if (ExpenseDao.delete(view.getContext(), expenseList.get(viewHolder.getAdapterPosition()).getExpenseId())) {

                            Toast.makeText(view.getContext(), context.getResources().getString(R.string.delete_expense_successfully), Toast.LENGTH_SHORT).show();
                            expenseList.clear();
                            expenseList.addAll(ExpenseDao.getExpenseList(view.getContext(), expense.getTripId()));
                            notifyItemRemoved(viewHolder.getAdapterPosition());

                        } else {
                            Toast.makeText(view.getContext(), "Delete failed", Toast.LENGTH_SHORT).show();
                        }
                    });

            alertDialogDelete.setNegativeButton("NO",
                    (dialog, which) -> dialog.cancel());
            alertDialogDelete.show();
        });

        //edit expense onclick
        viewHolder.btnEdit.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View dialogView = inflater.inflate(R.layout.dialog_update_expense, null);
            builder.setView(dialogView);
            Dialog dialog = builder.create();
            dialog.show();

            TextInputEditText tietType = dialogView.findViewById(R.id.update_expense_type_tiet);
            TextInputEditText tietAmount = dialogView.findViewById(R.id.update_expense_amount_tiet);
            TextInputEditText tietDate = dialogView.findViewById(R.id.update_expense_date_tiet);
            TextInputEditText tietTime = dialogView.findViewById(R.id.update_expense_time_tiet);
            TextInputEditText tietComment = dialogView.findViewById(R.id.update_expense_comment_tiet);
            Button btnCancel = dialogView.findViewById(R.id.update_expense_cancel_btn);
            Button btnUpdate = dialogView.findViewById(R.id.update_expense_btn);

            Expense selectedExpense = expenseList.get(viewHolder.getAdapterPosition());

            tietType.setText(selectedExpense.getType());
            tietAmount.setText(selectedExpense.getAmount().toString());
            String[] dateAndTime = selectedExpense.getTime().split(" ", 2);
            tietDate.setText(dateAndTime[0]);
            tietTime.setText(dateAndTime[1]);
            tietComment.setText(selectedExpense.getComment());

            Calendar calendar = Calendar.getInstance();
            String dateFormat = "MM/dd/yyyy";
            String timeFormat = "HH:mm";

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
                    new DatePickerDialog(view.getContext(), date, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
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
                    TimePickerDialog timePickerDialog = new TimePickerDialog(view.getContext(), time, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
                    timePickerDialog.show();
                }
            });

            btnCancel.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    dialog.cancel();
                }
            });

            btnUpdate.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    if (allRequiredFieldsFilled(Objects.requireNonNull(tietType.getText()).toString(), Objects.requireNonNull(tietAmount.getText()).toString())) {
                        selectedExpense.setType(tietType.getText().toString());
                        selectedExpense.setAmount(Double.parseDouble(tietAmount.getText().toString()));
                        selectedExpense.setTime(Objects.requireNonNull(tietDate.getText()) + " " + Objects.requireNonNull(tietTime.getText()));
                        selectedExpense.setComment(Objects.requireNonNull(tietComment.getText()).toString());

                        if (ExpenseDao.update(view.getContext(), selectedExpense)) {
                            Toast.makeText(view.getContext(), "Update expense successfully!!", Toast.LENGTH_SHORT).show();
                            expenseList.clear();
                            expenseList.addAll(ExpenseDao.getExpenseList(view.getContext(), expense.getTripId()));
                            Collections.reverse(expenseList);
                            notifyItemChanged(viewHolder.getAdapterPosition());
//                        displaySuitableViewsWhenListIsEmptyAndViceVersa();
                            dialog.cancel();

                        } else {
                            Toast.makeText(view.getContext(), "Update expense failed!!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(view.getContext(), "Please fill all the required fields", Toast.LENGTH_SHORT).show();
                    }
                }
            });


        });
    }

    private boolean allRequiredFieldsFilled(String type, String amount) {
        return !type.isEmpty() && !amount.isEmpty();

    }

    @Override
    public int getItemCount() {
        return expenseList.size();
    }
}
