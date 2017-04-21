package com.pa_gruppe11.freefalling.gameControllers;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.Toast;

import com.pa_gruppe11.freefalling.R;
import com.pa_gruppe11.freefalling.Singletons.Config;
import com.pa_gruppe11.freefalling.Singletons.DataHandler;

/**
 * Created by skars on 31.03.2017.
 */

public class Settings extends GameMenu implements SeekBar.OnSeekBarChangeListener {

    private int sfx;
    private int bgm;


    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.settings);
        loadValuesToView();

        findViewById(R.id.saveButton).setVisibility(View.GONE); //loading values performs changes: set to gone
    }


    public void save(View view){
        String msg = "Error saving changes";
        DataHandler data = DataHandler.getInstance();
        if(Config.getInstance().saveFile(this)) {
            findViewById(R.id.saveButton).setVisibility(View.GONE);
            msg = "Changes saved";
        }
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        Log.w("Settings", "data set to " + data.isHideMinimap());
    }

    public void loadValuesToView(){
        Config.getInstance().readFile(this);
        DataHandler data = DataHandler.getInstance();

        ((CheckBox) findViewById(R.id.fpsCheckBox)).setChecked(data.getFPS() == 60 ? true : false);
        ((CheckBox) findViewById(R.id.bgmCheckBox)).setChecked(data.isBgmMuted());
        ((CheckBox) findViewById(R.id.sfxCheckBox)).setChecked(data.isSfxMuted());
        ((CheckBox) findViewById(R.id.minimapCheckbox)).setChecked(!data.isHideMinimap());
        ((SeekBar) findViewById(R.id.sfxLevelSeekbar)).setOnSeekBarChangeListener(this);
        ((SeekBar) findViewById(R.id.sfxLevelSeekbar)).setProgress(data.getSfxLevel());
        ((SeekBar) findViewById(R.id.bgmLevelSeekbar)).setOnSeekBarChangeListener(this);
        ((SeekBar) findViewById(R.id.bgmLevelSeekbar)).setProgress(data.getBgmLevel());

    }

    public void onChange(View view) {
        int id = view.getId();
        DataHandler data = DataHandler.getInstance();
        switch(id) {
            case R.id.fpsCheckBox:
                Log.w("Settings", "pressed fpsCB");
                boolean fpsCB = ((CheckBox) findViewById(id)).isChecked();
                if(fpsCB)
                    data.setFPS(60);
                else
                    data.setFPS(30);
                break;
            case R.id.bgmCheckBox:
                Log.w("Settings", "pressed bgmCB");
                boolean bgmCB = ((CheckBox) findViewById(id)).isChecked();
                data.setBgmMuted(bgmCB);
                break;
            case R.id.sfxCheckBox:
                Log.w("Settings", "pressed sfxCB");
                boolean sfxCB = ((CheckBox) findViewById(id)).isChecked();
                data.setSfxMuted(sfxCB);
                break;
            case R.id.minimapCheckbox:
                Log.w("Settings", "pressed minimapCB");
                boolean miniCB = !((CheckBox) findViewById(id)).isChecked();
                data.setHideMinimap(miniCB);
                break;
            case R.id.bgmLevelSeekbar:
                int bgmValue = ((SeekBar) findViewById(id)).getProgress();
                data.setBgmLevel(bgmValue);
                break;
            case R.id.sfxLevelSeekbar:
                int sfxValue = ((SeekBar) findViewById(id)).getProgress();
                data.setSfxLevel(sfxValue);
                break;

        }
        findViewById(R.id.saveButton).setVisibility(View.VISIBLE);
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        int id = seekBar.getId();
        DataHandler data = DataHandler.getInstance();
        switch(id) {
            case R.id.bgmLevelSeekbar:
                int bgmValue = ((SeekBar) findViewById(id)).getProgress();
                data.setBgmLevel(bgmValue);
                break;
            case R.id.sfxLevelSeekbar:
                int sfxValue = ((SeekBar) findViewById(id)).getProgress();
                Log.w("Settings", "Changed -> SFX-level: " + sfxValue);
                data.setSfxLevel(sfxValue);
                break;
        }
        findViewById(R.id.saveButton).setVisibility(View.VISIBLE);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
