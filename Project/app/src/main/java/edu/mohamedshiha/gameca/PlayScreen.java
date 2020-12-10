package edu.mohamedshiha.gameca;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class PlayScreen extends AppCompatActivity implements SensorEventListener {

    enum UserInput{
        Cat(0),
        Dog(1),
        Mouse(2),
        Monkey(3),
        Nothing(4),
        ;

        private  int Value;
        UserInput(int value){
            Value = value;
        }

        public int getValue() {
            return Value;
        }
    }
    Sensor Accelerometer;
    SensorManager sensorManager;
    UserInput userInput;
    Button btnMonkey,btnMouse,btnCat,btnDog;
    boolean readUserInput;
    CountUpTimer GameLoop;
    int[] correctAnswer;
    int checkIndex = 0;
    boolean gameOver = false;
    TextView tv_UserDisplay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_screen);

        btnCat = findViewById(R.id.btn_cat);
        btnDog = findViewById(R.id.btn_dog);
        btnMonkey = findViewById(R.id.btn_monkey);
        btnMouse = findViewById(R.id.btn_mouse);

        tv_UserDisplay = findViewById(R.id.TV_UserDisplay);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        userInput = UserInput.Nothing;
        readUserInput = true;

        correctAnswer = getIntent().getIntArrayExtra("Guess");
        sensorManager.registerListener(this,Accelerometer,SensorManager.SENSOR_DELAY_GAME);


        GameLoop = new CountUpTimer(10000000) {
            public void onTick(double second) {
                btnCat.setEnabled(false);
                btnDog.setEnabled(false);
                btnMonkey.setEnabled(false);
                btnMouse.setEnabled(false);
                switch (userInput){
                    case Cat:
                        btnCat.setEnabled(true);
                        break;
                    case Dog:
                        btnDog.setEnabled(true);
                        break;
                    case Mouse:
                        btnMouse.setEnabled(true);
                        break;
                    case Monkey:
                        btnMonkey.setEnabled(true);
                        break;
                }

                if(gameOver){
                    // show game over screen
                    tv_UserDisplay.setText("Game Over");
                    StartGameOverScreen();
                }
                if(checkIndex >= correctAnswer.length){
                    MainActivity.CurrentScore +=4;
                    tv_UserDisplay.setText("You win");
                    CleanActivity();
                }
            }
        };
        GameLoop.start();

    }

    void StartGameOverScreen(){
        CleanActivity();
        startActivity(new Intent(this,GameOverScreen.class));
    }
    void CleanActivity(){
        sensorManager.unregisterListener(this,Accelerometer);
        GameLoop.cancel();
        finish();
    }
    @Override
    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];
        if(readUserInput) {
            // get user input
            if (x < -3.5 && (y > -2 && y < 2)) {
                userInput = UserInput.Cat;
                readUserInput = false;
            }else if(x > 3.5 && (y > -2 && y < 2)){
                userInput = UserInput.Dog;
                readUserInput = false;
            }else if (y < -3.5 && (x > -2 && x < 2)) {
                userInput = UserInput.Mouse;
                readUserInput = false;
            }else if(y > 3.5 && (x > -2 && x < 2)){
                userInput = UserInput.Monkey;
                readUserInput = false;
            }
        }else if( (x > -2 && x < 2) && (y > -2 && y < 2) )
        {
            // check if the Input is correct
            if(userInput.getValue() != correctAnswer[checkIndex] && !gameOver) {
                gameOver = true;
            }else if(userInput.getValue() == correctAnswer[checkIndex]){
                checkIndex++;
                userInput = UserInput.Nothing;
                readUserInput = true;
            }
            // reset if true
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}