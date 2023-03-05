package com.fatihyigit.simple2dgamepractice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {
    private TextView textViewTotalScore, textViewBestScore;
    private Button buttonTryAgain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        textViewTotalScore = findViewById(R.id.textViewTotalScore);
        textViewBestScore = findViewById(R.id.textViewBestScore);
        buttonTryAgain = findViewById(R.id.buttonTryAgain);

        int score = getIntent().getIntExtra("score",0);
        textViewTotalScore.setText(String.valueOf(score));

        SharedPreferences sharedPreferences = getSharedPreferences("Result",MODE_PRIVATE);
        int bestScore = sharedPreferences.getInt("bestScore",0);

        if(score > bestScore){
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("bestScore",score);
            editor.apply();

            textViewBestScore.setText(String.valueOf(score));
        } else {
            textViewBestScore.setText(String.valueOf(bestScore));
        }

        buttonTryAgain.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });
    }
}