package com.fatihyigit.simple2dgamepractice;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import android.view.MotionEvent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.Timer;
import java.util.TimerTask;

public class GameAreaActivity extends AppCompatActivity {
    private ConstraintLayout gameAreaConstraintLayout;
    private TextView textViewScore;
    private TextView textViewGameStart;
    private ImageView imageViewMainCharacter;
    private ImageView imageViewRed;
    private ImageView imageViewYellow;
    private ImageView imageViewBlack;

    private int score = 0;

    //positions
    private int mainCharacterX;
    private int mainCharacterY;
    private int redX;
    private int redY;
    private int yellowX;
    private int yellowY;
    private int blackX;
    private int blackY;

    //dimensions
    private int screenWidth;
    private int screenHeight;
    private int mainCharacterWidth;
    private int mainCharacterHeight;

    //controls
    private boolean touchState = false;
    private boolean startState = false;

    private Timer timer = new Timer();
    private Handler handler = new Handler();

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_area);

        textViewScore = findViewById(R.id.textViewScore);
        textViewGameStart = findViewById(R.id.textViewGameStart);
        imageViewMainCharacter = findViewById(R.id.imageViewMainCharacter);
        imageViewRed = findViewById(R.id.imageViewRed);
        imageViewYellow = findViewById(R.id.imageViewYellow);
        imageViewBlack = findViewById(R.id.imageViewBlack);
        gameAreaConstraintLayout = findViewById(R.id.gameAreaConstraintLayout);

        //throwing objects out when the game first starts
        imageViewRed.setX(-80);
        imageViewRed.setY(-80);
        imageViewYellow.setX(-80);
        imageViewYellow.setY(-80);
        imageViewBlack.setX(-80);
        imageViewBlack.setY(-80);

        gameAreaConstraintLayout.setOnTouchListener((v, event) -> {

            if (startState) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Log.e("MotionEvent", "Dokundu");
                        touchState = true;
                        return true;
                    case MotionEvent.ACTION_UP:
                        Log.e("MotionEvent", "Bıraktı");
                        touchState = false;
                        return true;
                }
            } else {
                startState = true;

                textViewGameStart.setVisibility(View.INVISIBLE);

                mainCharacterX = (int) imageViewMainCharacter.getX();
                mainCharacterY = (int) imageViewMainCharacter.getY();

                mainCharacterWidth = imageViewMainCharacter.getWidth();
                mainCharacterHeight = imageViewMainCharacter.getHeight();

                screenWidth = gameAreaConstraintLayout.getWidth();
                screenHeight = gameAreaConstraintLayout.getHeight();

                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        handler.post(() -> {
                            mainCharacterMove();
                            moveObjects();
                            collisionControl();
                            textViewScore.setText(String.valueOf(score));
                        });
                    }
                }, 0, 20);
            }

            return false;
        });
    }

    public void mainCharacterMove() {
        int mainCharacterSpeed = Math.round(screenHeight / 60.0f); //fix speed for each device screen size

        if (touchState) {
            mainCharacterY -= mainCharacterSpeed;
        } else {
            mainCharacterY += mainCharacterSpeed;
        }

        if (mainCharacterY <= 0) {
            mainCharacterY = 0;
        }

        if (mainCharacterY >= screenHeight - mainCharacterHeight) {
            mainCharacterY = screenHeight - mainCharacterHeight;
        }

        imageViewMainCharacter.setY(mainCharacterY);
    }

    public void moveObjects() {
        int yellowSpeed = Math.round(screenWidth / 50.0f); //fix speed for each device screen size
        int redSpeed = Math.round(screenWidth / 30.0f); //fix speed for each device screen size
        int blackSpeed = Math.round(screenWidth / 60.0f); //fix speed for each device screen size

        yellowX -= yellowSpeed;
        if (yellowX < 0) {
            yellowX = screenWidth + 20;
            yellowY = (int) (Math.random() * screenHeight);
        }

        imageViewYellow.setX(yellowX);
        imageViewYellow.setY(yellowY);

        redX -= redSpeed;
        if (redX < 0) {
            redX = screenWidth + 20;
            redY = (int) (Math.random() * screenHeight);
        }

        imageViewRed.setX(redX);
        imageViewRed.setY(redY);

        blackX -= blackSpeed;
        if (blackX < 0) {
            blackX = screenWidth + 20;
            blackY = (int) (Math.random() * screenHeight);
        }

        imageViewBlack.setX(blackX);
        imageViewBlack.setY(blackY);
    }

    public void collisionControl() {
        int yellowCenterX = yellowX + imageViewYellow.getWidth() / 2;
        int yellowCenterY = yellowY + imageViewYellow.getHeight() / 2;

        if (0 <= yellowX
                && yellowCenterX <= mainCharacterWidth
                && mainCharacterY <= yellowCenterY
                && yellowCenterY <= mainCharacterY + mainCharacterHeight) {
            score+=20;
            yellowX=-1;
        }

        int redCenterX = redX + imageViewRed.getWidth() / 2;
        int redCenterY = redY + imageViewRed.getHeight() / 2;

        if (0 <= redX
                && redCenterX <= mainCharacterWidth
                && mainCharacterY <= redCenterY
                && redCenterY <= mainCharacterY + mainCharacterHeight) {
            score+=50;
            redX=-1;
        }

        int blackCenterX = blackX + imageViewBlack.getWidth() / 2;
        int blackCenterY = blackY + imageViewBlack.getHeight() / 2;

        if (0 <= blackX
                && blackCenterX <= mainCharacterWidth
                && mainCharacterY <= blackCenterY
                && blackCenterY <= mainCharacterY + mainCharacterHeight) {
            blackX=-1;

            //timer stop
            timer.cancel();
            timer = null;

            Intent intent =new Intent(this,ResultActivity.class);
            intent.putExtra("score",score);
            startActivity(intent);
            finish();
        }
    }
}