package com.pa_gruppe11.freefalling.Singletons;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Log;

import com.pa_gruppe11.freefalling.Drawable;
import com.pa_gruppe11.freefalling.R;
import com.pa_gruppe11.freefalling.gameControllers.GameMenu;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by skars on 31.03.2017.
 */

public final class ResourceLoader {

    private static final int[] resourceIdList = {
            R.drawable.bg_sky, R.drawable.stickman, R.drawable.block};                     // Add new resources here, or load all images from drawable




    private static final ResourceLoader INSTANCE =  new ResourceLoader();

    private HashMap<Integer, Bitmap> imageList = new HashMap<Integer, Bitmap>();

    private HashMap<Integer, Drawable> drawableList = new HashMap<>();

    private GameMenu context;

    //private LoadingScreen view;


    private ResourceLoader() {
//        loadDrawables(R.drawable.class);

        //TODO: test, remove
//        imageList.put(R.drawable.bg_sky, BitmapFactory.decodeResource(context.getResources(), R.drawable.bg_sky));
    }

    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }


    /**
     * NOTE: This method correctly loads all drawables. But all drawables is A LOT of images.
     * Use only if #PcMasterRace_64GBram
     * @param context
     */
    public void loadGameResources(GameMenu context){
        // android.content.res.Resources
        final Class<R.drawable> c = R.drawable.class;
        final Field[] fields = c.getFields();
        for (Field field : fields) {
            final int drawableId;
            try {
                drawableId = field.getInt(c);
                //Log.w("ResourceLoader", "field " + drawableId);
                if(drawableId > 0)
                    imageList.put(drawableId, BitmapFactory.decodeResource(context.getResources(), drawableId));
            } catch (Exception e) { //
                e.printStackTrace();
            }
        }

        for(int i : imageList.keySet()) {
            //Log.w("ResourceLoader", "Name: " + context.getResources().getResourceEntryName(i));
        }

    }


    /**
     * Add id's to resourceIdList in ResourceLoader for each new resource to be loaded.
     * @param context
     */
    public void manualLoad(GameMenu context) {
        for(int i : resourceIdList)
            imageList.put(i, BitmapFactory.decodeResource(context.getResources(), i));
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
