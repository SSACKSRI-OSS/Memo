package com.ssacksri.memo;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

public class SQLiteHelper extends SQLiteOpenHelper {



    SQLiteHelper(Context context,
                 String name,
                 SQLiteDatabase.CursorFactory factory,
                 int version){
        super(context,name,factory,version);
    }

    public void queryData(String sql){
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL(sql);
    }


    public void insertDiary(String date, String comment,byte[] image){
        SQLiteDatabase database = getWritableDatabase();
        String sql = "INSERT INTO DIARY (date,comment,image) VALUES(?,?,?)";

        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();
        statement.bindString(1,date);
        statement.bindString(2,comment);
        statement.bindBlob(3,image);

        statement.executeInsert();
    }
    public void updateDiary(String comment, byte[] image, String num){
        SQLiteDatabase database = getWritableDatabase();
        String sql = "UPDATE DIARY SET comment =? , image = ? WHERE num=?";

        SQLiteStatement statement = database.compileStatement(sql);
        statement.bindString(1,comment);
        statement.bindBlob(2,image);
        statement.bindString(3,num);

        statement.execute();
        database.close();
    }


    public void deleteDiary(String num){
        SQLiteDatabase database = getWritableDatabase();
        String sql = "DELETE FROM DIARY WHERE num =?";

        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();
        statement.bindString(1,num);
        statement.execute();
        statement.close();
    }

    public Cursor getData(String sql){
        SQLiteDatabase database = getReadableDatabase();
        return database.rawQuery(sql,null);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase){

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i,int il){

    }
}
