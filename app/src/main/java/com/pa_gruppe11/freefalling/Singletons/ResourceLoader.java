package com.pa_gruppe11.freefalling.Singletons;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.pa_gruppe11.freefalling.gameControllers.GameMenu;

import java.lang.reflect.Field;
import java.util.HashMap;

/**
 * Created by skars on 31.03.2017.
 */

public final class ResourceLoader {

    private static final ResourceLoader INSTANCE =  new ResourceLoader();

    private HashMap<Integer, Bitmap> imageList;

    private GameMenu context;

    //private LoadingScreen view;

    public void loadDrawables(Class<?> clz){
        final Field[] fields = clz.getDeclaredFields();
        for (Field field : fields) {
            final int drawableId;
            try {
                drawableId = field.getInt(clz);
                imageList.put(drawableId, BitmapFactory.decodeResource(context.getResources(), drawableId));
            } catch (Exception e) { //
                continue;
            }
        /* make use of drawableId for accessing Drawables here */

        }
    }

    public void setImageList(Class<?> clz){

    }

    public Bitmap getImage(int id) {
        return imageList.get(id);
    }

    public Bitmap loadImage(int id){

        return imageList.get(id);
    }

    public void recycleAll(){

        for (int i : imageList.keySet()){
            imageList.get(i).recycle();
 //           ((BitmapDrawable)imageList.get(i).getDrawable()).getBitmap().recycle();     // Removes element for element in the hashmap.
        }

    }

    public static ResourceLoader getInstance(){

        return INSTANCE;
    }

    public HashMap<Integer, Bitmap> getImageList(){
        return imageList;
    }

    public void setContext(GameMenu context) {
        this.context = context;
    }




}
