package com.example.diogo.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private int duration = Toast.LENGTH_SHORT;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Context context = getApplicationContext();
        CharSequence text = "onCreate";
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();

        LocationManager locationManager =
                (LocationManager) getSystemService
                        (Context.LOCATION_SERVICE);

        Location location =
                locationManager.getLastKnownLocation("gps");

        TextView tv = new TextView(this);
        tv.setText("Location is " + location.getLatitude()
                + ", " + location.getLongitude() );
        setContentView(tv);
    }
    @Override
    protected void onStart(){
        super.onStart();
        CharSequence text = "onStart";
        Context context = getApplicationContext();
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }
    @Override
    protected void onRestart(){
        super.onRestart();
        CharSequence text = "onRestart";
        Context context = getApplicationContext();
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }
    @Override
    protected void onResume(){
        super.onResume();
        CharSequence text = "onResume";
        Context context = getApplicationContext();
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }
    @Override
    protected void onPause(){
        super.onPause();
        CharSequence text = "onPause";
        Context context = getApplicationContext();
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }
    @Override
    protected void onStop(){
        super.onStop();
        CharSequence text = "onStop";
        Context context = getApplicationContext();
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        CharSequence text = "onDestroy";
        Context context = getApplicationContext();
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
