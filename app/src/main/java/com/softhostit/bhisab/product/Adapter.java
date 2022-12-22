package com.softhostit.bhisab.product;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.softhostit.bhisab.POS.ProductAdapter;
import com.softhostit.bhisab.R;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    private Context context;
    List<Model> modelList;

    public Adapter(Context context, List<Model> modelList) {
        this.context = context;
        this.modelList = modelList;
    }

    @NonNull
    @Override
    public Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.product_list_item, parent, false);
        return new Adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter.ViewHolder holder, int position) {
        Model model = modelList.get(position);

        holder.productName.setText("Name: " + model.getName());
        holder.productSellPrice.setText("Price: " + model.getSell_price());
        holder.productBuyPrice.setText("Buy Price: " + model.getBuy_price());
        holder.productStock.setText("Stock: " + model.getOpenstock());


        String image = model.getImages();
        String domain = model.getDomain();
        String image_url = "https://" + domain + "/php/product/" + image;


        // set image from server with glide
        Glide.with(context)
                .load(image_url)
                .placeholder(R.drawable.image_placeholder)
                .into(holder.img_product);

        // show defult image if image is not available
        if (image_url.equals("null")) {
            holder.img_product.setImageResource(R.drawable.image_placeholder);
        }

        holder.addCart.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView productName, productSellPrice, productBuyPrice, productStock, addCart;
        ImageView img_product;


        public ViewHolder(@NonNull View itemView) {
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
