package edu.mohamedshiha.gameca;

import androidx.appcompat.app.AppCompatActivity;

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
            public void onTick(float second) {
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
                }
                if(checkIndex >= correctAnswer.length){
                    MainActivity.CurrentScore +=4;
                    tv_UserDisplay.setText("You win");
                    GoBack();
                }
            }
        };
        GameLoop.start();

    }

    void GoBack(){
        sensorManager.unregisterListener(this,Accelerometer);
        MainActivity.CurrentScore +=4;
        GameLoop.cancel();
        finish();
    }
    @Override
    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];
        if(readUserInput) {
            // get user input
            if (x < -2.5 && (y > -1 && y < 1)) {
                userInput = UserInput.Cat;
                readUserInput = false;
            }else if(x > 2.5 && (y > -1 && y < 1)){
                userInput = UserInput.Dog;
                readUserInput = false;
            }else if (y < -2.5 && (x > -1 && x < 1)) {
                userInput = UserInput.Mouse;
                readUserInput = false;
            }else if(y > 2.5 && (x > -1 && x < 1)){
                userInput = UserInput.Monkey;
                readUserInput = false;
            }
        }else if( (x > -2 && x < 2) && (y > -2 && y < 2) )
        {
            // check if the Input is correct
            if(userInput.getValue() != correctAnswer[checkIndex]) {
                readUserInput = true;
                Toast.makeText(this,"Too bad you lost",Toast.LENGTH_SHORT).show();
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