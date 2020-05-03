package com.example.better_budgets;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;
import org.apache.commons.io.IOUtils;
import org.codehaus.jackson.map.ObjectMapper;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class danger_zone_entry extends AppCompatActivity {

    int locationID = -1;
    String original_name = "";

    private static final String URL = "https://maps.googleapis.com/maps/api/geocode/json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danger_zone_entry);

        EditText editName = (EditText) findViewById(R.id.editName);
        EditText editStreet = (EditText) findViewById(R.id.editStreet);
        EditText editState = (EditText) findViewById(R.id.editState);
        EditText editCity = (EditText) findViewById(R.id.editCity);
        EditText editCountry = (EditText) findViewById(R.id.editCountry);
        EditText editZip = (EditText) findViewById(R.id.editZip);
        Intent intent = getIntent();
        locationID = intent.getIntExtra("locationID", -1);

        if (locationID != -1) {
            Location location = danger_zones.locations.get(locationID);
            String locationName = location.getName();
            original_name = location.getName();
            String locationAddress = location.getAddress();
            editName.setText(locationName);
            String[] addresses = locationAddress.split(", ");
            editStreet.setText(addresses[0]);
            editCity.setText(addresses[1]);
            editState.setText(addresses[2]);
            editCountry.setText(addresses[3]);
            editZip.setText(addresses[4]);
        }
    }

    public void onSavePressed(View view) {
        EditText editName = (EditText) findViewById(R.id.editName);
        EditText editStreet = (EditText) findViewById(R.id.editStreet);
        EditText editState = (EditText) findViewById(R.id.editState);
        EditText editCity = (EditText) findViewById(R.id.editCity);
        EditText editCountry = (EditText) findViewById(R.id.editCountry);
        EditText editZip = (EditText) findViewById(R.id.editZip);

        String name = editName.getText().toString();
        String[] addresses = {editStreet.getText().toString().trim(),
        editCity.getText().toString().trim(), editState.getText().toString().trim(),
        editCountry.getText().toString().trim(), editZip.getText().toString().trim()};

        danger_zone_entry_error_dialog error_dialog = new danger_zone_entry_error_dialog();

        Context context = getApplicationContext();
        SQLiteDatabase sqLiteDatabase = context.openOrCreateDatabase("danger_zones", Context.MODE_PRIVATE, null);
        DBHelper_GPS dbHelper_gps = new DBHelper_GPS(sqLiteDatabase);


        for (int i = 0; i < 5; i++) {
            if (addresses[i].length() == 0) {
                error_dialog.show(getSupportFragmentManager(), "error no entry dialog");
                return;
            }
            for (int j = 0; j < addresses[i].length(); j++) {
                if (addresses[i].charAt(j) == ',') {
                    error_dialog.show(getSupportFragmentManager(), "error comma dialog");
                    return;
                }
            }
        }
        String address = addresses[0] + ", " + addresses[1]
                + ", " + addresses[2] + ", " + addresses[3]
                + ", " + addresses[4];
        float latitude = 0;
        float longitude = 0;
//        try {
//            GoogleResponse response = convertToLatLong(address);
//            Result[] results = response.getResults();
//            Geometry geometry = results[0].getGeometry();
//            LatitudeLongitude latitudeLongitude = geometry.getLocation();
//            latitude = Float.valueOf(latitudeLongitude.getLat());
//            longitude = Float.valueOf(latitudeLongitude.getLng());
//        } catch (IOException e) {
//            error_dialog.show(getSupportFragmentManager(), "error IO dialog");
//            return;
//        }
        Geocoder coder = new Geocoder(this);
        List<Address> new_address;

        try {
            new_address = coder.getFromLocationName(address, 5);
            if (new_address == null) {
                return;
            }
            Address location = new_address.get(0);
            latitude = (float) location.getLatitude();
            longitude = (float) location.getLongitude();
        } catch (IOException e) {
            error_dialog.show(getSupportFragmentManager(), "error IO dialog");
            return;
        }



        if (locationID != -1) {
            if (name.equals(original_name)) {
                dbHelper_gps.updateData(name, address, latitude, longitude);
            } else {
                dbHelper_gps.deleteData(original_name);
                dbHelper_gps.addData(name, address, latitude, longitude);
            }
        } else {
            dbHelper_gps.addData(name, address, latitude, longitude);
        }

        Intent intent = new Intent(getApplicationContext(), danger_zones.class);
        startActivity(intent);


    }

    public void onDeletePressed(View view) {
        Context context = getApplicationContext();
        SQLiteDatabase sqLiteDatabase = context.openOrCreateDatabase("danger_zones",
                Context.MODE_PRIVATE, null);
        DBHelper_GPS dbHelper_gps = new DBHelper_GPS(sqLiteDatabase);
        if (locationID != -1) {
            dbHelper_gps.deleteData(original_name);
        }
        Intent intent = new Intent(getApplicationContext(), danger_zones.class);
        startActivity(intent);
    }



    public GoogleResponse convertToLatLong(String fullAddress) throws IOException {

        /*
         * Create an java.net.URL object by passing the request URL in
         * constructor. Here you can see I am converting the fullAddress String
         * in UTF-8 format. You will get Exception if you don't convert your
         * address in UTF-8 format. Perhaps google loves UTF-8 format. :) In
         * parameter we also need to pass "sensor" parameter. sensor (required
         * parameter) — Indicates whether or not the geocoding request comes
         * from a device with a location sensor. This value must be either true
         * or false.
         */
        URL url = new URL(URL + "?address="
                + URLEncoder.encode(fullAddress, "UTF-8") + "&sensor=true"
                + "&key=AIzaSyB2hems1rSm_iPexeOqx-dPEZSSnLNTTvg");
        // Open the Connection
        URLConnection conn = url.openConnection();

        InputStream in = conn.getInputStream();
        ObjectMapper mapper = new ObjectMapper();
        GoogleResponse response = (GoogleResponse)mapper.readValue(in,GoogleResponse.class);
        in.close();
        return response;


    }
}
