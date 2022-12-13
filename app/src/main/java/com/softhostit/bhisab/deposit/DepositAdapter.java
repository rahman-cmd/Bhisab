package com.softhostit.bhisab.deposit;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.softhostit.bhisab.R;

import java.text.SimpleDateFormat;
import java.util.Date;
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
        View view = LayoutInflater.from(context).inflate(R.layout.deposit_item, parent, false);
        return new ViewHolder(view);
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DepositModel depositModel = depositModelList.get(position);

//        কাস্টমার নাম : এস.এম. মনির \nতারিখ : ১২/১১/২০২২\nভা.নং : ৫১৮\nটাকার পরিমানঃ ১৫০০\nবিবরণ : বিক্রয়
        int id = depositModel.getId();
        String account = depositModel.getAccount();
        int date = depositModel.getDate();
        int amount = depositModel.getAmount();
        int user_id = depositModel.getUser_id();
        int payer = depositModel.getPayer();
        String in_cat = depositModel.getIn_cat();
        String des = depositModel.getDes();
        String domain = depositModel.getDomain();
        String fname = depositModel.getFname();

        // 1670349600 = 2022-07-31 00:00:00 strtotime
        // Timestamp To Date Converter
        Date date1 = new Date(date * 1000L);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/LLL/yyyy");
        String formatted = simpleDateFormat.format(date1);


        holder.deposit_info.setText("কাস্টমার নাম : " + fname + "\nতারিখ: "+ formatted + "\nভা.নং : " + id + "\nটাকার পরিমানঃ " + amount + "\nবিবরণ : " + des+ "\nখাত : " + in_cat);
        holder.print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DepositDetailsActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("account", account);
                intent.putExtra("date", date);
                intent.putExtra("amount", amount);
                intent.putExtra("user_id", user_id);
                intent.putExtra("payer", payer);
                intent.putExtra("name", in_cat);
                intent.putExtra("des", des);
                intent.putExtra("domain", domain);
                intent.putExtra("fname", fname);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return depositModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView deposit_info;
        Button print;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            deposit_info = itemView.findViewById(R.id.deposit_info);
            print = itemView.findViewById(R.id.print);

        }
    }
}
