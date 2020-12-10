package edu.mohamedshiha.gameca;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class GameOverScreen extends AppCompatActivity {

    TextView tv_score,tv_Level,tv_GameOver;
    // using database to check if the current score is a new high score
    // also to add the new score to the database
    DatabaseHandler db = new DatabaseHandler(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over_screen);

        tv_score = findViewById(R.id.TV_ScoreItem);
        tv_Level = findViewById(R.id.tv_Level);
        tv_GameOver = findViewById(R.id.TV_GameOver);

        // create score object from the current score and level
        Score score = new Score("Test","Moh",(MainActivity.CurrentLevel-1)+"",""+(MainActivity.CurrentScore));
        // show score on screen
        tv_score.setText("Your Score is: "+score.getScore());
        tv_Level.setText("Your got to level: "+score.getLevel());
        // check if the score is <=0 don't add it
        if(Integer.parseInt(score.getScore()) > 0 )
            db.addScore(score);
        // check if the current score is a new high score
        if(db.getHighScore() != null && Integer.parseInt(score.getScore()) > Integer.parseInt(db.getHighScore().getScore()) )
            tv_GameOver.setText("New High Score");
    }


    public void StartNewGame(View view){
        StartActivityFrom(MainActivity.GameState.Restart);
    }

    public void ShowScoreBoard(View view){
        StartActivityFrom(MainActivity.GameState.End);
    }

    // will reset the game and start an intent (sequence screen or high score screen)
    private void StartActivityFrom(MainActivity.GameState gameState) {
        MainActivity.CurrentScore = 0;
        MainActivity.CurrentLevel = 0;
        MainActivity.gameState = gameState;
        //startActivity(intent);
        this.finish();
    }


}