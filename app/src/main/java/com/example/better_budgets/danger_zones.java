package com.example.better_budgets;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class danger_zones extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danger_zones);
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

        intent.putExtra("locationID", "");

        startActivity(intent);
    }
}
