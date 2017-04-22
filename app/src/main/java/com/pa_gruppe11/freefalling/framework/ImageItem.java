package com.pa_gruppe11.freefalling.framework;

import android.graphics.Bitmap;

/**
 * Created by Kristian on 08/04/2017.
 */

public class ImageItem {

    private Bitmap image;
    private String title;
    private int identifier;


    public ImageItem(Bitmap image, String title, int identifier) {
        this.image = image;
        this.title = title;
        this.identifier = identifier;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void recycle() {
        image.recycle();
    }

    public int getIdentifier() {
        return identifier;
    }
    public void setIdentifier(int identifier) {
        this.identifier = identifier;
    }

}
