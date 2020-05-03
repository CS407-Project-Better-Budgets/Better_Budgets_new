package com.example.better_budgets;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

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

    public ArrayList<Location> readLocations() {

        createTable();
        Cursor c = sqLiteDatabase.rawQuery(String.format("select * from danger_zones"), null);

        int nameIndex = c.getColumnIndex("name");
        int addressIndex = c.getColumnIndex("address");
        int latitudeIndex = c.getColumnIndex("latitude");
        int longitudeIndex = c.getColumnIndex("longitude");

        c.moveToFirst();

        ArrayList<Location> LocationsList = new ArrayList<>();

        while (!c.isAfterLast()) {

            String name = c.getString(nameIndex);
            String address = c.getString(addressIndex);
            float latitude = c.getFloat(latitudeIndex);
            float longitude =c.getFloat(longitudeIndex);

            Location location = new Location(name, address, latitude, longitude);
            LocationsList.add(location);
            c.moveToNext();
        }
        c.close();
        sqLiteDatabase.close();

        return LocationsList;
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
