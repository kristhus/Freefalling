package com.pa_gruppe11.freefalling.gameControllers;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.pa_gruppe11.freefalling.Singletons.GameThread;

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
        overridePendingTransition(0, 0);
    }

    @Override
    public void onBackPressed() {
        Class thisClass = this.getClass();
        if(thisClass == MainMenu.class) {
            // Quit ?Bitmap.createBitmap
            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(startMain);
        } else if(thisClass == GameActivity.class) {
            // set up prompt that you are leaving the game

        } else {
            goTo(MainMenu.class);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(this.getClass() == GameActivity.class)
            GameThread.getInstance().setRunning(false);
        Log.w("GameMenu", "Paused");
    }

    @Override
    public void onResume() {
        super.onResume();
        if(this.getClass() == GameActivity.class)
            GameThread.getInstance().setRunning(false);
        Log.w("GameMenu", "Resumed");
    }

    private void alertPrompt() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("Quit game in progress?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    public void goToMainMenu(View view) {
        if(getClass() != MainMenu.class)
            goTo(MainMenu.class);
    }

}
