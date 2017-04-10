package com.pa_gruppe11.freefalling.framework;

import android.support.annotation.NonNull;

/**
 * Created by Kristian on 10/04/2017.
 */

public class VectorSAT implements Comparable<VectorSAT>{
    public float x;
    public float y;


    public VectorSAT(float x, float y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public int compareTo(@NonNull VectorSAT another) {
        float thisLength = getLength();
        float thatLength = another.getLength();
        if(thisLength == thatLength)
            return 0;
        return thisLength > thatLength ? 1 : -1;
    }

    public float getLength() {
        return (float) Math.sqrt(x*x + y*y);
    }

}