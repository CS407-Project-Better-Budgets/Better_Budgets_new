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
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class manage_data_amount extends AppCompatActivity {
    public static ArrayList<Spending> spendings = new ArrayList<>();
    public static ArrayList<Spending> spendings_amount = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_data_amount);
    }

    public void searchOnClick(View view){
        spendings_amount.clear();
        EditText low_temp = findViewById(R.id.editText_md_amount_lower);
        String low = low_temp.getText().toString();
        double lower_value= Double.parseDouble(low);

        EditText high_temp = findViewById(R.id.editText_md_amount_higher);
        String high = high_temp.getText().toString();
        double higher_value= Double.parseDouble(high);

        if (higher_value >= lower_value) {
            Context context = getApplicationContext();
            SQLiteDatabase sqLiteDatabase = context.openOrCreateDatabase("spending", Context.MODE_PRIVATE, null);

            DBHelper dbHelper = new DBHelper(sqLiteDatabase);

            spendings = dbHelper.showData_all();

            ArrayList<String> displaySpendings = new ArrayList<>();
            ArrayList<Spending> filteredSpendings = new ArrayList<>();

            for (Spending spending: spendings) {
                if ((spending.getAmount() <= higher_value) && (spending.getAmount() >= lower_value)) {
                    displaySpendings.add(String.format("id:%s\nDate:%s\nSource:%s\nAmount:%f\nSeller:%s", spending.getId(), spending.getDate(), spending.getSource(), spending.getAmount(), spending.getSeller()));
                    spendings_amount.add(spending);
                }
            }

            ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, displaySpendings);
            ListView listView = (ListView) findViewById(R.id.list_md_amount_spending);
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(getApplicationContext(), manage_data_amount_edit.class);
                    intent.putExtra("spendingid", position);

                    startActivity(intent);
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.manage_data_menu, menu);
        return true;
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
