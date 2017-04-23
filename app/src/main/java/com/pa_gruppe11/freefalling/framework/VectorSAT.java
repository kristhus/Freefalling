package com.pa_gruppe11.freefalling.framework;

import android.support.annotation.NonNull;
import android.util.Log;

/**
 * Created by Kristian on 10/04/2017.
 * A custom vector with direction and position
 * Contains some basic vector calculation
 */

public class VectorSAT implements Comparable<VectorSAT>{
    public float x;
    public float y;

    public float magnitude;


    public VectorSAT(float x, float y) {
        this.x = x;
        this.y = y;

    }

    /**
     * Used to compare two vectors
     * @param another
     * @return greater, larger or equal
     */
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


    /**
     * Get the unitvector for the given vector
     * @param vector the vector to perform calculations on
     * @return
     */
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

    /**
     * returns the magnitude for a vector given as two floats
     * @param x point x
     * @param y point y
     * @return ||[x,y]||
     */
    public float calculateMagnitude(float x, float y){
        return (float)Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
    }

    public String toString() {
        return "[" + x + ", "+y + "]";
    }

    public float getMagnitude() {
        return magnitude;
    }

    public void setMagnitude(float magnitude) {
        this.magnitude = magnitude;
    }
}