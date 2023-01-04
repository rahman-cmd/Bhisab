package com.softhostit.bhisab.invoice;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.softhostit.bhisab.R;

import java.util.ArrayList;


public class InvoiceAdapter extends RecyclerView.Adapter<InvoiceAdapter.ViewHolder>{

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



        holder.allTV.setText("Name: " + name + "\nCname: " + cname + "\nPhone1: " + phone1 + "\nPre_due: " + pre_due + "\nAddress: " + address +"\n\n");


    }

    @Override
    public int getItemCount() {
        return invoiceModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView allTV;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            allTV = itemView.findViewById(R.id.allTV);

        }
    }
}
