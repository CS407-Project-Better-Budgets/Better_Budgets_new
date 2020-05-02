package com.example.better_budgets;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    public static Boolean danger_zone = false;
    public SQLiteDatabase sqLiteDatabase;
    public DBHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Context context = getApplicationContext();
        sqLiteDatabase = context.openOrCreateDatabase("spending", Context.MODE_PRIVATE, null);
        helper = new DBHelper(sqLiteDatabase);
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

    public String getDate(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date).substring(0,11).replaceAll("/","-");
    }

    public String make_summary(){
        String summary = "";
        //!!!!possible problems: Float and double
        //to do: get the spending data from database and build the string
        //get total spending

        String year = getDate().substring(0,4);
        String month = getDate().substring(5,7);
        //Toast.makeText(this,year+" "+month, Toast.LENGTH_LONG).show();
        //Toast.makeText(this,year+month,Toast.LENGTH_LONG).show();
        //Cursor c = sqLiteDatabase.rawQuery(String.format("SELECT * from spending"), null);
        Cursor c = sqLiteDatabase.rawQuery(String.format("SELECT * from spending"), null);
        int idIndex = c.getColumnIndex("id");
        int sourceIndex = c.getColumnIndex("source");
        int dateIndex = c.getColumnIndex("date");
        int amountIndex = c.getColumnIndex("amount");
        int sellerIndex = c.getColumnIndex("seller");

        c.moveToFirst();
        ArrayList<Spending> datalist = new ArrayList<>();

        while ( (!c.isAfterLast())){
            String id = c.getString(idIndex);
            String source = c.getString(sourceIndex);
            String date = c.getString(dateIndex);
            //Toast.makeText(this,date, Toast.LENGTH_LONG).show();
            double amount = c.getDouble(amountIndex);
            String seller = c.getString(sellerIndex);
            if(!date.substring(4).equals(year)){
                c.moveToNext();
                continue;
            }
            if(!date.substring(0,2).equals(month)){
                c.moveToNext();
                continue;
            }
            Spending spending = new Spending(id,date,source,amount,seller);
            datalist.add(spending);
            c.moveToNext();
        }
        if(datalist.size()==0){
            summary = String.format("Total Spending this month: $%d \nYour top spending: %s", 0, "N/A");

            return summary;
        }
        c.close();
        double total = 0;
        ArrayList<Double> spending = new ArrayList<Double>();
        ArrayList<String> sellers = new ArrayList<String>();
        for (int i = 0; i < datalist.size(); i ++){
            total += datalist.get(i).getAmount();
            int index = sellers.indexOf(datalist.get(i).getSeller());
            if(index != -1){
                spending.set(index,new Double(spending.get(index).doubleValue() + datalist.get(i).getAmount()));
            }
            else{
                spending.add(new Double(datalist.get(i).getAmount()));
                sellers.add(datalist.get(i).getSeller());
            }

        }
        double max_amount = spending.get(0).doubleValue();
        for (int j = 0; j < spending.size(); j++){
            if (max_amount < spending.get(j)){
                max_amount = spending.get(j);
            }
        }
        String max_seller = sellers.get(spending.indexOf(new Double(max_amount)));


        summary = String.format("Total Spending this month: $%.2f \nYour top spending: %s", total, max_seller);
        return summary;


    }


}
