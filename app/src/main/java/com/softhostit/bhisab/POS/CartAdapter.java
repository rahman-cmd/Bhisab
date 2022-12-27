package com.softhostit.bhisab.POS;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.softhostit.bhisab.R;
import com.softhostit.bhisab.database.DatabaseHelper;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    private Context context;
    private ArrayList<CartModel> cartModelArrayList;
    DatabaseHelper cartDB;

    public CartAdapter(Context context, ArrayList<CartModel> cartModelArrayList) {
        this.context = context;
        this.cartModelArrayList = cartModelArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_product_items, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CartModel cartModel = cartModelArrayList.get(position);

        // delete data from database
        holder.img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cartDB = new DatabaseHelper(context);
                cartDB.deleteData(cartModel.getId());
                cartModelArrayList.remove(position);
                notifyDataSetChanged();
            }
        });

        holder.txt_item_name.setText(cartModel.getName());
        holder.txt_price.setText(cartModel.getSell_price());

    }

    @Override
    public int getItemCount() {
        return cartModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txt_item_name, txt_price, txt_minus;
        ImageView img_delete;

        public ViewHolder(View itemView) {
            super(itemView);

            txt_item_name = itemView.findViewById(R.id.txt_item_name);
            txt_price = itemView.findViewById(R.id.txt_price);
            txt_minus = itemView.findViewById(R.id.txt_minus);
            img_delete = itemView.findViewById(R.id.img_delete);


        }
    }

}
