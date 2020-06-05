package com.example.chikaapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chikaapp.R;
import com.example.chikaapp.model.Devices;
import com.example.chikaapp.model.Products;

import java.util.ArrayList;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ViewHolder> {

    private ArrayList<Products> productsArrayList;
    public int imgId;
    Context context;

    public ProductsAdapter(ArrayList<Products> productsArrayList, Context context) {
        this.productsArrayList = productsArrayList;
        this.context = context;
    }

    public void updateList(ArrayList<Products> productsArrayList) {
        this.productsArrayList = productsArrayList;
    }




    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
        ProductsAdapter.ViewHolder viewHolder = new ProductsAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Products products = productsArrayList.get(position);

        imgId = getProductsImage(products.getType());

        holder.tv_Products_name.setText(products.getName());
        holder.img_Products.setImageResource(imgId);

    }

    @Override
    public int getItemCount() {
        return productsArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_Products;
        TextView tv_Products_name;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_Products = itemView.findViewById(R.id.img_products);
            tv_Products_name =itemView.findViewById(R.id.tv_products_name);
        }
    }
    private int getProductsImage(String type) {
        switch (type) {
            case "SW2": {
                imgId = R.drawable.ca_sw;
                break;
            }
            case "SW3": {
                imgId = R.drawable.sw2;
                break;
            }
            case "SR2":{
                imgId = R.drawable.sr;
                break;
            }
            case "SR3":{
                imgId = R.drawable.sr;
                break;
            }
            case "IRX":{
                imgId = R.drawable.irx;
                break;
            }
            case "SS01":{
                imgId = R.drawable.ca_ss01;
                break;
            }
            case "SS02":{
                imgId = R.drawable.ca_ss02;
                break;
            }
            case "SS03":{
                imgId = R.drawable.ca_ss03;
                break;
            }
            default:
                imgId = R.drawable.ca_hc;
                break;
        }
        return imgId;
    }
}
