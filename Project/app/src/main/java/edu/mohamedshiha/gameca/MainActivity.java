package edu.mohamedshiha.gameca;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    Button btnMonkey,btnMouse,btnCat,btnDog;
    int[] currentGuess;
    int LevelDifficulty = 4;
    Random rand = new Random();
    enum GameButtons{
        Cat,
        Dog,
        Mouse,
        Monkey
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnCat = findViewById(R.id.btn_cat);
        btnDog = findViewById(R.id.btn_dog);
        btnMonkey = findViewById(R.id.btn_monkey);
        btnMouse = findViewById(R.id.btn_mouse);

        currentGuess = null;

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
            switch (currentGuess[currentButtonToLightIndex]){
                case 0:
                    btnCat.setEnabled(true);
                    // start timer
                    // if timer is up make this false and add 1 to index
                    break;
                case  1:
                    break;
                case 2:
                    break;
                case  3:
                    break;
            }
        }
    }

}