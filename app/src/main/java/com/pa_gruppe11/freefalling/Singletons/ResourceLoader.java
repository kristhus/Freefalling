package com.pa_gruppe11.freefalling.Singletons;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.pa_gruppe11.freefalling.R;
import com.pa_gruppe11.freefalling.gameControllers.GameMenu;

import java.lang.reflect.Field;
import java.util.HashMap;

/**
 * Created by skars on 31.03.2017.
 */

public final class ResourceLoader {

    private static final ResourceLoader INSTANCE =  new ResourceLoader();

    private HashMap<Integer, Bitmap> imageList = new HashMap<Integer, Bitmap>();

    private GameMenu context;

    //private LoadingScreen view;


    private ResourceLoader() {
//        loadDrawables(R.drawable.class);

        //TODO: test, remove
//        imageList.put(R.drawable.bg_sky, BitmapFactory.decodeResource(context.getResources(), R.drawable.bg_sky));
    }


    public void loadDrawables(Class<?> clz, GameMenu context){
        Log.w("ResourceLoader", "loadDrawables");
        final Field[] fields = clz.getDeclaredFields();
        for (Field field : fields) {
            final int drawableId;
            try {
                drawableId = field.getInt(clz);
                Log.w("ResourceLoader", "field " + drawableId);
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


    public void loadImage(int id, GameMenu context){
        try {
            Log.w("ResourceLoader", "load " + id);
            Bitmap b = BitmapFactory.decodeResource(context.getResources(), id);
            imageList.put(id, BitmapFactory.decodeResource(context.getResources(), id));
        } catch (Exception e) { //
            e.printStackTrace();
            Log.w("ResourceLoader", "Could not load resource with id " + id);
        }
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
