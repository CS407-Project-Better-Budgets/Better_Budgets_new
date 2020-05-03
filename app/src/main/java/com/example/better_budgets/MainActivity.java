package com.example.better_budgets;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.Manifest;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class MainActivity extends AppCompatActivity {
    public static Boolean danger_zone = true;
    public SQLiteDatabase sqLiteDatabase;
    public DBHelper helper;
    private NotificationManagerCompat notificationManager;
    private ArrayList<Location> locations = new ArrayList<>();
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private LatitudeLongitude currentLocation = new LatitudeLongitude();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        notificationManager = NotificationManagerCompat.from(this);

        Context context = getApplicationContext();
        sqLiteDatabase = context.openOrCreateDatabase("spending", Context.MODE_PRIVATE, null);
        helper = new DBHelper(sqLiteDatabase);
        TextView summary = (TextView)findViewById(R.id.summary);
        summary.setText(make_summary());

        //the switch for danger zone
        final Button button = findViewById(R.id.danger_zone_switch);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if(danger_zone){
                    enteredDangerZone();
                }
                else{
                    TextView danger_switch_text = (TextView)findViewById(R.id.danger_switch_text);
                    danger_switch_text.setText("Danger zone function: off");
                }
            }
        });

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
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

    public void sendOnChannel1() {
        String title = "You have entered a Danger Zone";
        String message = "Watch out! You are in a Danger Zone!";
        Intent activityIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this,
                0, activityIntent, 0);

        Intent broadcastIntent = new Intent(this, NotificationReceiver.class);
        broadcastIntent.putExtra("toastMessage", message);
        PendingIntent actionIntent = PendingIntent.getBroadcast(this,
                0, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);


        Notification notification = new NotificationCompat.Builder(this, "channel1")
                .setSmallIcon(R.drawable.ic_priority_high_black_24dp)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setColor(Color.BLUE)
                .setContentIntent(contentIntent)
                .setAutoCancel(true)
                .setOnlyAlertOnce(true)
                .addAction(R.mipmap.ic_launcher, "Go To Better_Budgets", actionIntent)
                .build();

        notificationManager.notify(1, notification);
    }

    private void enteredDangerZone() {
        int permission = ActivityCompat.checkSelfPermission(this.getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            mFusedLocationProviderClient.getLastLocation().addOnCompleteListener(this, task -> {
                android.location.Location mLastKnownLocation = task.getResult();
                if (task.isSuccessful() && mLastKnownLocation != null) {
                    currentLocation.setLat(String.valueOf(mLastKnownLocation.getLatitude()));
                    currentLocation.setLng(String.valueOf(mLastKnownLocation.getLongitude()));
                }
            });
            Context context = getApplicationContext();
            SQLiteDatabase sqLiteDatabase = context.openOrCreateDatabase("danger_zones",
                    Context.MODE_PRIVATE, null);

            DBHelper_GPS dbHelper_gps = new DBHelper_GPS(sqLiteDatabase);
            dbHelper_gps.createTable();
            locations = dbHelper_gps.readLocations();
            for (Location location : locations) {
                if (currentLocation.getLat() == null ||
                        currentLocation.getLng() == null) {
                    return;
                }
                if (Math.abs(distance(Double.parseDouble(currentLocation.getLat()),
                        Double.parseDouble(currentLocation.getLng()),
                        (double) location.getLatitude(), (double) location.getLongitude(), "M")) < 0.5) {
                    sendOnChannel1();
                }
            }
        }
    }

    public static float distance(double lat1, double lon1, double lat2,
                                 double lon2, String unit) {
        if ((lat1 == lat2) && (lon1 == lon2)) {
            return 0;
        }
        else {
            double theta = lon1 - lon2;
            double dist = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2))
                    + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                    * Math.cos(Math.toRadians(theta));
            dist = Math.acos(dist);
            dist = Math.toDegrees(dist);
            dist = dist * 60 * 1.1515;
            if (unit.equals("K")) {
                dist = dist * 1.609344;
            } else if (unit.equals("N")) {
                dist = dist * 0.8684;
            }
            return (float) (dist);
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                enteredDangerZone();
            }
        }
    }


}
