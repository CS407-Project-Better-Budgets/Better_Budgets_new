package com.example.better_budgets;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;


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
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS spending" + " (id TEXT PRIMARY KEY, source TEXT, date TEXT, amount DOUBLE, seller TEXT)");

    }

    public void addData(String id, String source, String date, double amount, String seller){
        createTable();
        sqLiteDatabase.execSQL(String.format("INSERT INTO spending (id, source, date, amount, seller) VALUES ('%s','%s','%s','%f','%s')", id, source, date, amount, seller));
        //return String.format("INSERT INTO spending (id, source, date, amount, seller) VALUES ('%s','%s','%s','%f','%s')", id, source, date, amount, seller);
    }

    public void deleteData(String id){
        createTable();
        sqLiteDatabase.execSQL(String.format("DELETE FROM spending WHERE id = %s", id));
    }
    public void updateData(String id, String source, String date, double amount, String seller){
        createTable();
        sqLiteDatabase.execSQL(String.format("UPDATE spending set source = '%s', date = '%s' , amount = '%f', seller = '%s' where id = '%s'", source, date, amount, seller, id));
    }

    public ArrayList<Spending> showData_all() {
        createTable();

        Cursor c = sqLiteDatabase.rawQuery(String.format("SELECT * from spending"), null);

        int idIndex = c.getColumnIndex("id");
        int dateIndex = c.getColumnIndex("date");
        int sourceIndex = c.getColumnIndex("source");
        int amountIndex = c.getColumnIndex("amount");
        int sellerIndex = c.getColumnIndex("seller");


        ArrayList<Spending> spendingList = new ArrayList<>();

        if (c.moveToFirst()) {
            while (!c.isAfterLast()) {
                String id = c.getString(idIndex);
                String date = c.getString(dateIndex);
                String source = c.getString(sourceIndex);
                double amount = c.getDouble(amountIndex);
                String seller = c.getString(sellerIndex);

                Spending spending = new Spending(id, date, source, amount, seller);
                spendingList.add(spending);
                c.moveToNext();
            }
        } else {
            Spending spending = new Spending("", "", "", 0, "");
            spendingList.add(spending);
        }
        c.close();

        return spendingList;
    }
}
