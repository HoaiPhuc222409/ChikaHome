package com.example.chikaapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.chikaapp.R;
import com.example.chikaapp.model.Image;

import java.util.List;

public class ImageAdapter extends BaseAdapter {

    private List<Image> imageList;
    private LayoutInflater layoutInflater;
    private Context context;

    public ImageAdapter(Context aContext,  List<Image> imageList) {
        this.context = aContext;
        this.imageList = imageList;
        layoutInflater = LayoutInflater.from(aContext);
    }

    @Override
    public int getCount() {
        return imageList.size();
    }

    @Override
    public Object getItem(int position) {
        return imageList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_image, null);
            holder = new ViewHolder();
            holder.img_Room = (ImageView) convertView.findViewById(R.id.img_image_room);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Image image = this.imageList.get(position);


        holder.img_Room.setImageResource(image.getUrl_image());

        return convertView;
    }

    static class ViewHolder {
        ImageView img_Room;
    }

}
