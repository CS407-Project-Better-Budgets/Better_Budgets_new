package com.example.better_budgets;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class credit_card_input_list extends AppCompatActivity {


    SQLiteDatabase sqLiteDatabase;
    public void store_data(View view){
        SharedPreferences sharedPreferences = getSharedPreferences("com.example.better_budgets", Context.MODE_PRIVATE);
        Intent intent = getIntent();
        int size = intent.getIntExtra("size",0);
        DBHelper dbHelper = new DBHelper(sqLiteDatabase);
        for (int i = 0; i < size; i++){
            String entry = sharedPreferences.getString("display"+i,"");
            if(!entry.equals("")){
                entry = entry.replace(",","");
                Scanner scan = new Scanner(entry);

                String seller = scan.next();
                float amount = Float.parseFloat(scan.next());
                String date = scan.next();
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                LocalDateTime now = LocalDateTime.now();
                String id = dtf.format(now).replaceAll(" ", "")+Integer.toString(i);
                dbHelper.addData(id,"credit_card",date,amount,seller);

            }
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        Toast.makeText(this,"Your Data have been recorded",Toast.LENGTH_LONG).show();
        Intent new_intent = new Intent(getApplicationContext(),import_home.class);
        startActivity(new_intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Context context = getApplicationContext();
        sqLiteDatabase = context.openOrCreateDatabase("spending", Context.MODE_PRIVATE, null);
        ArrayList<String> list = new ArrayList<String>();
        Intent intent = getIntent();
        final int size = intent.getIntExtra("size",0);
        SharedPreferences sharedPreferences = getSharedPreferences("com.example.better_budgets", Context.MODE_PRIVATE);
        for (int i = 0; i < size; i ++){
            list.add(sharedPreferences.getString("display"+ Integer.toString(i),""));
        }
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,list);
        ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), edit_input.class);
                intent.putExtra("position", position);
                intent.putExtra("size", size);
                startActivity(intent);
            }
        });
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_card_input_list);
    }
}
