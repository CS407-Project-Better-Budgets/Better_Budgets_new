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
import android.widget.EditText;
import android.widget.TextView;

public class manage_data_seller_edit extends AppCompatActivity {
    Integer spendingid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_data_seller_edit);

        EditText editId = findViewById(R.id.editText_md_seller_edit_id);
        EditText editDate = findViewById(R.id.editText_md_seller_edit_date);
        EditText editSource = findViewById(R.id.editText_md_seller_edit_source);
        EditText editAmount = findViewById(R.id.editText_md_seller_edit_amount);
        EditText editSeller = findViewById(R.id.editText_md_seller_edit_seller);

        Intent intent = getIntent();
        spendingid = intent.getIntExtra("spendingid", -1);

        if (spendingid != -1) {
            Spending spending = manage_data_seller.spendings.get(spendingid);
            String id = spending.getId();
            String date = spending.getDate();
            String source = spending.getSource();
            double amount = spending.getAmount();
            String seller = spending.getSeller();

            editId.setText(id);
            editDate.setText(date);
            editSource.setText(source);
            editAmount.setText(String.valueOf(amount));
            editSeller.setText(seller);
        }
    }

    public void saveOnClick(View view) {
        EditText editId = findViewById(R.id.editText_md_seller_edit_id);
        EditText editDate = findViewById(R.id.editText_md_seller_edit_date);
        EditText editSource = findViewById(R.id.editText_md_seller_edit_source);
        EditText editAmount = findViewById(R.id.editText_md_seller_edit_amount);
        EditText editSeller = findViewById(R.id.editText_md_seller_edit_seller);

        String id = editId.getText().toString();
        String date = editDate.getText().toString();
        String source = editSource.getText().toString();
        double amount = Double.parseDouble(editAmount.getText().toString());
        String seller = editSeller.getText().toString();

        Context context = getApplicationContext();
        SQLiteDatabase sqLiteDatabase = context.openOrCreateDatabase("spending", Context.MODE_PRIVATE, null);

        DBHelper dbHelper = new DBHelper(sqLiteDatabase);

        dbHelper.updateData(id, source, date, amount, seller);
    }

    public void deleteOnClick(View view) {
        EditText editId = findViewById(R.id.editText_md_seller_edit_id);

        String id = editId.getText().toString();

        Context context = getApplicationContext();
        SQLiteDatabase sqLiteDatabase = context.openOrCreateDatabase("spending", Context.MODE_PRIVATE, null);

        DBHelper dbHelper = new DBHelper(sqLiteDatabase);

        dbHelper.deleteData(id);

        Intent intent = new Intent(this, manage_data.class);
        startActivity(intent);
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
                Intent intent = new Intent(this, manage_data.class);
                startActivity(intent);
            case R.id.item_goto_main_activity:
                Intent intent2 = new Intent(this, MainActivity.class);
                startActivity(intent2);
            default: return super.onOptionsItemSelected(item);
        }

    }
}
