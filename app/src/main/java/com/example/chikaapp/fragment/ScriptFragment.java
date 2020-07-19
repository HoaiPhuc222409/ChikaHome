package com.example.chikaapp.fragment;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chikaapp.R;
import com.example.chikaapp.SharedPreferencesUtils;
import com.example.chikaapp.action.CustomToast;
import com.example.chikaapp.action.RecyclerItemClickListener;
import com.example.chikaapp.adapter.DevicesAdapter;
import com.example.chikaapp.adapter.ScriptsAdapter;
import com.example.chikaapp.api.ApiRetrofit;
import com.example.chikaapp.api.DeviceUtils;
import com.example.chikaapp.api.ScriptUtils;
import com.example.chikaapp.model.Devices;
import com.example.chikaapp.model.Scripts;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScriptFragment extends Fragment {

    RecyclerView rclScripts;
    ScriptsAdapter scriptsAdapter;
    ArrayList<Scripts> scriptsArrayList;
    Context context;
    ScriptUtils scriptUtils;
    CommunicationInterface listener;

    public ScriptFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof CommunicationInterface) {
            listener = (CommunicationInterface) context;
        } else {
            throw new RuntimeException(context.toString() + "Can phai implement");
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.fragment_script, container, false);
        initialize(view);
        getDataScripts(SharedPreferencesUtils.loadToken(getContext()));

        rclScripts.addOnItemTouchListener(
                new RecyclerItemClickListener(context, rclScripts, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        if (scriptsArrayList.get(position).getId().equals("create")){
                            Toast.makeText(getContext(), "Tao Scripts", Toast.LENGTH_SHORT).show();
                        } else {
                            listener.ScriptToDetailScripts(scriptsArrayList.get(position));
                        }
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }
                }));
        return view;
    }

    private void initialize(View view) {
        rclScripts = view.findViewById(R.id.rclScript);

        scriptsArrayList = new ArrayList<>();
        scriptsAdapter = new ScriptsAdapter(new ArrayList<Scripts>(), getContext());
        rclScripts.setAdapter(scriptsAdapter);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 2);
        rclScripts.setLayoutManager(mLayoutManager);
    }

    public void getDataScripts(String token) {
        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiRetrofit.URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        scriptUtils = retrofit.create(ScriptUtils.class);

        Call<ArrayList<Scripts>> call = scriptUtils.getScript(token);
        call.enqueue(new Callback<ArrayList<Scripts>>() {
            @Override
            public void onResponse(Call<ArrayList<Scripts>> call, Response<ArrayList<Scripts>> response) {
                ArrayList<Scripts> scripts = new ArrayList<>();
                scripts = response.body();
                if (scripts != null) {

                    scripts.add(new Scripts("create", "create", "New Script", null,null,null,null));
                    scriptsAdapter.updateList(scripts);
                    scriptsArrayList = scripts;
                    scriptsAdapter.notifyDataSetChanged();
                } else {
                    CustomToast.makeText(getContext(), "Fail", CustomToast.LENGTH_LONG, CustomToast.CONFUSING, false).show();
                }

            }

            @Override
            public void onFailure(Call<ArrayList<Scripts>> call, Throwable t) {
                CustomToast.makeText(getContext(), "Failure", CustomToast.LENGTH_LONG, CustomToast.WARNING, false).show();
            }
        });
    }

}
