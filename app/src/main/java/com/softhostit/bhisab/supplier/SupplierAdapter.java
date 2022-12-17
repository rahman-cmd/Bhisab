package com.softhostit.bhisab.supplier;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.softhostit.bhisab.R;

import java.util.List;

public class SupplierAdapter extends RecyclerView.Adapter<SupplierAdapter.ViewHolder> {
    private Context context;
    List<SupplierModel> supplierModelList;

    public SupplierAdapter(Context context, List<SupplierModel> supplierModelList) {
        this.context = context;
        this.supplierModelList = supplierModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.customer_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SupplierModel supplierModel = supplierModelList.get(position);

        int id = (supplierModel.getId());
        String name = supplierModel.getName();
        String phone = supplierModel.getPhone();
        String address = supplierModel.getAddress();
        String cname = supplierModel.getCname();
        String group = supplierModel.getGroup();

        holder.contact_info.setText("" + "সাপ্লাইয়ারঃ " + name + "\nকম্পানিঃ " + cname + "\nফোনঃ " + phone + "\nগ্রুপঃ" + group + "\nঠিকানাঃ " + address);
        holder.contact_info.setPadding(5, 0, 0, 0);

        holder.profile_image.setVisibility(View.GONE);

    }

    @Override
    public int getItemCount() {
        return supplierModelList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView contact_info;
        ImageView profile_image;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profile_image = itemView.findViewById(R.id.profile_image);
            contact_info = itemView.findViewById(R.id.contact_info);
        }
    }
}
