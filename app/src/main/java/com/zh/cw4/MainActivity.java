package com.zh.cw4;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createDB();
        setContentView(R.layout.activity_main);
    }

    protected void createDB() {
        SQLiteDatabase db = this.openOrCreateDatabase("CW4", Context.MODE_PRIVATE, null);


        db.execSQL("CREATE TABLE IF NOT EXISTS Photos (" +
                " id INTEGER AUTOINCREMENT PRIMARY KEY," +
                " location TEXT NOT NULL," +
                " size INTEGER DEFAULT 0" +
                ") ;");

        db.execSQL("CREATE TABLE IF NOT EXISTS Tags (" +
        "FOREIGN KEY(id) REFERENCES Photos(id)," +
        "tag TEXT NOT NULL" +
        ") ;" );

        insertPhoto(db, "sdcard/p1.jpg", 100);
        insertPhoto(db, "sdcard/p2.jpg", 200);
        insertPhoto(db, "sdcard/p3.jpg", 300);
        insertPhoto(db, "sdcard/p4.jpg", 200);

        insertTag(db, 1, "Old Well");
        insertTag(db, 1, "UNC");
        insertTag(db, 2, "Sitterson");
        insertTag(db, 2, "Building");
        insertTag(db, 3, "Sky");
        insertTag(db, 4, "Dining");
        insertTag(db, 4, "Building");
    }

    protected void insertPhoto(SQLiteDatabase db, String location, int size) {
        db.execSQL("INSERT INTO Photos VALUES ( " + location + ", " + size + ");");
    }

    protected void insertTag(SQLiteDatabase db, int id, String tag) {
        db.execSQL("INSERT INTO Tags VALUES ( " + id + ", " + tag + ");");
    }
}
