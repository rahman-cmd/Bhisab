package com.softhostit.bhisab.expense;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.softhostit.bhisab.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder> {

    private Context context;
    List<ExpenseModel> expenseModelList;

    public ExpenseAdapter(Context context, List<ExpenseModel> expenseModelList) {
        this.context = context;
        this.expenseModelList = expenseModelList;
    }

    @Override
    public ExpenseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.expense_list, parent, false);
        return new ExpenseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ExpenseViewHolder holder, int position) {
        ExpenseModel expenseModel = expenseModelList.get(position);

        int date = expenseModel.getDate();

        // Timestamp To Date Converter
        Date date1 = new Date(date * 1000L);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/LLL/yyyy");
        String formatted = simpleDateFormat.format(date1);

        holder.expenseName.setText("খাতঃ " + expenseModel.getName());
        holder.expenseDate.setText("তারিখঃ " + formatted);
        holder.expenseAmount.setText("টাকার পরিমানঃ " + expenseModel.getAmount());
        holder.expenseDescription.setText("বিবরণঃ " + expenseModel.getDes());

    }

    @Override
    public int getItemCount() {
        return expenseModelList.size();
    }

    public class ExpenseViewHolder extends RecyclerView.ViewHolder {
        TextView expenseName, expenseAmount, expenseDate, expenseDescription;

        public ExpenseViewHolder(@NonNull View itemView) {
            super(itemView);
            expenseName = itemView.findViewById(R.id.expenseName);
            expenseAmount = itemView.findViewById(R.id.expenseAmount);
            expenseDate = itemView.findViewById(R.id.expenseDate);
            expenseDescription = itemView.findViewById(R.id.expenseDescription);

        }
    }
}
