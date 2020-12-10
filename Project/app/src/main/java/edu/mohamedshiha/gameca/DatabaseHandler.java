package edu.mohamedshiha.gameca;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import org.w3c.dom.Comment;

import java.util.ArrayList;
import java.util.List;
import java.util.jar.Attributes;


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

    // Creating Tables
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


    // code to get all Scores as a list if ordered descending by the score
    public List<Score> getAllScores() {
        List<Score> contactList = new ArrayList<Score>();
        // Select All Query
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
    // Getting contacts Count
    public Score getHighScore() {
        Score score = getAllScores().size()>0 ? getAllScores().get(0) : null;
        return score;
    }

}
