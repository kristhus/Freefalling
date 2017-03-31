package com.pa_gruppe11.freefalling.Singletons;

import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageView;

import com.pa_gruppe11.freefalling.R;

import java.lang.reflect.Field;
import java.util.HashMap;

/**
 * Created by skars on 31.03.2017.
 */

public final class ResourceLoader {

    private static final ResourceLoader INSTANCE =  new ResourceLoader();

    private HashMap<Integer, ImageView> imageList;

    //private LoadingScreen view;


    public static void loadDrawables(Class<?> clz){
        final Field[] fields = clz.getDeclaredFields();
        for (Field field : fields) {
            final int drawableId;
            try {
                drawableId = field.getInt(clz);
            } catch (Exception e) {
                continue;
            }
        /* make use of drawableId for accessing Drawables here */

        }
    }

    public void setImageList(Class<?> clz){

    }



    public ImageView loadImage(int id){

        return imageList.get(id);
    }

    public void recycleAll(){

        for (int i : imageList.keySet()){
            ((BitmapDrawable)imageList.get(i).getDrawable()).getBitmap().recycle();     // Removes element for element in the hashmap.
        }

    }

    public static ResourceLoader getInstance(){

        return INSTANCE;
    }

    public HashMap getImageList(){
        return imageList;
    }

}
