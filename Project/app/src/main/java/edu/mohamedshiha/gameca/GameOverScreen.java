package edu.mohamedshiha.gameca;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class GameOverScreen extends AppCompatActivity {

    TextView tv_score,tv_Level,tv_GameOver;
    DatabaseHandler db = new DatabaseHandler(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over_screen);

        tv_score = findViewById(R.id.TV_ScoreItem);
        tv_Level = findViewById(R.id.tv_Level);
        tv_GameOver = findViewById(R.id.TV_GameOver);

        Score score = new Score("Test","Moh",(MainActivity.CurrentLevel-1)+"",""+(MainActivity.CurrentScore));
        tv_score.setText("Your Score is: "+score.getScore());
        tv_Level.setText("Your got to level: "+score.getLevel());
        if(Integer.parseInt(score.getScore()) > 0 )
            db.addScore(score);
        if(db.getHighScore() != null && Integer.parseInt(score.getScore()) > Integer.parseInt(db.getHighScore().getScore()) )
            tv_GameOver.setText("New High Score");
    }


    public void StartNewGame(View view){
        StartActivityFrom(new Intent(this,MainActivity.class));
    }

    public void ShowScoreBoard(View view){
        StartActivityFrom(new Intent(this,ScoreBoard.class));
    }

    private void StartActivityFrom(Intent intent) {
        MainActivity.CurrentScore = 0;
        MainActivity.CurrentLevel = 0;
        startActivity(intent);
        this.finish();
    }


}