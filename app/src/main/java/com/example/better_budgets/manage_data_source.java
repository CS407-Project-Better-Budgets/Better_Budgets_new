package com.example.better_budgets;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class manage_data_source extends AppCompatActivity {
    public static ArrayList<Spending> spendings = new ArrayList<>();
    public static ArrayList<Spending> spendings_source = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_data_source);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.manage_data_menu, menu);
        return true;
    }

    public void manualOnClick(View view){
        spendings_source.clear();
        Context context = getApplicationContext();
        SQLiteDatabase sqLiteDatabase = context.openOrCreateDatabase("spending", Context.MODE_PRIVATE, null);

        DBHelper dbHelper = new DBHelper(sqLiteDatabase);

        spendings = dbHelper.showData_all();

        ArrayList<String> displaySpendings = new ArrayList<>();
        for (Spending spending: spendings) {
            if (spending.getSource().equalsIgnoreCase("manual")) {
                displaySpendings.add(String.format("id:%s\nDate:%s\nSource:%s\nAmount:%f\nSeller:%s", spending.getId(), spending.getDate(), spending.getSource(), spending.getAmount(), spending.getSeller()));
                spendings_source.add(spending);
            }
        }

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, displaySpendings);
        ListView listView = (ListView) findViewById(R.id.list_md_source_spending);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), manage_data_source_edit.class);
                intent.putExtra("spendingid", position);

                startActivity(intent);
            }
        });
    }

    public void messageOnClick(View view){
        spendings_source.clear();
        Context context = getApplicationContext();
        SQLiteDatabase sqLiteDatabase = context.openOrCreateDatabase("spending", Context.MODE_PRIVATE, null);

        DBHelper dbHelper = new DBHelper(sqLiteDatabase);

        spendings = dbHelper.showData_all();

        ArrayList<String> displaySpendings = new ArrayList<>();
        for (Spending spending: spendings) {
            if (spending.getSource().equalsIgnoreCase("credit_card")) {
                displaySpendings.add(String.format("id:%s\nDate:%s\nSource:%s\nAmount:%f\nSeller:%s", spending.getId(), spending.getDate(), spending.getSource(), spending.getAmount(), spending.getSeller()));
                spendings_source.add(spending);
            }
        }

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, displaySpendings);
        ListView listView = (ListView) findViewById(R.id.list_md_source_spending);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), manage_data_source_edit.class);
                intent.putExtra("spendingid", position);

                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_goto_data_management:
                goToDataManagementHome();
                return true;
            case R.id.item_goto_main_activity:
                goToMainActivity();
                return true;
            default: return super.onOptionsItemSelected(item);
        }

    }

    public void goToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void goToDataManagementHome() {
        Intent intent = new Intent(this, manage_data.class);
        startActivity(intent);
    }
}
