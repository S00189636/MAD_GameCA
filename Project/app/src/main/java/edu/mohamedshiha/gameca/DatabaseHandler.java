package edu.mohamedshiha.gameca;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;



public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "CAGameScores";
    private static final String TABLE_Scores = "scores";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_Score = "score";
    private static final String KEY_Level = "level";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //3rd argument to be passed is CursorFactory instance
    }

    // Creating Table
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_Scores + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT," + KEY_Level + " TEXT,"
                + KEY_Score + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Scores);
        // Create tables again
        onCreate(db);
    }

    public void emptyScores() {
        // Drop older table if existed
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Scores);
        // Create tables again
        onCreate(db);
    }


    public ArrayList<Score> GetTopFive(){
        Score[] scores = new Score[5];
        List<Score> scoreList = getAllScores();
        // if i have less then 5 scores
        // fill what i have and fill the rest with 0s
        if(scoreList.size() < 5){
            for (int i = 0; i < scoreList.size(); i++){
                scores[i] = scoreList.get(i);
            }
            for (int i = scoreList.size(); i< scores.length;i++){
                scores[i] = new Score("0","Player","0","0");
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
        ArrayList<Score> scoreListSorted = new ArrayList<>();
        for (Score s : scores)
            scoreListSorted.add(s);
        return scoreListSorted;
    }



    // code to add the new Score
    void addScore(Score score) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, score.getName()); // player Name
        values.put(KEY_Level, score.getLevel()); // player level
        values.put(KEY_Score, score.getScore()); // player score
        // Inserting Row
        db.insert(TABLE_Scores, null, values);
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }


    // code to get all Scores as a list
    public List<Score> getAllScores() {
        List<Score> contactList = new ArrayList<Score>();
        // Select All Query grouping by score to not get duplicates
        String selectQuery = "SELECT  * FROM " + TABLE_Scores + " Group by "+ KEY_Score + " ORDER BY " +KEY_Score;
        SQLiteDatabase db = this.getWritableDatabase();
        //execute query
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
               //Log.i("Database index 0",cursor.getString(0)+""); // for debugging
                Score score = new Score(
                        cursor.getInt(0)+"",// id
                        cursor.getString(1),// name
                        cursor.getString(2),// level
                        cursor.getString(3)// score
                );
                // Adding contact to list
                contactList.add(score);
            } while (cursor.moveToNext());
        }
        cursor.close();
        // return contact list
        return contactList;
    }
}
