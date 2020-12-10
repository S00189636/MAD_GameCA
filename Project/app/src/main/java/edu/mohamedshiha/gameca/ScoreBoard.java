package edu.mohamedshiha.gameca;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class ScoreBoard extends AppCompatActivity {

    ListView lv_scores;
    DatabaseHandler db = new DatabaseHandler(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_board);

        lv_scores = findViewById(R.id.LV_TopScores);
        FillTopFiveScores();
    }

    public void StartNewGame(View view){
        MainActivity.CurrentScore = 0;
        MainActivity.CurrentLevel = 0;
        startActivity(new Intent(this,MainActivity.class));
        this.finish();
    }


    // this will fill the listview with top 5 high scores from the database
    // the list returned from the database dose not have any duplicates
    // so i need to handel nulls if i have less then 5 in the database
    private void FillTopFiveScores(){

        Score[] scores = new Score[5];
        List<Score> scoreList = db.getAllScores();
        // if no data in database don't do anything
        if(scoreList.size() < 1) return;

        // if i have less then 5 scores
        // fill what i have and fill the rest with 0s
        if(scoreList.size() < 5){
            for (int i = 0; i < scoreList.size(); i++){
                scores[i] = scoreList.get(i);
            }
            for (int i = scoreList.size(); i< scores.length;i++){
                scores[i] = new Score("0","0");
            }
        }
        // if i have more then 5 in database fill first 5
        // knowing that the list returned from database is sorted by the score
        else {
            for (int i = 0; i < scores.length; i++){
                scores[i] = scoreList.get(i);
            }
        }
        // convert array to list to give it to the adopter
        List<Score> scoreListSorted = new ArrayList<>();
        for (Score s : scores)
            scoreListSorted.add(s);
        // finally fill the adapter
        lv_scores.setAdapter(new ScoreListViewAdapter(this,scoreListSorted));
    }

    public void ClearScores(View view) {
        db.emptyScores();
        lv_scores.setAdapter(new ScoreListViewAdapter(this,db.getAllScores()));
    }
}