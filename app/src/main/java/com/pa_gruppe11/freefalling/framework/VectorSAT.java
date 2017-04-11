package com.pa_gruppe11.freefalling.framework;

import android.support.annotation.NonNull;
import android.util.Log;

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


    public static VectorSAT getUnitVector(VectorSAT vector) {
        VectorSAT v = new VectorSAT(vector.x, vector.y);    // copy of vector
        float vLength = (float) Math.sqrt(Math.pow(v.x, 2) + Math.pow(v.y, 2));
        v.x = v.x/vLength;
        v.y = v.y/vLength;
        return v;
    }


    public static VectorSAT getPerpendicular(VectorSAT v) {
        return getUnitVector(new VectorSAT(v.y, -v.x));
    }

    public String toString() {
        return "[" + x + ", "+y + "]";
    }

}