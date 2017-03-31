package com.pa_gruppe11.freefalling.gameControllers;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by skars on 31.03.2017.
 */

public class GameMenu extends Activity {

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
    }


    public void goTo(Class javaClass) {
        Intent intent = new Intent(this, javaClass);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
        Log.w("GameMenu", "goTo " + javaClass.getCanonicalName());
        overridePendingTransition(0,0);
    }


}
