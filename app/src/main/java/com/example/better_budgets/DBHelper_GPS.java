package com.example.better_budgets;

import android.database.sqlite.SQLiteDatabase;

public class DBHelper_GPS {
    SQLiteDatabase sqLiteDatabase;
    public DBHelper_GPS(SQLiteDatabase sqLiteDatabase){
        this.sqLiteDatabase = sqLiteDatabase;
        createTable();
    }

    //database setup
    public void createTable() {
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS danger_zones" + " (name TEXT PRIMARY KEY, address TEXT, latitude FLOAT, longitude FLOAT)");

    }

    public void addData(String name, String address, float latitude, float longitude){
        createTable();
        sqLiteDatabase.execSQL(String.format("INSERT INTO danger_zones (name, address, latitude, longitude) VALUES ('%s','%s','%f','%f')", name, address, latitude, longitude));
    }


    public void deleteData(String name){
        createTable();
        sqLiteDatabase.execSQL(String.format("DELETE FROM danger_zones WHERE name = '%s'", name));
    }
    public void updateData(String name, String address, float latitude, float longitude){
        createTable();
        sqLiteDatabase.execSQL(String.format("UPDATE danger_zones set address = '%s' , latitude = '%f', longitude = '%f' where name = '%s'", address, latitude, longitude,name));
    }
}
