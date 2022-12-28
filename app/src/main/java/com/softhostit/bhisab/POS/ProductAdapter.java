package com.softhostit.bhisab.POS;


import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.softhostit.bhisab.R;
import com.softhostit.bhisab.database.DatabaseHelper;

import java.util.List;

import es.dmoral.toasty.Toasty;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private Context context;
    List<ProductModel> productModelList;
    public static int count;

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


        String image = productModel.getImages();
        String domain = productModel.getDomain();
        String image_url = "https://" + domain + "/php/product/" + image;

        // stock will be shown in red color if stock is less than 1


        if ((productModel.getOpenstock()) > 5) {
            holder.productStock.setVisibility(View.VISIBLE);
            holder.productStock.setText(context.getString(R.string.in_stock) + " : " + productModel.getOpenstock());
        } else if ((productModel.getOpenstock()) == 0) {
            holder.productStock.setTextColor(Color.RED);
            holder.productStock.setVisibility(View.VISIBLE);
            holder.productStock.setText(context.getString(R.string.not_available) + " : " + productModel.getOpenstock());
        } else {
            holder.productStock.setTextColor(Color.RED);
            holder.productStock.setVisibility(View.VISIBLE);
            holder.productStock.setText(context.getString(R.string.low_stock) + " : " + productModel.getOpenstock());
        }


        // set image from server with glide
        Glide.with(context)
                .load(image_url)
                .placeholder(R.drawable.image_placeholder)
                .into(holder.img_product);

        // show defult image if image is not available
        if (image_url.equals("null")) {
            holder.img_product.setImageResource(R.drawable.image_placeholder);
        }


        int id = (productModel.getId());
        String name = productModel.getName();
        int sell_price = (productModel.getSell_price());
        int buy_price = (productModel.getBuy_price());
        int openstock = productModel.getOpenstock();
        String images = productModel.getImages();
        String barcode = productModel.getBarcode();

        DatabaseHelper product = new DatabaseHelper(context.getApplicationContext());
        Cursor res = product.getAllData();
//        PosActivity.txtCount.setText("" + res.getCount());


        holder.addCart.setOnClickListener(v -> {
            // click and increase count 1++
            DatabaseHelper productDB = new DatabaseHelper(context.getApplicationContext());
            PosActivity.txtCount.setText("" + productDB.getAllData().getCount());

            if (productModel.getOpenstock() <=0){
                Toasty.warning(context, R.string.stock_not_available_please_update_stock, Toast.LENGTH_SHORT).show();
            } else {
                int isInserted =  productDB.insertData(id, name, sell_price, buy_price, openstock, images, barcode, domain);

                if (isInserted > 0) {
                    PosActivity.txtCount.setVisibility(View.VISIBLE);
                }
            }

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
