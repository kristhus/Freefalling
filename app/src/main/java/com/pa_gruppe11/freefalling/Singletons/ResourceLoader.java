package com.pa_gruppe11.freefalling.Singletons;

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Log;

import com.pa_gruppe11.freefalling.Drawable;
import com.pa_gruppe11.freefalling.R;
import com.pa_gruppe11.freefalling.framework.AudioFreefalling;
import com.pa_gruppe11.freefalling.framework.ImageItem;
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

    private static final int[] resourceIdList = {// Add new resources here, or load all images from drawable
            R.drawable.bg_sky,
            R.drawable.stickman,
            R.drawable.block,
            R.drawable.knife,
            R.drawable.sawblade,
            R.drawable.roger,
            R.drawable.goat,
            R.drawable.grubermann

    };

    private static final int[] sfxIdList = {    // list of audio files, considered as sfx
            R.raw.goat
    };
    private static final int[] bgmIdList = {    // list of audio files, considered as bgm

    };

    private static final int[] preGameResources = {
            R.drawable.hanz,
            R.drawable.gruber,
            R.drawable.goat
    };


    private static final ResourceLoader INSTANCE =  new ResourceLoader();

    private HashMap<Integer, Bitmap> imageList = new HashMap<Integer, Bitmap>();
    private ArrayList<ImageItem> data;

    private HashMap<Integer, AudioFreefalling> audioList = new HashMap<>();


    private GameMenu context;

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
       // bm.recycle();   // This should possibly done somewhere else or in some other way. Gives Warning undefined behavior when call getWidth.
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
     * This method loads the various resources from a pre defined list into its designated HashMap
     * The created object can then be easily accessed via the given resourceId
     * @param context
     */
    public void manualLoad(GameMenu context) {
        if(resourceIdList.length > 0)
            for(int i : resourceIdList)
                imageList.put(i, BitmapFactory.decodeResource(context.getResources(), i));
        if(sfxIdList.length > 0)
            for(int j : sfxIdList)
                audioList.put(j, new AudioFreefalling(context, j, AudioFreefalling.SFX));
        if(bgmIdList.length > 0)
            for(int k : bgmIdList)
                audioList.put(k, new AudioFreefalling(context, k, AudioFreefalling.BGM));
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
        recycleGameResources();
        recycleMenuResources();
    }

    /**
     * Remove resources connected to the game
     */
    public void recycleGameResources() {
        for (int i : imageList.keySet())
            imageList.get(i).recycle();
        for (int j : audioList.keySet())
            audioList.get(j).dispose();
    }

    /**
     * Remove resources connected to the menus
     */
    public void recycleMenuResources() {
        if(data != null) {
            for (int i = 0; i < data.size(); i++) {
                data.get(i).recycle();  // baade
                data.remove(i);         // i pose
            }
        }
    }

    public static ResourceLoader getInstance(){

        return INSTANCE;
    }

    public HashMap<Integer, AudioFreefalling> getAudioList() {
        return audioList;
    }

    public HashMap<Integer, Bitmap> getImageList(){
        return imageList;
    }

    public void setContext(GameMenu context) {
        this.context = context;
    }


    public ArrayList<ImageItem> getCharacters() {
        data = new ArrayList<ImageItem>();
        TypedArray imageIds = context.getResources().obtainTypedArray(R.array.menu_resource_ids);

        for(int i = 0; i < imageIds.length(); i++) {
           // String resourceName = imageIds.getString(i);
            int id = imageIds.getResourceId(i, -1);
            String resourceName = context.getResources().getResourceEntryName(id);
            Log.w("ResourceLoader", "resourceName: " + resourceName);
            data.add(new ImageItem(
                            BitmapFactory.decodeResource(
                                    context.getResources(), imageIds.getResourceId(i, -1))
                            , resourceName
                            , id));
        }
        return data;
    }


    /**
     * Reload the volume values of the audio files.
     * Call when a Settings-related change to DataHandler has been made
     */
    public void reloadAudioVolume() {
        for(int i : audioList.keySet()){
            AudioFreefalling audio = audioList.get(i);
            if(audio.getAudioType() == AudioFreefalling.BGM)
                audio.mute(DataHandler.getInstance().isBgmMuted());
            else if(audio.getAudioType() == AudioFreefalling.SFX)
                audio.mute(DataHandler.getInstance().isSfxMuted());
        }
    }
}
