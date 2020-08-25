package com.example.chikaapp.fragment;


import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.chikaapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddIRDeviceFragment extends Fragment implements View.OnClickListener {

    ImageView imgAirConditioner, imgTelevision, imgPickAirCond, imgPickTv;
    Button btnDone;


    public AddIRDeviceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_add_ir_device, container, false);
        initialize(view);
        return view;
    }

    public void initialize(View view) {
        imgAirConditioner = view.findViewById(R.id.img_air_cond);
        imgTelevision = view.findViewById(R.id.img_tv);
        imgPickAirCond = view.findViewById(R.id.img_tick);
        imgPickTv = view.findViewById(R.id.img_tick1);
        btnDone = view.findViewById(R.id.btn_Done);

        imgAirConditioner.setOnClickListener(this);
        imgTelevision.setOnClickListener(this);
        btnDone.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_air_cond:
                if (imgPickAirCond.getVisibility() == View.GONE) {
                    if (imgPickTv.getVisibility() == View.VISIBLE) {
                        imgPickTv.setVisibility(View.GONE);
                    }
                    imgPickAirCond.setVisibility(View.VISIBLE);
                } else {
                    imgPickAirCond.setVisibility(View.GONE);
                }
                break;
            case R.id.img_tv:
                if (imgPickTv.getVisibility() == View.GONE) {
                    if (imgPickAirCond.getVisibility() == View.VISIBLE) {
                        imgPickAirCond.setVisibility(View.GONE);
                    }
                    imgPickTv.setVisibility(View.VISIBLE);
                }
            case R.id.btn_Done:

                break;
            default:
                break;
        }
    }
}
