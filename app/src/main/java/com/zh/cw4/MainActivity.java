package com.zh.cw4;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    SQLiteDatabase db;
    Button findButton;
    TextView result;
    TextView size;
    TextView tag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createDB();
        result = (TextView) findViewById(R.id.result);
        size = (TextView) findViewById(R.id.size);
        tag = (TextView) findViewById(R.id.tag);
        setContentView(R.layout.activity_main);
    }

    protected void createDB() {
        db = this.openOrCreateDatabase("CW4", Context.MODE_PRIVATE, null);


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

    private void find() {
        String[] selectionArgs = new String[2];
        selectionArgs[0] = (String) size.getText();
        selectionArgs[1] = (String) tag.getText();

        StringBuffer sb = new StringBuffer();

        Cursor c = db.rawQuery("SELECT * FROM Photos WHERE size= ? AND tag=?;", selectionArgs);
        try {
            while (c.moveToNext()) {
                for (int i = 0; i < c.getColumnCount(); i++) {
                    sb.append(c.getString(i));
                }
                sb.append("\n");
            }
        } finally {
            c.close();
        }

        result.setText(sb.toString());
    }
}
