package com.example.better_budgets;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;

public class manual_input extends AppCompatActivity {
    ArrayList <String> cost_list = new ArrayList<String>();
    ArrayList <String> seller_list = new ArrayList<String>();
    ArrayList <String> date_list = new ArrayList<String>();
    public void submit_manual(View view){
        String cost = ((EditText)this.findViewById(R.id.data_entry_manual_cost)).getText().toString();
        String seller = ((EditText)this.findViewById(R.id.data_entry_manual_seller)).getText().toString();
        String day = ((EditText)this.findViewById(R.id.data_entry_manual_day)).getText().toString();
        String year = ((EditText)this.findViewById(R.id.data_entry_manual_year)).getText().toString();
        String month = ((EditText)this.findViewById(R.id.data_entry_manual_month)).getText().toString();
        String date = month+day+year;
        if(cost=="" || seller == "" ){
            Toast.makeText(this,"entry should not be empty", Toast.LENGTH_LONG).show();
            empty_input();
        }
        if(date == ""){
            Toast.makeText(this,"entry should not be empty", Toast.LENGTH_LONG).show();
            empty_input();
        }

        try{
            Double doub = Double.parseDouble(cost);
        }
        catch(Exception e){
            try{
                Integer intg = Integer.parseInt(cost);
            }
            catch(Exception el){
                Toast.makeText(this,"cost must be a value", Toast.LENGTH_LONG).show();
                empty_input();
            }
        }
        cost_list.add(0,cost);
        seller_list.add(0,seller);
        date_list.add(0,date);
        empty_input();
        submit_input();
        Toast.makeText(this,"entry recorded", Toast.LENGTH_LONG).show();
    }
    public void empty_input (){
        EditText cost = (EditText) this.findViewById(R.id.data_entry_manual_cost);
        EditText seller = (EditText) this.findViewById(R.id.data_entry_manual_seller);
        EditText day = (EditText) this.findViewById(R.id.data_entry_manual_day);
        EditText month = (EditText) this.findViewById(R.id.data_entry_manual_month);
        EditText year = (EditText) this.findViewById(R.id.data_entry_manual_year);
        cost.setText("");
        seller.setText("");
        day.setText("");
        month.setText("");
        year.setText("");
    }

    public void submit_input (){
        Context context = getApplicationContext();
        SQLiteDatabase sqLiteDatabase = context.openOrCreateDatabase("spending", Context.MODE_PRIVATE, null);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
        DBHelper dbHelper = new DBHelper(sqLiteDatabase);
        Date date = new Date();
        String id = sdf.format(date);
        dbHelper.addData(id, "manual", date_list.get(0),Float.parseFloat(cost_list.get(0)),seller_list.get(0));
    }

    public void finish_manual(View view){
        Intent intent = new Intent(this, import_home.class);
        startActivity(intent);
    }

   public void back_manual (View view){
       Intent intent = new Intent(this, import_home.class);
       startActivity(intent);
   }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_input);
    }
}
