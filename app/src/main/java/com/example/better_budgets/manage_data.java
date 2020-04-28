package com.example.better_budgets;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class manage_data extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_data);
    }

    public void allOnClick(View view){
        Intent intent = new Intent(this, manage_data_all.class);
        startActivity(intent);
    }

    public void idOnClick(View view){
        Intent intent = new Intent(this, manage_data_id.class);
        startActivity(intent);
    }

    public void sourceOnClick(View view){
        Intent intent = new Intent(this, manage_data_source.class);
        startActivity(intent);
    }

    public void dateOnClick(View view){
        Intent intent = new Intent(this, manage_data_date.class);
        startActivity(intent);
    }

    public void amountOnClick(View view){
        Intent intent = new Intent(this, manage_data_amount.class);
        startActivity(intent);
    }

    public void sellerOnClick(View view){
        Intent intent = new Intent(this, manage_data_seller.class);
        startActivity(intent);
    }
}
