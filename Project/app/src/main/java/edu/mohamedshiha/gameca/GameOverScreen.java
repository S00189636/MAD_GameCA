package edu.mohamedshiha.gameca;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class GameOverScreen extends AppCompatActivity {

    TextView tv_score,tv_Level,tv_GameOver;
    EditText et_name;
    Score score;
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
        et_name = findViewById(R.id.ET_Name);

        // create score object from the current score and level
        score = new Score("Moh",(MainActivity.CurrentScore)+"",""+(MainActivity.CurrentLevel-1));
        // show score on screen
        tv_score.setText("Your Score is: "+score.getScore());
        tv_Level.setText("Your got to level: "+score.getLevel());


        // check if the current score is a new high score
        ArrayList<Score> scores = db.GetTopFive();
        for(Score highScore : scores){
            if(score.getScoreInt() > highScore.getScoreInt()){
                tv_GameOver.setText("New High Score Enter Your Name");
                ((Button)findViewById(R.id.btn_SaveScore)).setEnabled(true);
                et_name.setEnabled(true);
                break;
            }
        }
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


    public void SaveHighScore(View view) {
        score.setName(et_name.getText().toString());
        db.addScore(score);
        Toast.makeText(this,"Saved",Toast.LENGTH_LONG).show();
        ((Button) view).setEnabled(false);
        et_name.setEnabled(false);
        et_name.setBackgroundColor(Color.GRAY);
    }
}