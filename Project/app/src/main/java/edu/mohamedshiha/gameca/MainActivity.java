package edu.mohamedshiha.gameca;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Random;
import java.util.Timer;

public class MainActivity extends AppCompatActivity {

    Button btnMonkey,btnMouse,btnCat,btnDog,btn_play;
    TextView tv_UserDisplay, tv_score, tv_level;
    int[] currentGuess;
    CountUpTimer highlightTimer;
    CountUpTimer GameLoop;
    int LevelDifficulty = 1;
    boolean timeUp = false;
    boolean highlightStarted = false;
    boolean DataLoaded = false;

    public static int CurrentLevel;
    public static  int CurrentScore;
    Random rand = new Random();

    enum GameState{
        Setup,
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

        // get view elements
        btnCat = findViewById(R.id.btn_cat);
        btnDog = findViewById(R.id.btn_dog);
        btnMonkey = findViewById(R.id.btn_monkey);
        btnMouse = findViewById(R.id.btn_mouse);
        btn_play = findViewById(R.id.Btn_Play);

        tv_UserDisplay = findViewById(R.id.TV_UserDisplay);
        tv_score = findViewById(R.id.TV_Score);
        tv_level = findViewById(R.id.TV_CurrentLevel);

        // setup game variables
        // using the counter to animate the buttons
        highlightTimer = new CountUpTimer(10000000) {
            public void onTick(float second) {
                timeUp = this.CurrentSeconds >1.5;
            }
        };

        // using an enum to track game state
        gameState = GameState.Setup;
        // using the counter as a game loop
        // this will hold all game logic
        GameLoop = new CountUpTimer(10000000) {
            public void onTick(float second) {
                tv_score.setText("Score: "+(CurrentScore));
                switch (gameState){
                    case Setup:
                        Setup();
                        break;
                    case Start:
                        SetupGameToStart();
                        break;
                    case ShowingGuess:
                        //tv_level.setText("Index: "+(currentButtonToLightIndex));
                        LightButtons();
                        break;
                    case ShowingMessage:
                        if (highlightTimer.CurrentSeconds >=0.8)
                            gameState = GameState.NextActivity;
                        break;
                    case NextActivity:
                        //start activity here for now just stop game loop;
                        tv_UserDisplay.setText("");
                        LevelDifficulty +=2;
                        CurrentLevel++;
                        DataLoaded = false;
                        //recreate();
                        btn_play.setEnabled(true);
                        StartPlayScreen();
                        gameState = GameState.Setup;
                        break;
                }
            }
        };
        // start the game
        GameLoop.start();
    }

    private void StartPlayScreen(){
        Intent playScreenIntent = new Intent(this,PlayScreen.class);
        //playScreenIntent.getIntExtra("Size",LevelDifficulty-2);
        playScreenIntent.putExtra("Guess",currentGuess);
        startActivity(playScreenIntent);
        DataLoaded = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void StartLevel(View view) {
        gameState = GameState.Start;
        highlightTimer.start();
        btn_play.setEnabled(false);
    }

    private void Setup() {
        if(DataLoaded) return;
        tv_score.setText("Score: "+(CurrentScore));
        tv_level.setText("Level: "+(CurrentLevel+1));
        tv_UserDisplay.setText( ("Get Ready!! Press Play") );
        timeUp = false;
        currentButtonToLightIndex = 0;
        // reset guess array to get a new one
        currentGuess = null;
        FillGuessArray();
        DataLoaded = true;
    }

    private void SetupGameToStart() {

        //if(highlightTimer == null) return;
        if(highlightTimer.CurrentSeconds > 0.5) {
            tv_UserDisplay.setText("");
            highlightTimer.cancel();
            highlightTimer.CurrentSeconds =0;
            gameState = GameState.ShowingGuess;
            //LightButtons();
        }
    }

    /*
     * will fill the array with random selection
     * this will make sure the number dose not show up twice in a row
     */
    private void FillGuessArray(){
        // set the array size to match the level difficulty starts at '1'
        currentGuess = new int[LevelDifficulty];

        int lastGuess = -1;
        for (int i = 0; i< currentGuess.length;i++){
            // a save exit from the while loop to avoid infinite loop
            int saveExit = 0;
            while(saveExit < 30 && true) {
                int guess = rand.nextInt(4);
                if (guess != lastGuess) {
                    lastGuess = guess;
                    break;
                }
                saveExit++;
            }
            currentGuess[i] = lastGuess;
        }
    }

    private int currentButtonToLightIndex = 0;

    // will animate the buttons depending on the guess array
    // the button animation happens in "StartHighlight(Button btn)" method
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
            // when every guess has been shown, this will change the game state to the next state
            if(currentButtonToLightIndex >= currentGuess.length){
                //tv_level.setText("Index: "+(currentButtonToLightIndex));
                String Text = rand.nextBoolean() ? "Match The Pattern" : "Did You Get That";
                tv_UserDisplay.setText(Text);
                highlightTimer.start();
                highlightTimer.CurrentSeconds = 0;
                gameState = GameState.ShowingMessage;
            }
        }
    }


    private void StartHighlight(Button btn) {
        if(timeUp)
        {
            //Toast.makeText(this,"Time is up: "+currentButtonToLightIndex,Toast.LENGTH_SHORT).show();
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