package com.example.better_budgets;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class danger_zones extends AppCompatActivity {

    public static ArrayList<Location> locations = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danger_zones);


        Context context = getApplicationContext();
        SQLiteDatabase sqLiteDatabase = context.openOrCreateDatabase("danger_zones",
                Context.MODE_PRIVATE, null);

        DBHelper_GPS dbHelper_gps = new DBHelper_GPS(sqLiteDatabase);
        dbHelper_gps.createTable();
        locations = dbHelper_gps.readLocations();

        ArrayList<String> displayLocations = new ArrayList<>();
        for (Location location : locations) {
            displayLocations.add(String.format("Name: %s\nAddress: %s",
                    location.getName(), location.getAddress()));
        }

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, displayLocations);
        final ListView listView = (ListView) findViewById(R.id.locationList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), danger_zone_entry.class);
                intent.putExtra("locationID", position);
                startActivity(intent);
            }
        });

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.danger_zone_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.go_home:
                goToMainActivity();
                return true;
            case R.id.add_danger_zone:
                goToDangerZoneEntry();
                return true;
            default: return super.onOptionsItemSelected(item);
        }

    }



    public void goToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);

        startActivity(intent);
    }

    public void goToDangerZoneEntry() {
        Intent intent = new Intent(this, danger_zone_entry.class);

        intent.putExtra("locationID", -1);

        startActivity(intent);
    }
}
