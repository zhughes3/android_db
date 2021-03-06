package com.zh.cw4;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    SQLiteDatabase db;
    Button findButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createDB();
        setContentView(R.layout.activity_main);
    }

    protected void createDB() {
        db = this.openOrCreateDatabase("CW4", Context.MODE_PRIVATE, null);

        db.execSQL("DROP TABLE IF EXISTS Photos;");
        db.execSQL("DROP TABLE IF EXISTS Tags;");

        db.execSQL("CREATE TABLE IF NOT EXISTS Photos (" +
                " id INTEGER PRIMARY KEY AUTOINCREMENT," +
                " location TEXT NOT NULL UNIQUE," +
                " size INTEGER DEFAULT 0" +
                ") ;");

        db.execSQL("CREATE TABLE IF NOT EXISTS Tags (" +
        "id INTEGER NOT NULL," +
        "tag TEXT NOT NULL," +
                "FOREIGN KEY (id) REFERENCES Photos(id)" +
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
        db.execSQL("INSERT INTO Photos (location, size) VALUES ( '" + location + "', " + size + ");");
    }

    protected void insertTag(SQLiteDatabase db, int id, String tag) {
        db.execSQL("INSERT INTO Tags VALUES ( " + id + ", '" + tag + "');");
    }

    private void addResults(Cursor c, TextView result) {
        StringBuffer sb = new StringBuffer();
        try {
            while (c.moveToNext()) {
                for (int i = 0; i < c.getColumnCount(); i++) {
                    sb.append(c.getString(i) + "\t");
                }
                sb.append("\n");
            }
        } finally {
            c.close();
        }
        result.setText(sb.toString());
    }

    public void query(View view) {
        TextView result = (TextView) findViewById(R.id.result);
        EditText size = (EditText) findViewById(R.id.size);
        EditText tag = (EditText) findViewById(R.id.tag);

        Editable _size = size.getText();
        Editable _tag = tag.getText();

        /**
         * If tag and size both are entered, query the database to find the images
         *     that have that exact tag and the exact size mentioned in the text views.
         * If only a tag is entered, show all images having the specified tag.
         * If only a size is entered, show all the images having the specified size.
         */
        if (_size.length() > 0 && _tag.length() > 0) {
            String[] selectionArgs = new String[2];
            selectionArgs[0] = size.getText().toString();
            selectionArgs[1] = tag.getText().toString();
            Cursor c = db.rawQuery("SELECT location FROM Photos WHERE size= ? AND id IN " +
                    "(SELECT id FROM Tags WHERE tag=?);", selectionArgs);
            addResults(c, result);
        } else if (_size.length() > 0 && _tag.length() == 0) {
            String[] selectionArgs = new String[1];
            selectionArgs[0] = size.getText().toString();
            Cursor c = db.rawQuery("SELECT location FROM Photos where size = ?;", selectionArgs);
            addResults(c, result);
        } else if (_size.length() == 0 && _tag.length() > 0) {
            String[] selectionArgs = new String[1];
            selectionArgs[0] = tag.getText().toString();
            Cursor c = db.rawQuery("SELECT location FROM Photos WHERE id IN " +
                    "(SELECT id FROM Tags WHERE tag=?);", selectionArgs);
            addResults(c, result);
        }

        _size.clear();
        _tag.clear();
    }
}
