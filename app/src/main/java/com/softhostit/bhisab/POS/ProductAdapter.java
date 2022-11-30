package com.softhostit.bhisab.POS;


import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.softhostit.bhisab.Constant;
import com.softhostit.bhisab.Login.SharedPrefManager;
import com.softhostit.bhisab.R;

import java.util.List;

import es.dmoral.toasty.Toasty;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private Context context;
    List<ProductModel> productModelList;

    public ProductAdapter(Context context, List<ProductModel> productModelList) {
        this.context = context;
        this.productModelList = productModelList;
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
        ProductModel productModel = productModelList.get(position);

        holder.productName.setText("" + productModel.getName());
        holder.productSellPrice.setText("" + productModel.getSell_price());
        holder.productBuyPrice.setText("" + productModel.getBuy_price());
        holder.productStock.setText("Stock: " + productModel.getOpenstock());


        String image = productModel.getImages();
        String domain = productModel.getDomain();
        String image_url = "https://"+ domain+"/php/product/" + image;

        // stock will be shown in red color if stock is less than 1
//        if ((productModel.getOpenstock()) < 10) {
//            holder.productStock.setTextColor(context.getResources().getColor(R.color.red));
//        }

        // set image from server with glide
        Glide.with(context)
                .load(image_url)
                .placeholder(R.drawable.image_placeholder)
                .into(holder.img_product);

        // show defult image if image is not available
        if (image_url.equals("null")) {
            holder.img_product.setImageResource(R.drawable.image_placeholder);
        }


        holder.addCart.setOnClickListener(v -> {
            //add to cart
            Toasty.success(context, "Added to cart", Toasty.LENGTH_SHORT).show();
        });


    }

    @Override
    public int getItemCount() {
        return productModelList.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView productName, productSellPrice, productBuyPrice, productStock, addCart;
        ImageView img_product;


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
