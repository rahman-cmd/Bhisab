package com.softhostit.bhisab.coustomer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.softhostit.bhisab.R;

import java.util.List;


public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.ViewHolder> {

    private Context context;
    List<CustomerModel> customerModelList;

    public CustomerAdapter(Context context, List<CustomerModel> customerModelList) {
        this.context = context;
        this.customerModelList = customerModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.customer_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CustomerModel customerModel = customerModelList.get(position);

        String domain = customerModel.getDomain();
        String imageUrl = customerModel.getPhoto();
        String fullUrl = "https://" + domain + "/thumb.php?src=/home/bhisa545b/public_html/main_software/php/contact/../../" + imageUrl;


        // show defult image if image is not available
        if (imageUrl.equals("null")) {
            holder.profile_image.setImageResource(R.drawable.ic_person);
        } else {
            Glide.with(context).load(fullUrl).into(holder.profile_image);
        }

        holder.contact_info.setText("" + customerModel.getFname() + "\n" + customerModel.getCname() + "\n" + customerModel.getPhone1());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Clicked" + fullUrl, Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public int getItemCount() {
        return customerModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView profile_image;
        TextView contact_info;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profile_image = itemView.findViewById(R.id.profile_image);
            contact_info = itemView.findViewById(R.id.contact_info);
            cardView = itemView.findViewById(R.id.cardView);

        }
    }

}
