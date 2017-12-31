package com.example.root.scarnedice;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Method;
import java.util.Random;
import java.util.concurrent.Callable;

import android.os.Handler;

//CompoundButton.OnCheckedChangeListener
public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    boolean mode = false;

    private int yourOverAllScore = 0;
    private int yourTurnScore;
    private int compOverAllScore = 0;
    private int comTurnScore;
    private Random random = new Random();

    ImageView diceView;

    TextView ComputerScoreValue;
    TextView yourScoreValue;
    TextView yourTurnScoreValue;
    TextView yourTurnScoreLable;
    TextView compTurnScoreValue;
    TextView compTurnScoreLable;

    Button rollButton;
    Button holdButton;
    Button resetButton;

    Switch switchMode;

    RelativeLayout relaLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rollButton = (Button)findViewById(R.id.rollButton);
        holdButton = (Button) findViewById(R.id.holdButton);
        resetButton = (Button) findViewById(R.id.resetButton);

        ComputerScoreValue = (TextView) findViewById(R.id.computerScoreValue);
        yourScoreValue = (TextView) findViewById(R.id.yourScoreValue);
        yourTurnScoreValue = (TextView) findViewById(R.id.yourTurnScoreValue);
        yourTurnScoreLable = (TextView) findViewById(R.id.yourTurnScoreLable);
        compTurnScoreLable = (TextView) findViewById(R.id.compTurnScoreLable);
        compTurnScoreValue = (TextView) findViewById(R.id.compTurnScoreValue);

        switchMode = (Switch) findViewById(R.id.mode);

        diceView = (ImageView) findViewById(R.id.diceView);

       // relaLayout = (RelativeLayout) findViewById(R.id.relaLayout);

        rollButton.setOnClickListener(this);
        holdButton.setOnClickListener(this);
        resetButton.setOnClickListener(this);
        //switchMode.setOnCheckedChangeListener(this);
        rollingDice(6,1);
        yourTurn();
    }
//    @Override
//    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//        if(isChecked){
//            mode = true;
//            changeDiceImageForBeastMode(6);
//            relLayout.setBackgroundResource(R.drawable.beastbackground);
//            createToast("Beast Mode Activated",500);
//        }
//        else{
//            mode = false;
//            changeDiceImageForLiteMode(1);
//            relLayout.setBackgroundResource(R.drawable.litebackground);
//            createToast("Lite Mode Activated",500);
//        }
//    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rollButton:
                switchMode.setEnabled(false);
                int score = rollingDice(6,1);
                if(score == 1){
                    yourTurnScore = 0;
                    yourTurnScoreValue.setText(String.valueOf(yourTurnScore));
                    createToast("You Encounter 1",1000);
                    computerTurn();
                }
                else{
                    yourTurnScore+=score;
                    yourTurnScoreValue.setText(String.valueOf(yourTurnScore));
                }
                break;
            case R.id.holdButton:
                yourOverAllScore += yourTurnScore;
                yourScoreValue.setText((String.valueOf(yourOverAllScore)));
                if(yourOverAllScore >= 100){
                    createToast("You Wins! Keep Playing....!!!",1000);
                    startNewGame();
                }else {
                    computerTurn();
                }
                break;
            case R.id.resetButton:
                resetGame();
                break;
        }
    }

    private void computerTurn() {
        createToast("Computer Turn",1000);
        comTurnScore = 0;
        compTurnScoreValue.setText(String.valueOf(comTurnScore));
        rollButton.setEnabled(false);
        holdButton.setEnabled(false);
        resetButton.setEnabled(false);
        yourTurnScoreValue.setVisibility(View.INVISIBLE);
        yourTurnScoreLable.setVisibility(View.INVISIBLE);
        compTurnScoreLable.setVisibility(View.VISIBLE);
        compTurnScoreValue.setVisibility(View.VISIBLE);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                computerPlays();
            }
        },1000);
    }

    private void computerPlays() {
        int score;
        if(!mode) {
            score = rollingDice(6, 1);
        }
        else{
            score = rollingDice(5,2);
        }
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {}
        },1000);
        if(score == 1){
            comTurnScore = 0;
            compTurnScoreValue.setText(String.valueOf(comTurnScore));
            createToast("Computer Encounter 1",500);
            yourTurn();
        }
        else {
            comTurnScore += score;
            compTurnScoreValue.setText(String.valueOf(comTurnScore));
            if (compOverAllScore + comTurnScore >= 100) {
                computerHold();
            } else {
                if (comTurnScore >= 20) {
                    computerHold();
                } else {
                    handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            computerPlays();
                        }
                    }, 1000);
                }
            }
        }
    }

    private void computerHold() {
        createToast("Computer Holds",500);
        compOverAllScore += comTurnScore;
        ComputerScoreValue.setText(String.valueOf(compOverAllScore));
        if(compOverAllScore >= 100){
            createToast("Computer Wins! Keep Trying",1000);
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startNewGame();
                }
            },1000);
        }else{
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    yourTurn();;
                }
            },1000);
        }
    }

    private void startNewGame() {
        createToast("Starting New Game.....!!!",1000);
        rollingDice(6,1);
        resetGame();
    }

    private void resetGame() {
        compOverAllScore = 0;
        yourOverAllScore = 0;
        ComputerScoreValue.setText(String.valueOf(compOverAllScore));
        yourScoreValue.setText(String.valueOf(yourOverAllScore));
        switchMode.setEnabled(true);
        yourTurn();
    }

    private void yourTurn() {
        createToast("Your Turn",500);
        yourTurnScore = 0;
        yourTurnScoreValue.setText(String.valueOf(yourTurnScore));
        yourTurnScoreValue.setVisibility(View.VISIBLE);
        yourTurnScoreLable.setVisibility(View.VISIBLE);
        compTurnScoreLable.setVisibility(View.INVISIBLE);
        compTurnScoreValue.setVisibility(View.INVISIBLE);

        rollButton.setEnabled(true);
        holdButton.setEnabled(true);
        resetButton.setEnabled(true);
    }

    private int rollingDice(int x,int y) {
        int diceFront = random.nextInt(x)+y;
        if(mode){
            changeDiceImageForBeastMode(diceFront);
        }
        else{
            changeDiceImageForLiteMode(diceFront);
        }
        return diceFront;
    }
    private void changeDiceImageForBeastMode(int x){
        switch (x){
            case 1:
                diceView.setImageResource(R.drawable.dice1);
                break;
            case 2:
                diceView.setImageResource(R.drawable.dice2);
                break;
            case 3:
                diceView.setImageResource(R.drawable.dice3);
                break;
            case 4:
                diceView.setImageResource(R.drawable.dice4);
                break;
            case 5:
                diceView.setImageResource(R.drawable.dice5);
                break;
            case 6:
                diceView.setImageResource(R.drawable.dice6);
                break;
        }
    }

    private void changeDiceImageForLiteMode(int x){
        switch (x){
            case 1:
                diceView.setImageResource(R.drawable.bluedice1);
                break;
            case 2:
                diceView.setImageResource(R.drawable.bluedice2);
                break;
            case 3:
                diceView.setImageResource(R.drawable.bluedice3);
                break;
            case 4:
                diceView.setImageResource(R.drawable.bluedice4);
                break;
            case 5:
                diceView.setImageResource(R.drawable.bluedice5);
                break;
            case 6:
                diceView.setImageResource(R.drawable.bluedice6);
                break;
        }
    }

    private void createToast(String msg,int time){
        final Toast toast = Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG);
        toast.show();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                toast.cancel();
            }
        },time);
    }
}
