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

public class OrderDetailsAdapter extends RecyclerView.Adapter<OrderDetailsAdapter.ViewHolder> {

    private Context context;
    private ArrayList<OrderItemModel> orderItemModelArrayList;

    public OrderDetailsAdapter(Context context, ArrayList<OrderItemModel> orderItemModelArrayList) {
        this.context = context;
        this.orderItemModelArrayList = orderItemModelArrayList;
    }

    @NonNull
    @Override
    public OrderDetailsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.order_details_item, parent, false);
        return new OrderDetailsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderDetailsAdapter.ViewHolder holder, int position) {

//        // Timestamp To Date Converter
//        Date date1 = new Date(Long.parseLong(date_issue) * 1000L);
//        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/LLL/yyyy");
//        String formatted = simpleDateFormat.format(date1);
        OrderItemModel orderItemModel = orderItemModelArrayList.get(position);
        holder.txt_product_name.setText(orderItemModel.getName());
        holder.txt_qty.setText("Qty: " + orderItemModel.getQuantity());
        holder.txt_unit.setText("Unite: " + orderItemModel.getUnit());
        holder.txt_price.setText("Price " + orderItemModel.getPrice());

        // calculate total price of each item and quantity
        int price = orderItemModel.getPrice();
        int quantity = orderItemModel.getQuantity();

        int total = price * quantity;

        holder.txt_total_cost.setText("Total: " + total);


    }

    @Override
    public int getItemCount() {
        return orderItemModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_product_name, txt_qty, txt_unit, txt_price, txt_total_cost, txt_total_due;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_product_name = itemView.findViewById(R.id.txt_product_name);
            txt_qty = itemView.findViewById(R.id.txt_qty);
            txt_unit = itemView.findViewById(R.id.txt_unit);
            txt_price = itemView.findViewById(R.id.txt_price);
            txt_total_cost = itemView.findViewById(R.id.txt_total_cost);
            txt_total_due = itemView.findViewById(R.id.txt_total_due);


        }
    }

}
