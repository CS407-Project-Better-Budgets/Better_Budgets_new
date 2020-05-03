package com.example.better_budgets;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.view.View;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class analysis_module extends AppCompatActivity {

    //this module will give users some useful analysis of their spending
    //this module is quite open-use your creativity!

    AnyChartView anyChartView;

    public static ArrayList<Spending> spendings = new ArrayList<>();
    //ArrayList <String> months = new ArrayList<>();
    //ArrayList <Integer> spent = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis_module);

        anyChartView = findViewById(R.id.any_chart_view);

        setupPieChart();

    }

    public void setupPieChart(){
        Pie pie = AnyChart.pie();
        List<DataEntry> dataEntries = new ArrayList<>();

        Context context = getApplicationContext();
        SQLiteDatabase sqLiteDatabase = context.openOrCreateDatabase("spending", Context.MODE_PRIVATE, null);
        DBHelper dbHelper = new DBHelper(sqLiteDatabase);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
        Date date = new Date();
        String id = sdf.format(date);

        spendings = new DBHelper(sqLiteDatabase).

       // ArrayList<String> displayNotes = new ArrayList<>();
        for (Spending spending : spendings) {
            dataEntries.add(new ValueDataEntry(spending.getSeller(), spending.getAmount()));
            //displayNotes.add(String.format("Title:%s\nDate:%s", note.getTitle(), note.getDate()));
        }

       // for(int i = 0; i < months.length; i++){
         //   dataEntries.add(new ValueDataEntry(months[i], spending[i]));
       // }

        pie.data(dataEntries);
        anyChartView.setChart(pie);


    }

    public void back_main (View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}





















