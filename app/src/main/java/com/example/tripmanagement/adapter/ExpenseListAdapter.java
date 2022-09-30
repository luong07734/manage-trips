package com.example.tripmanagement.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tripmanagement.R;
import com.example.tripmanagement.model.Expense;
import com.example.tripmanagement.model.Trip;

import java.util.ArrayList;

public class ExpenseListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context context;
    private ArrayList<Expense> expenseList;

    public ExpenseListAdapter(Context _context, ArrayList<Expense> _expenseList){
        this.expenseList = _expenseList;
        this.context = _context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView txtType;
        private TextView txtAmount;
        private TextView txtComment;
        private TextView txtTime;
        private ImageView btnDelete;
        private ImageView btnEdit;
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

        return new ExpenseListAdapter.ViewHolder(expenseView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Expense expense = expenseList.get(position);
        ViewHolder viewHolder = (ViewHolder)holder;
        viewHolder.txtType.setText(expense.getType());
        viewHolder.txtAmount.setText(expense.getAmount().toString());
        viewHolder.txtTime.setText(expense.getTime());
        viewHolder.txtComment.setText(expense.getComment());
        viewHolder.btnDelete.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return expenseList.size();
    }
}
