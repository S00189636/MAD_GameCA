package edu.mohamedshiha.gameca;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Random;
import java.util.Timer;

public class MainActivity extends AppCompatActivity {

    Button btnMonkey,btnMouse,btnCat,btnDog;
    TextView tv_UserDisplay, tv_score, tv_level;
    int[] currentGuess;
    CountUpTimer highlightTimer;
    CountUpTimer GameLoop;
    int LevelDifficulty = 4;
    boolean timeUp = false;
    boolean highlightStarted = false;

    int CurrentLevel ,CurrentScore ;
    Random rand = new Random();
    enum GameState{
        Start,
        ShowingGuess,
        ShowingMessage,
        NextActivity
    }

    GameState gameState = GameState.Start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnCat = findViewById(R.id.btn_cat);
        btnDog = findViewById(R.id.btn_dog);
        btnMonkey = findViewById(R.id.btn_monkey);
        btnMouse = findViewById(R.id.btn_mouse);

        tv_UserDisplay = findViewById(R.id.TV_UserDisplay);
        tv_score = findViewById(R.id.TV_Score);
        tv_level = findViewById(R.id.TV_CurrentLevel);

        currentGuess = null;
        highlightTimer = new CountUpTimer(10000000) {
            public void onTick(float second) {
               timeUp = this.CurrentSeconds >1.9;
            }
        };

        FillGuessArray();
        gameState = GameState.Start;
        highlightTimer.start();


        GameLoop = new CountUpTimer(10000000) {
            public void onTick(float second) {
                switch (gameState){
                    case Start:
                        tv_score.setText("Score: "+(CurrentScore+1));
                        tv_level.setText("Level: "+(CurrentLevel+1));
                        tv_UserDisplay.setText( ("Get Ready!!") );
                        if(highlightTimer.CurrentSeconds > 1) {
                            tv_UserDisplay.setText("");
                            highlightTimer.cancel();
                            highlightTimer.CurrentSeconds =0;
                            gameState = GameState.ShowingGuess;
                        }
                        break;
                    case ShowingGuess:
                        LightButtons();
                        break;
                    case ShowingMessage:
                        if (highlightTimer.CurrentSeconds >=1)
                            gameState = GameState.NextActivity;
                        break;
                    case NextActivity:
                        //start activity here for now just stop game loop;
                        this.cancel();
                        tv_UserDisplay.setText("");
                        recreate();
                        break;
                }
            }
        };

        GameLoop.start();
    }


    private void FillGuessArray(){
        currentGuess = new int[LevelDifficulty];
        int lastGuess = -1;
        for (int i = 0; i< currentGuess.length;i++){
            while(true) {
                int guess = rand.nextInt(4);
                if (guess != lastGuess) {
                    lastGuess = guess;
                    break;
                }
            }
            currentGuess[i] = lastGuess;
        }
    }

    private int currentButtonToLightIndex = 0;

    private void LightButtons(){
        if(currentGuess != null){
            //Toast.makeText(this,"Index: "+currentButtonToLightIndex,Toast.LENGTH_SHORT).show();
            switch (currentGuess[currentButtonToLightIndex]){
                case 0:
                    StartHighlight(btnCat);
                    break;
                case  1:
                    StartHighlight(btnDog);
                    break;
                case 2:
                    StartHighlight(btnMouse);
                    break;
                case  3:
                    StartHighlight(btnMonkey);
                    break;
            }

            if(currentButtonToLightIndex >= currentGuess.length){
                String Text = rand.nextBoolean() ? "Match The Pattern" : "Did You Get That";
                tv_UserDisplay.setText(Text);
                highlightTimer.start();
                gameState = GameState.ShowingMessage;
            }
        }
    }


    private void StartHighlight(Button btn) {
        if(timeUp)
        {
            highlightTimer.cancel();
            btn.setEnabled(false);
            currentButtonToLightIndex++;
            timeUp= false;
            highlightStarted = false;
        }else if(!highlightStarted) {
            highlightTimer.start();
            highlightTimer.CurrentSeconds = 0;
            highlightStarted = true;
            btn.setEnabled(true);
        }
    }

}