package com.example.better_budgets;

import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
        EditText phone_view = (EditText) findViewById(R.id.phone_input);
        phone_number = phone_view.getText().toString();
        EditText year_entry = (EditText) findViewById(R.id.year_entry);
        int year = Integer.parseInt(year_entry.getText().toString());
        EditText month_entry = (EditText) findViewById(R.id.month_entry);
        int month = Integer.parseInt(month_entry.getText().toString());
        EditText day_entry = (EditText) findViewById(R.id.day_entry);
        int day = Integer.parseInt(day_entry.getText().toString());
        show_data(read_msg(phone_number, year, month, day));
    }

    public void back_last(View view){

    }

    public ArrayList<Spending> read_msg(String phone_num, int year_be, int month_be, int day_be){
        //boolean val = false;
        String msg;
        ArrayList <String> msg_list = obtainPhoneMessage(phone_num);
        ArrayList <Spending> spd_list = new ArrayList<Spending>();
        for (int i = 0; i < msg_list.size(); i ++){
            msg = msg_list.get(i);
            Scanner scan = new Scanner(msg);
            String id = "";
            String date = "";
            String seller = "";
            float amount = 0.0f;
            while(scan.hasNext()){
                String curr = scan.next();
                if(curr.charAt(0) == '$'){
                    amount = Float.parseFloat(curr.substring(1));
                }
                else if(curr.equals("at")){
                    String next = "";
                    while(next != "was"){
                        next = scan.next();
                        seller += next;
                    }
                }
                else if (curr.equals("on")){
                    String monthName = scan.next();
                    String month = Integer.toString(months.indexOf(monthName));
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
                }
            }

        }
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
        for (int i = 0; i < spd_list.size(); i ++){
            Spending spd = spd_list.get(i);
            Cursor cursor = sqLiteDatabase.rawQuery(String.format("SELECT id FROM spending WHERE date = '%s' AND source = '%s' AND amount = '%s'AND seller = '%s'",spd.getDate(),spd.getSource(),spd.getAmount(),spd.getSeller()), null);
            if(cursor != null){
                continue;
            }
            checked_spd_list = new ArrayList<Spending>();
            checked_spd_list.add(spd);
        }
        if(checked_spd_list.size() == 0){
            Toast.makeText(this,"No data found",Toast.LENGTH_LONG).show();
            return;
        }
        SharedPreferences sharedPreferences = getSharedPreferences("com.example.better_budgets", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        for (int i = 0; i < checked_spd_list.size(); i++){
            editor.putString("display"+Integer.toString(i),spendingtoString(checked_spd_list.get(i)));
            editor.apply();
        }
        Intent intent = new Intent(getApplicationContext(), credit_card_input_list.class);
        intent.putExtra("size", checked_spd_list.size());
        startActivity(intent);



    }



    private Uri SMS_INBOX = Uri.parse("content://sms/");

    private ArrayList<String> obtainPhoneMessage(String phone_num) {
        //ArrayList<String> date_list = new ArrayList<String>();
        ArrayList<String> msg_list = new ArrayList<String>();

        ContentResolver cr = getContentResolver();
        String[] projection = new String[]{"_id", "address", "person", "body", "date", "type"};
        Cursor cur = cr.query(SMS_INBOX, projection, null, null, "date desc");
        if (null == cur) {
            Log.i("ooc", "************cur == null");
            return msg_list ;
        }
        while (cur.moveToNext()) {
            String number = cur.getString(cur.getColumnIndex("address"));
            //String name = cur.getString(cur.getColumnIndex("person"));
            String body = cur.getString(cur.getColumnIndex("body"));
            //String date = cur.getString(cur.getColumnIndex("body"));
            if (number.equals(phone_num)) {
                msg_list.add(body);

            }
        }
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
