package com.sevencrayons.compass;

import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.util.Log;
import android.widget.TextView;


public class CompassActivity extends ActionBarActivity {

    ActionBar actionBar;
    private static final String TAG = "CompassActivity";

    static public TextView tvDegrees , tvStrength;
    private Compass compass, compass2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass);
        actionBar = getSupportActionBar();
        actionBar.hide();

        tvDegrees = (TextView) findViewById(R.id.degrees);
        tvStrength = (TextView) findViewById(R.id.magnetic_strenght1);
        compass = new Compass(this);
        compass2 = new Compass(this);
        compass.arrowView = (ImageView) findViewById(R.id.main_image_dial);
        compass2.arrowView = (ImageView) findViewById(R.id.main_image_hands);

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "start compass");
        compass.start();
        compass2.start();

    }

    @Override
    protected void onPause() {
        super.onPause();
        compass.stop();
        compass2.stop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        compass.start();
        compass2.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "stop compass");
        compass.stop();
        compass2.stop();
    }
}
