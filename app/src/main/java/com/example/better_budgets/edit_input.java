package com.example.better_budgets;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class edit_input extends AppCompatActivity {

    public int size = 0;

    public void change_spending(View view){
        EditText spending = (EditText) findViewById(R.id.spending_edit);
        String new_spending = spending.getText().toString();
        SharedPreferences sharedPreferences = getSharedPreferences("com.example.better_budgets", Context.MODE_PRIVATE);
        Intent intent = getIntent();
        int position = intent.getIntExtra("position", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("display"+position,new_spending);
        editor.apply();
        Toast.makeText(this,"data edited", Toast.LENGTH_LONG);
        Intent intent_new = new Intent(getApplicationContext(),credit_card_input_list.class);
        intent_new.putExtra("size",size);
        startActivity(intent_new);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences sharedPreferences = getSharedPreferences("com.example.better_budgets", Context.MODE_PRIVATE);
        Intent intent = getIntent();
        int position = intent.getIntExtra("position", 0);
        size = intent.getIntExtra("size", 0);

        EditText spending = (EditText) findViewById(R.id.spending_edit);
        spending.setText(sharedPreferences.getString("display"+position,""));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_input);
    }
}
