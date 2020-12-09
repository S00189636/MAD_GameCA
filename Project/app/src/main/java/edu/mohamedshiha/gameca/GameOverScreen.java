package edu.mohamedshiha.gameca;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class GameOverScreen extends AppCompatActivity {

    TextView tv_score,tv_Level;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over_screen);

        tv_score = findViewById(R.id.TV_Score);
        tv_Level = findViewById(R.id.tv_Level);
        tv_score.setText("Your Score is: "+MainActivity.CurrentScore);
        tv_Level.setText("Your Score is: "+MainActivity.CurrentScore);
    }


    public void StartNewGame(View view){
        MainActivity.CurrentScore = 0;
        startActivity(new Intent(this,MainActivity.class));
        this.finish();
    }
}