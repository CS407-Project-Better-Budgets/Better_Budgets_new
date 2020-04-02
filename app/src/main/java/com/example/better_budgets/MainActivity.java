package com.example.better_budgets;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    public static Boolean danger_zone = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView summary = (TextView)findViewById(R.id.summary);

        summary.setText(make_summary());

        //the switch for danger zone
        final Button button = findViewById(R.id.danger_zone_switch);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                danger_zone = !danger_zone;
                if(danger_zone){
                    TextView danger_switch_text = (TextView)findViewById(R.id.danger_switch_text);
                    danger_switch_text.setText("Danger zone function: on");
                }
                else{
                    TextView danger_switch_text = (TextView)findViewById(R.id.danger_switch_text);
                    danger_switch_text.setText("Danger zone function: off");
                }
            }
        });
    }

    public void click_danger(View view){
        Intent intent = new Intent(this, danger_zones.class);
        startActivity(intent);
    }

    public void click_import(View view){
        Intent intent = new Intent(this, import_home.class);
        startActivity(intent);
    }

    public void click_analysis(View view){
        Intent intent = new Intent(this, analysis_module.class);
        startActivity(intent);
    }

    public void click_data(View view){
        Intent intent = new Intent(this, manage_data.class);
        startActivity(intent);
    }

    public String make_summary(){
        String summary = "awaiting for implementation";
        //to do: get the spending data from database and build the string
        return summary;
    }


}
