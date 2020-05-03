package com.example.better_budgets;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

public class import_home extends AppCompatActivity {

    //the home page for importing data

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_home);
    }

    public void back_main (View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void check_submit(View view){
        RadioGroup rdgrp = (RadioGroup)findViewById(R.id.rad);
        if(rdgrp.getCheckedRadioButtonId() == -1){
            Toast.makeText(getApplicationContext(),"Please select one input", Toast.LENGTH_SHORT);
        }
        else{
            if(R.id.credit_card == rdgrp.getCheckedRadioButtonId()){
                Intent intent = new Intent(this, credit_card_input.class);
                startActivity(intent);
            }
            else if (R.id.manual == rdgrp.getCheckedRadioButtonId()){
                Intent intent = new Intent(this, manual_input.class);
                startActivity(intent);
            }

        }
    }
}
