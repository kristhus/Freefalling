package com.pa_gruppe11.freefalling;

import android.graphics.Canvas;

/**
 * Created by Kristian on 31/03/2017.
 */
public interface Drawable {

    /**
     *
     * @param x the designated x-position to draw the model
     * @paramx the designated y-position to draw the model
     * @param id The resource-id of the image to be painted, as located in imagefolder. Given on the form id=R.mipmap.name
     */
    public void Drawable(int x, int y, int id);
    /*{
        this.id = id;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
*/
    public void draw(Canvas canvas);
    //    canvas.drawBitmap(ResourceLoader.getInstance().getImageList().get(id), x, y);
    //}
   // }


}
