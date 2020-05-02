package com.example.better_budgets;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;




public class DBHelper {
    // this class is not an activity
    //this class is designed to make database management easier by collecting the codes related
    //to database together
    SQLiteDatabase sqLiteDatabase;
    public DBHelper(SQLiteDatabase sqLiteDatabase){
        this.sqLiteDatabase = sqLiteDatabase;
        createTable();
    }

    //database setup
    public void createTable() {
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS spending " + "(id TEXT PRIMARY KEY, source TEXT, date TEXT, amount FLOAT, seller TEXT)");

    }

    public void addData(String id, String source, String date, float amount, String seller){
        createTable();
        sqLiteDatabase.execSQL(String.format("INSERT INTO spending (id, source, date, amount, seller) VALUES ('%s','%s','%s','%f','%s')", id, source, date, amount, seller));
    }

    public void deleteData(String id){
        createTable();
        sqLiteDatabase.execSQL(String.format("DELETE FROM spending WHERE id = %s", id));
    }
    public void updateData(String id, String source, String date, float amount, String seller){
        createTable();
        sqLiteDatabase.execSQL(String.format("UPDATE spending set source = '%s', date = '%s' , amount = '%s', seller = '%f' where id = '%s'", source, date, amount, seller, id));
    }
}
