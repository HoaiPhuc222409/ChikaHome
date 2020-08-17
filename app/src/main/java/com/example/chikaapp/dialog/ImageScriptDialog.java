package com.example.chikaapp.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.chikaapp.R;
import com.example.chikaapp.adapter.ImageAdapter;
import com.example.chikaapp.fragment.CommunicationInterface;
import com.example.chikaapp.model.Image;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ImageScriptDialog extends DialogFragment {

    GridView gridView;
    Image image;
    CommunicationInterface listener;
    public static ImageScriptDialog newInStance(List<Image> imageList){
        ImageScriptDialog imageScriptDialog=new ImageScriptDialog();
        Bundle args = new Bundle();
        args.putSerializable("image", (Serializable) imageList);
        imageScriptDialog.setArguments(args);
        return imageScriptDialog;
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_image,container);
        gridView = view.findViewById(R.id.gridImage);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Object o = gridView.getItemAtPosition(i);
                image = (Image) o;
                Fragment fragment=getActivity().getSupportFragmentManager().findFragmentByTag("image_dialog");
                ImageScriptDialog imageScriptDialog = (ImageScriptDialog) fragment;
                listener.DialogImageToAddScript(image);
                imageScriptDialog.dismiss();
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        List<Image> images =(List<Image>) getArguments().getSerializable("image");
        if (!images.isEmpty()){
            gridView.setAdapter(new ImageAdapter(getContext(), images));
        }
    }


}
