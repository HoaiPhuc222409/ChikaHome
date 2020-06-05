package com.example.chikaapp.fragment;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.chikaapp.R;
import com.example.chikaapp.SharedPreferencesUtils;
import com.example.chikaapp.action.CustomToast;
import com.example.chikaapp.action.RecyclerItemClickListener;
import com.example.chikaapp.adapter.ProductsAdapter;
import com.example.chikaapp.api.ApiRetrofit;
import com.example.chikaapp.api.ProductUtils;
import com.example.chikaapp.model.Products;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressPie;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProductsFragment extends Fragment {

    ProductUtils productUtils;
    ACProgressPie loadingDialog;
    ArrayList<Products> productsArrayList;
    ProductsAdapter productsAdapter;
    RecyclerView rclProducts;
    CommunicationInterface listener;
    String idRoom, type;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof CommunicationInterface) {
            listener = (CommunicationInterface) context;
        } else {
            throw new RuntimeException(context.toString() + "Can phai implement");
        }
    }
    public ProductsFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=LayoutInflater.from(getContext()).inflate(R.layout.fragment_products, container, false);
        initialize(view);
        getDataProducts(SharedPreferencesUtils.loadToken(getContext()));
        Bundle bundle = getArguments();
        idRoom = bundle.getString("idRoom");

        rclProducts.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), rclProducts, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String topic = productsArrayList.get(position).getId();
                String token = SharedPreferencesUtils.loadToken(getContext());
                type = productsArrayList.get(position).getType();
                int max = getNumberButtonOfSwitch(type);
                if (!type.equals("IRX")){
                    getButtonUsed(token, topic, max, idRoom);
                } else {
                    Toast.makeText(getContext(), "IRX", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));

        return view;
    }

    public void initialize(View view){
        productsArrayList = new ArrayList<>();
        productsAdapter = new ProductsAdapter(productsArrayList,getContext());

        rclProducts = view.findViewById(R.id.rclProducts);

        rclProducts.setAdapter(productsAdapter);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(),2);
        rclProducts.setLayoutManager(mLayoutManager);

        loadingDialog = new ACProgressPie.Builder(getContext())
                .ringColor(Color.WHITE)
                .pieColor(Color.WHITE)
                .updateType(ACProgressConstant.PIE_AUTO_UPDATE)
                .build();
        loadingDialog.setCancelable(true);

    }

    public void getButtonUsed(String token, String topic, int max, String idRoom){
        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiRetrofit.URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        productUtils = retrofit.create(ProductUtils.class);

        Call<ArrayList> call = productUtils.getUsedButton(token, topic);
        call.enqueue(new Callback<ArrayList>() {
            @Override
            public void onResponse(Call<ArrayList> call, Response<ArrayList> response) {
                ArrayList array;
                array = response.body();
                if (array.size()==max){
                    CustomToast.makeText(getContext(),"All buttons are already in use!!",CustomToast.LENGTH_LONG,CustomToast.WARNING,false).show();
                } else {
                    listener.ProductsToButtonNotUsed(array, max, idRoom, topic, type);
                }
            }
            @Override
            public void onFailure(Call<ArrayList> call, Throwable t) {
                Toast.makeText(getContext(), "Can't load data, please try again!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public int getNumberButtonOfSwitch(String type){
        int max=0;
        if (type.equals("SW2")||type.equals("SR2")){
            max=2;
        } else if (type.equals("SW3")||type.equals("SR3")){
            max=3;
        }
        return max;
    }

    public void getDataProducts(String token) {
        loadingDialog.show();
        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiRetrofit.URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        productUtils = retrofit.create(ProductUtils.class);

        Call<ArrayList<Products>> call = productUtils.getProducts(token);
        call.enqueue(new Callback<ArrayList<Products>>() {
            @Override
            public void onResponse(Call<ArrayList<Products>> call, Response<ArrayList<Products>> response) {
                ArrayList<Products> products=new ArrayList<>();
                products = response.body();
                if (products!=null){
                    loadingDialog.dismiss();
                    productsArrayList=products;
                    productsAdapter.updateList(products);
                    productsAdapter.notifyDataSetChanged();
                }else {
                    loadingDialog.dismiss();
                    CustomToast.makeText(getContext(),"Fail",CustomToast.LENGTH_LONG,CustomToast.CONFUSING,false).show();
                }
            }
            @Override
            public void onFailure(Call<ArrayList<Products>> call, Throwable t) {
                loadingDialog.dismiss();
                CustomToast.makeText(getContext(),"Failure",CustomToast.LENGTH_LONG,CustomToast.WARNING,false).show();
            }
        });
    }

}
