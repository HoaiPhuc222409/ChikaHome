package com.example.chikaapp.fragment;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chikaapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddIRDeviceFragment extends Fragment {


    public AddIRDeviceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_ir_device, container, false);
    }

}
