package io.github.qianniancc.diary.helper;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBHelper {

    public static void getDiaryList(){

        SQLiteDatabase db= SQLiteDatabase.openOrCreateDatabase("diary.db",null);
        Cursor c=db.rawQuery("SELECT * FROM diary",null);

    }

}
