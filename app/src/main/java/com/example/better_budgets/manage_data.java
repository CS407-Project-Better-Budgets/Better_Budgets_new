package com.example.better_budgets;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class manage_data extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_data);
    }

    public void allOnClick(View view){
        Intent intent_all = new Intent(this, manage_data_all.class);
        startActivity(intent_all);
    }

    public void idOnClick(View view){
        Intent intent_id = new Intent(this, manage_data_id.class);
        startActivity(intent_id);
    }

    public void sourceOnClick(View view){
        Intent intent_source = new Intent(this, manage_data_source.class);
        startActivity(intent_source);
    }

    public void dateOnClick(View view){
        Intent intent_date = new Intent(this, manage_data_date.class);
        startActivity(intent_date);
    }

    public void amountOnClick(View view){
        Intent intent_amount = new Intent(this, manage_data_amount.class);
        startActivity(intent_amount);
    }

    public void sellerOnClick(View view){
        Intent intent_seller = new Intent(this, manage_data_seller.class);
        startActivity(intent_seller);
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
