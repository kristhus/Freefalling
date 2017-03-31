package com.pa_gruppe11.freefalling.Singletons;

import android.widget.ImageView;

import java.util.HashMap;

/**
 * Created by skars on 31.03.2017.
 */

public final class ResourceLoader {

    private static final ResourceLoader INSTANCE =  new ResourceLoader();

    private HashMap<Integer, ImageView> imageList;

    private LoadingScreen view;

    public ImageView loadImage(int id){

        return imageList.get(id);
    }

    public void recycleAll(){
        
    }

    public static ResourceLoader getInstance(){

        return INSTANCE;
    }

    public HashMap getImageList(){
        return imageList;
    }

}
