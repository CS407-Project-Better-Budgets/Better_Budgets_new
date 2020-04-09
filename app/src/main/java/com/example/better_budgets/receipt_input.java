package com.example.better_budgets;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

public class receipt_input extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt_input);
    }

    public void receipt_option_submit(View view){
        RadioGroup rdgrp = (RadioGroup)findViewById(R.id.rad_receipt);
        if(rdgrp.getCheckedRadioButtonId() == -1){
            Toast.makeText(getApplicationContext(),"Please select one input", Toast.LENGTH_SHORT);
        }
        else{
            if(R.id.gallery == rdgrp.getCheckedRadioButtonId()){
                Intent intent = new Intent(this, gallery_receipt.class);
                startActivity(intent);
            }
            else if (R.id.camera == rdgrp.getCheckedRadioButtonId()){
                Intent intent = new Intent(this, camera.class);
                startActivity(intent);
            }

        }
    }
}
