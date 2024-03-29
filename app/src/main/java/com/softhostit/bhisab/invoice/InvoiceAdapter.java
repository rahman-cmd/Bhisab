package com.softhostit.bhisab.invoice;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.softhostit.bhisab.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class InvoiceAdapter extends RecyclerView.Adapter<InvoiceAdapter.ViewHolder> {

    private Context context;
    private ArrayList<InvoiceModel> invoiceModelArrayList;
    private ArrayList<ClientDetailsModel> clientDetailsModelArrayList;

    public InvoiceAdapter(Context context, ArrayList<InvoiceModel> invoiceModelArrayList, ArrayList<ClientDetailsModel> clientDetailsModelArrayList) {
        this.context = context;
        this.invoiceModelArrayList = invoiceModelArrayList;
        this.clientDetailsModelArrayList = clientDetailsModelArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.invoice_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        InvoiceModel invoiceModel = invoiceModelArrayList.get(position);

        String currency = invoiceModel.getCurrency();
        String type = invoiceModel.getType();
        int invoice_id_custom = invoiceModel.getInvoice_id_custom();
        int invoice_id = invoiceModel.getInvoice_id();
        String date_issue = invoiceModel.getDate_issue();
        int client_id = invoiceModel.getClient_id();
        int discount = invoiceModel.getDiscount();
        String discount_type = invoiceModel.getDiscount_type();
        int vat = invoiceModel.getVat();
        String vat_type = invoiceModel.getVat_type();
        int total = invoiceModel.getTotal();
        int total_payment = invoiceModel.getTotal_payment();
        int due = invoiceModel.getDue();
        int due_collect_date = invoiceModel.getDue_collect_date();


        String name = clientDetailsModelArrayList.get(position).getName();
        String cname = clientDetailsModelArrayList.get(position).getCname();
        String phone1 = clientDetailsModelArrayList.get(position).getPhone1();
        int pre_due = clientDetailsModelArrayList.get(position).getPre_due();
        String address = clientDetailsModelArrayList.get(position).getAddress();


        // Timestamp To Date Converter
        Date date1 = new Date(Long.parseLong(date_issue) * 1000L);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/LLL/yyyy");
        String formatted = simpleDateFormat.format(date1);


        holder.invoiceID.setText((invoice_id + ""));
        holder.date_issue.setText(formatted);
        holder.name.setText(name);
        holder.pre_due.setText("$" + pre_due);
        holder.total_payment.setText("$" + total_payment);
        holder.mobileNumber.setText(" "+phone1);

        holder.cardViewInvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, InvoiceDetailsActivity.class);
                intent.putExtra("invoice_id", invoice_id);
                intent.putExtra("invoice_id_custom", invoice_id_custom);
                intent.putExtra("formatted", formatted);
                intent.putExtra("client_id", client_id);
                intent.putExtra("discount", discount);
                intent.putExtra("discount_type", discount_type);
                intent.putExtra("vat", vat);
                intent.putExtra("vat_type", vat_type);
                intent.putExtra("total", total);
                intent.putExtra("total_payment", total_payment);
                intent.putExtra("due", due);
                intent.putExtra("due_collect_date", due_collect_date);
                intent.putExtra("name", name);
                intent.putExtra("cname", cname);
                intent.putExtra("phone1", phone1);
                intent.putExtra("pre_due", pre_due);
                intent.putExtra("address", address);
                intent.putExtra("currency", currency);
                intent.putExtra("type", type);
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return invoiceModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView invoiceID, date_issue, name, pre_due, total_payment, mobileNumber;
        CardView cardViewInvoice;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            invoiceID = itemView.findViewById(R.id.invoiceID);
            date_issue = itemView.findViewById(R.id.date_issue);
            name = itemView.findViewById(R.id.name);
            pre_due = itemView.findViewById(R.id.pre_due);
            total_payment = itemView.findViewById(R.id.total_payment);
            mobileNumber = itemView.findViewById(R.id.mobileNumber);

            cardViewInvoice = itemView.findViewById(R.id.cardViewInvoice);

        }
    }
}
