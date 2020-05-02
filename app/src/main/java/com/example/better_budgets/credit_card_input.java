package com.example.better_budgets;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;

public class credit_card_input extends AppCompatActivity {
    //this module will read message from bank
    SQLiteDatabase sqLiteDatabase;
    ArrayList<String> months;
    String phone_number = "";
    String bank_name = "";
    //ArrayList<String> prepared_query;
    ArrayList<Spending> checked_spd_list;

    public void start_collecting(View view){
        //Toast.makeText(this, "Your APP sucks",Toast.LENGTH_LONG).show();
        EditText phone_view = (EditText) findViewById(R.id.phone_input);
        phone_number = phone_view.getText().toString();
        EditText year_entry = (EditText) findViewById(R.id.year_entry);
        int year = Integer.parseInt(year_entry.getText().toString());
        EditText month_entry = (EditText) findViewById(R.id.month_entry);
        int month = Integer.parseInt(month_entry.getText().toString());
        EditText day_entry = (EditText) findViewById(R.id.day_entry);
        int day = Integer.parseInt(day_entry.getText().toString());
        //Toast.makeText(this, "Your APP sucks",Toast.LENGTH_LONG).show();
        read_msg(phone_number, year, month, day);

        show_data(read_msg(phone_number, year, month, day));
    }

    public void back_last(View view){

    }

    public ArrayList<Spending> read_msg(String phone_num, int year_be, int month_be, int day_be){
        //boolean val = false;
        String msg;
        ArrayList <String> msg_list = obtainPhoneMessage(phone_num);
        ArrayList <Spending> spd_list = new ArrayList<Spending>();
        //Toast.makeText(this, msg_list.get(0),Toast.LENGTH_LONG).show();
        //Toast.makeText(this, "Your APP sucks",Toast.LENGTH_LONG).show();

        //ArrayList <Spending> spd_list = new ArrayList<Spending>();
        for (int i = 0; i < msg_list.size(); i ++){
            msg = msg_list.get(i);
            Scanner scan = new Scanner(msg);
            String id = "";
            String date = "";
            String seller = "";
            double amount = 0.00;
            while(scan.hasNext()){
                String curr = scan.next();
                if(curr.substring(0,1).equals("$")){
                    amount = Double.parseDouble(curr.substring(1));
                    //Toast.makeText(this,Double.toString(amount), Toast.LENGTH_SHORT).show();
                }
                else if(curr.equals("at")){
                    String next = "";
                    next = scan.next();
                    while(!next.equals("was")){

                        seller += next;
                        next = scan.next();
                        if(!next.equals("was")){
                            seller += "_";
                        }
                    }
                    //Toast.makeText(this,seller, Toast.LENGTH_SHORT).show();
                }
                else if (curr.equals("on")){
                    String monthName = scan.next();
                    String month = Integer.toString(months.indexOf(monthName)+1);
                    if(months.indexOf(monthName) < 9){
                        month = "0"+month;
                    }
                    if(months.indexOf(monthName) == -1){
                        continue;
                    }
                    String day = scan.next().substring(0,2);
                    String year = scan.next().substring(0,4);
                    if(Integer.parseInt(year) < year_be){
                        continue;
                    }
                    else if(Integer.parseInt(year) == year_be){
                        if(Integer.parseInt(month) < month_be){
                            continue;
                        }
                        else if (Integer.parseInt(month) == month_be){
                            if(Integer.parseInt(day) < day_be){
                                continue;
                            }
                        }
                    }
                    date = month+day+year;
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                    LocalDateTime now = LocalDateTime.now();
                    id = dtf.format(now).replaceAll(" ", "")+Integer.toString(i);
                    spd_list.add(new Spending(id, date,"credit_card",amount,seller));
                    //Toast.makeText(this, spendingtoString(spd_list.get(0)), Toast.LENGTH_SHORT).show();
                }

                //Toast.makeText(this,Double.toString(amount), Toast.LENGTH_SHORT).show();
            }

        }


    //Toast.makeText(this,"APP",Toast.LENGTH_LONG);
        //Toast.makeText(this,Integer.toString(spd_list.size()),Toast.LENGTH_LONG);
    return spd_list;
    }

    public String spendingtoString(Spending spd){
        if(spd == null){
            return "";
        }
        else{
            return String.format("%s, %.2f, %s", spd.getSeller(), spd.getAmount(), spd.getDate());
        }
    }

    public void show_data(ArrayList<Spending> spd_list){
        checked_spd_list = new ArrayList<Spending>();
        Toast.makeText(this,Integer.toString(spd_list.size()),Toast.LENGTH_LONG).show();
        for (int i = 0; i < spd_list.size(); i ++) {
            Spending spd = spd_list.get(i);
            Cursor cursor = sqLiteDatabase.rawQuery(String.format("SELECT id FROM spending WHERE date = '%s' AND source = '%s' AND amount = '%s'AND seller = '%s'", spd.getDate(), spd.getSource(), spd.getAmount(), spd.getSeller()), null);
            /*
            if(cursor != null){
                continue;
            }
            */

            checked_spd_list.add(spd);
            //Toast.makeText(this,"zz",Toast.LENGTH_LONG).show();
        }

        if(checked_spd_list.isEmpty()){
            Toast.makeText(this,"No data found",Toast.LENGTH_LONG).show();
            return;
        }
        SharedPreferences sharedPreferences = getSharedPreferences("com.example.better_budgets", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        for (int i = 0; i < checked_spd_list.size(); i++){

            editor.putString("display"+Integer.toString(i),spendingtoString(checked_spd_list.get(i)));
            editor.apply();
        }
        //Toast.makeText(this,"aaaa",Toast.LENGTH_LONG).show();
        //String mee = sharedPreferences.getString("display0","xxxxx");
        //Toast.makeText(this,mee,Toast.LENGTH_LONG).show();

        Intent intent = new Intent(getApplicationContext(), credit_card_input_list.class);
        intent.putExtra("size", checked_spd_list.size());
        startActivity(intent);



    }



    private Uri SMS_INBOX = Uri.parse("content://sms/inbox");

    private ArrayList<String> obtainPhoneMessage(String phone_num) {
        //ArrayList<String> date_list = new ArrayList<String>();
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_SMS},
                    1);

        }
       // Toast.makeText(this, "Your APP sucks",Toast.LENGTH_LONG).show();


        ArrayList<String> msg_list = new ArrayList<String>();
        Cursor cursor = getContentResolver().query(Uri.parse("content://sms/inbox"), null, null, null, null);
        //Toast.makeText(this, "Your APP sucks",Toast.LENGTH_LONG).show();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String number = cursor.getString(cursor.getColumnIndex("address"));
            String body = cursor.getString(cursor.getColumnIndex("body"));
            if(number.equals(phone_num)){
                msg_list.add(body);
                //Toast.makeText(this,body,Toast.LENGTH_LONG).show();
            }
            cursor.moveToNext();
        }
        //Toast.makeText(this,"FUCK",Toast.LENGTH_LONG).show();
        //Toast.makeText(this,Integer.toString(msg_list.size()),Toast.LENGTH_LONG).show();
        return msg_list;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        months = new ArrayList<String>(){
            {
                add("January");
                add("February");
                add("March");
                add("April");
                add("May");
                add("June");
                add("July");
                add("August");
                add("September");
                add("October");
                add("November");
                add("December");
            }

        };
        Context context = getApplicationContext();
        sqLiteDatabase = context.openOrCreateDatabase("spending", Context.MODE_PRIVATE, null);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_card_input);
    }
}
