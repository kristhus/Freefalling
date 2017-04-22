package com.pa_gruppe11.freefalling.framework;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pa_gruppe11.freefalling.R;

import java.util.ArrayList;

/**
 * Created by Kristian on 08/04/2017.
 * Followed tutorial by Nilanchala Panigrahy
 */

public class GridViewAdapter extends ArrayAdapter {

    private Context context;
    private int resource;
    private ArrayList<ImageItem> data = new ArrayList<ImageItem>();

    public GridViewAdapter(@NonNull Context context, @LayoutRes int resource,  @NonNull ArrayList<ImageItem> data) {
        super(context, resource, data);
        this.resource = resource;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        TitledImage titledImage = null;

        if(row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(resource, parent, false);
            titledImage = new TitledImage();
            titledImage.imageTitle = (TextView) row.findViewById(R.id.text);
            titledImage.image = (ImageView) row.findViewById(R.id.image);
            row.setTag(titledImage);
        }
        else {
            titledImage = (TitledImage) row.getTag();
        }
        ImageItem item = data.get(position);
        titledImage.imageTitle.setText(item.getTitle());
        titledImage.image.setImageBitmap(item.getImage());
        return row;
    }

    public static class TitledImage {
        public TextView imageTitle;
        public ImageView image;
    }

    public ImageItem getImageItem(int i) {
        return data.get(i);
    }


}
