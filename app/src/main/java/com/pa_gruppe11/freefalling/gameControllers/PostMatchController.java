package com.pa_gruppe11.freefalling.gameControllers;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import com.pa_gruppe11.freefalling.Models.Player;
import com.pa_gruppe11.freefalling.R;

/**
 * Created by Martin Kostveit on 31.03.2017.
 *
 */

public class PostMatchController extends AppCompatActivity {
    private TextView textField, timer;
    private Button newGame, lobby;
    Player winner;


    public PostMatchController(Player winner) {
        this.winner=winner;
    }
    public void startNewMatch() {
        System.out.println("Starting new match");
    }
}
