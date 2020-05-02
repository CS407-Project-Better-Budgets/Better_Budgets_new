package com.example.better_budgets;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;

import java.util.ArrayList;
import java.util.List;


public class analysis_module extends AppCompatActivity {

    //this module will give users some useful analysis of their spending
    //this module is quite open-use your creativity!

    AnyChartView anyChartView;

    String[] months = {"Jan", "Feb", "Mar"};
    int[] spending = {500, 800, 2000};

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

        for(int i = 0; i < months.length; i++){
            dataEntries.add(new ValueDataEntry(months[i], spending[i]));
        }

        pie.data(dataEntries);
        anyChartView.setChart(pie);


    }
}





















