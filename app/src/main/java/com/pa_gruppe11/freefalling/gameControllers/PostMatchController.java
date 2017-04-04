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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_match_view);

        textField = (TextView) findViewById(R.id.textView);
        textField.setText("Player 1 wins!");
        timer = (TextView) findViewById(R.id.timerView);

        newGame = (Button) findViewById(R.id.newGame);
        lobby = (Button) findViewById(R.id.lobby);
        new CountDownTimer(30000, 1000) {
            public void onTick(long millisUntilFinished) {
                timer.setText("Seconds until return to lobby: "+(millisUntilFinished / 1000));
            }
            public void onFinish() {
                //go back to lobby
            }
        }.start();
    }
    public PostMatchController(Player winner) {
        this.winner=winner;
    }
    public void startNewMatch() {
        System.out.println("Starting new match");
    }
}
