package com.example.duckhunter;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.Random;

public class GameActivity extends AppCompatActivity {

    TextView mTxtDuckHunted, mTxtTimer;
    ImageView mImgVDuck;
    int mCounter = 0;
    int mWidthDisplay, mHeightDisplay;
    Random mRandom;
    boolean mGameOver = true;

    MediaPlayer mStartAudio = new MediaPlayer();
    MediaPlayer mDuck_clicked = new MediaPlayer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        mStartAudio = MediaPlayer.create(this, R.raw.start);
        mDuck_clicked = MediaPlayer.create(this, R.raw.duck_clicked);

        mTxtDuckHunted = findViewById(R.id.txtVDucksHunted);
        mTxtTimer = findViewById(R.id.txtVTimer);

        mImgVDuck = findViewById(R.id.imgVDuck);

        // metodo de cuenta regresiva
        // countDown();
        // medidas de pantalla
        windowMetrics();

        // movimiento del pato
        // moveDuck();

        mImgVDuck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickDuck();
            }
        });


    }


    @Override
    protected void onStart() {
        super.onStart();
        showGameOverDialog();
    }

    private void clickDuck() {
        if (!mGameOver){
            mDuck_clicked.start();
            mCounter++;
            mTxtDuckHunted.setText(String.valueOf(mCounter));

            mImgVDuck.setImageResource(R.drawable.duck_clicked);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mImgVDuck.setImageResource(R.drawable.duck);
                    moveDuck();
                }
            }, 500);
        }
    }

    private void windowMetrics() {
        // obtenemos tamaño de la pantalla
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        mWidthDisplay = size.x - 100;
        mHeightDisplay = size.y - 100;

        // inicializamos objeto para generar numeros aleatorios
        mRandom = new Random();
    }

    private void countDown(int time) {
        time = time * 1000;

        new CountDownTimer(time, 1000){
            // mostrando los segundos restantes desde que inicia el metodo
            @Override
            public void onTick(long millisUntilFinished) {
                long lastSeconds = millisUntilFinished / 1000;
                mTxtTimer.setText(lastSeconds + "s");
            }
            // acciones al finalizar la cuenta regresiva
            @Override
            public void onFinish() {
                mTxtTimer.setText("0s");
                mGameOver = true;
                // alertGameOver();
                showGameOverDialog();
            }
        }.start();
    }


    private void moveDuck() {

        int min = 0;
        int maxX = mWidthDisplay - mImgVDuck.getWidth();
        int maxY = mHeightDisplay - mImgVDuck.getHeight();

        // generamos dos numeros aleatorios, uno para x otro para y
        int randomX = mRandom.nextInt(((maxX - min) + 1) + min);
        int randomY = mRandom.nextInt(((maxY - min) + 1) + min);

        mImgVDuck.setX(randomX);
        mImgVDuck.setY(randomY);

    }


    private void showGameOverDialog(){
        if (mGameOver){
            mStartAudio.start();
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            View view = getLayoutInflater().inflate(R.layout.config_game_dialog, null);

            builder.setView(view);
            AlertDialog dialog = builder.create();
            // mostramos el dialog en pantalla
            dialog.setCancelable(false);
            dialog.show();

            Button btnExit = view.findViewById(R.id.btnExitGame);
            btnExit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });

            TextView txtTimer = view.findViewById(R.id.txtVCounterTimer);
            txtTimer.setText(String.valueOf(10));

            TextView txtDucks = view.findViewById(R.id.txtVDuckCounter);
            txtDucks.setText(String.valueOf(mCounter));

            ImageView imgVPlus = view.findViewById(R.id.imgVAddTime);

            imgVPlus.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("SuspiciousIndentation")
                @Override
                public void onClick(View view) {
                    int time = Integer.parseInt(txtTimer.getText().toString());
                    if (time >= 60) time = 60;
                    else time = time + 10;
                        txtTimer.setText(String.valueOf(time));

                }
            });

            ImageView imgVLess = view.findViewById(R.id.imgVLessTime);
            imgVLess.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int time = Integer.parseInt(txtTimer.getText().toString());
                    if (time <= 10) time = 10;
                    else time = time - 10;
                    txtTimer.setText(String.valueOf(time));
                }
            });

            Button btnStart = view.findViewById(R.id.btnPlayGame);

            btnStart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();

                    mCounter = 0;
                    mTxtDuckHunted.setText("0");
                    mGameOver = false;
                    moveDuck();

                    int strTime = Integer.parseInt(txtTimer.getText().toString());
                    countDown(strTime);

                }
            });
            // end if
        }
    }


}