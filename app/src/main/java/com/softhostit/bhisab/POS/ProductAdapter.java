package com.softhostit.bhisab.POS;


import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.softhostit.bhisab.R;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder>{

    private Context context;
    private ArrayList<ProductModel> productModelArrayList;

    public ProductAdapter(Context context, ArrayList<ProductModel> productModelArrayList) {
        this.context = context;
        this.productModelArrayList = productModelArrayList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.product_list_item, parent, false);
        return new ProductViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        ProductModel productModel = productModelArrayList.get(position);

        holder.productName.setText("Product Name: "+productModel.getName());
        holder.productSellPrice.setText("Sell Price: "+productModel.getSell_price());
        holder.productBuyPrice.setText("Buy Price: "+productModel.getBuy_price());
        holder.productStock.setText("Stock: "+productModel.getOpenstock());

        // stock will be shown in red color if stock is less than 10
        if (Integer.parseInt(productModel.getOpenstock()) < 10) {
            holder.productStock.setTextColor(context.getResources().getColor(R.color.red));
        }

        // set image from server with glide
        Glide.with(context).load(productModel.getImages()).into(holder.img_product);

        holder.addCart.setOnClickListener(v -> {
            //add to cart
            Toasty.success(context, "Added to cart " +position, Toasty.LENGTH_SHORT).show();
        });


    }

    @Override
    public int getItemCount() {
        return productModelArrayList.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView productName, productSellPrice, productBuyPrice, productStock;
        ImageView img_product, addCart;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);

            productName = itemView.findViewById(R.id.productName);
            productSellPrice = itemView.findViewById(R.id.productSellPrice);
            productBuyPrice = itemView.findViewById(R.id.productBuyPrice);
            productStock = itemView.findViewById(R.id.productStock);

            img_product = itemView.findViewById(R.id.img_product);
            addCart = itemView.findViewById(R.id.addCart);


        }
    }
}
