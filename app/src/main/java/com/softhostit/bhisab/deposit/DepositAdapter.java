package com.softhostit.bhisab.deposit;


import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.softhostit.bhisab.R;

import java.util.List;


public class DepositAdapter extends RecyclerView.Adapter<DepositAdapter.ViewHolder> {
    private Context context;
    List<DepositModel> depositModelList;

    public DepositAdapter(Context context, List<DepositModel> depositModelList) {
        this.context = context;
        this.depositModelList = depositModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.deposit_cat_item, parent, false);
        return new ViewHolder(view);
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DepositModel depositModel = depositModelList.get(position);

        holder.cat_tv.setText("" + depositModel.getId());
        holder.categoryName.setText("" + depositModel.getName());

    }

    @Override
    public int getItemCount() {
        return depositModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView cat_tv, categoryName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cat_tv = itemView.findViewById(R.id.cat_tv);
            categoryName = itemView.findViewById(R.id.categoryName);

        }
    }
}
