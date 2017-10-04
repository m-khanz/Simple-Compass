package com.sevencrayons.compass;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

public class Compass implements SensorEventListener {
    private static final String TAG = "Compass";

    private SensorManager sensorManager;
    private Sensor gsensor;
    private Sensor msensor;
    private float[] mGravity = new float[3];
    private float[] mGeomagnetic = new float[3];
    static public float azimuth = 0f;
    private float currectAzimuth = 0;
    int magnetic_field_strength;

    // compass arrow to rotate
    public ImageView arrowView = null;

    public Compass(Context context) {

        sensorManager = (SensorManager) context
                .getSystemService(Context.SENSOR_SERVICE);
        gsensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        msensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    }

    public void start() {

        sensorManager.registerListener(this, gsensor,
                SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(this, msensor,
                SensorManager.SENSOR_DELAY_GAME);
    }

    public void stop() {
        sensorManager.unregisterListener(this);
    }

    private void adjustArrow() {
        if (arrowView == null) {
            Log.i(TAG, "arrow view is not set");
            return;
        }


        Log.i(TAG, "will set rotation from " + currectAzimuth + " to "
                + azimuth);

        Animation an = new RotateAnimation(-currectAzimuth, -azimuth,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        currectAzimuth = azimuth;

        an.setDuration(500);
        an.setRepeatCount(0);
        an.setFillAfter(true);

        arrowView.startAnimation(an);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        final float alpha = 0.97f;

        synchronized (this) {
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

                mGravity[0] = alpha * mGravity[0] + (1 - alpha)
                        * event.values[0];
                mGravity[1] = alpha * mGravity[1] + (1 - alpha)
                        * event.values[1];
                mGravity[2] = alpha * mGravity[2] + (1 - alpha)
                        * event.values[2];

                // mGravity = event.values;

                // Log.e(TAG, Float.toString(mGravity[0]));
            }

            if (event.sensor.getType()  == Sensor.TYPE_MAGNETIC_FIELD) {
                // mGeomagnetic = event.values;
//                int sensorRead = (int) event.values;
//                mag = sensorRead;
//                magAbsVal = Math.sqrt(mag[0]*mag[0] + mag[1]*mag[1] + mag[2]*mag[2]);
                magnetic_field_strength =  (int)Math.sqrt((event.values[0]*event.values[0])+
                        (event.values[1]*event.values[1])+(event.values[2]*event.values[2]));

                mGeomagnetic[0] = alpha * mGeomagnetic[0] + (1 - alpha)
                        * event.values[0];
                mGeomagnetic[1] = alpha * mGeomagnetic[1] + (1 - alpha)
                        * event.values[1];
                mGeomagnetic[2] = alpha * mGeomagnetic[2] + (1 - alpha)
                        * event.values[2];
                // Log.e(TAG, Float.toString(event.values[0]));

            }

            float R[] = new float[9];
            float I[] = new float[9];
            boolean success = SensorManager.getRotationMatrix(R, I, mGravity,
                    mGeomagnetic);
            if (success) {
                float orientation[] = new float[3];
                SensorManager.getOrientation(R, orientation);
                // Log.d(TAG, "azimuth (rad): " + azimuth);
                azimuth = (float) Math.toDegrees(orientation[0]); // orientation
                azimuth = (azimuth + 360) % 360;
                int az = (int) azimuth;
                Log.d(TAG, "azimuth (deg): " + azimuth);
                adjustArrow();

                if(magnetic_field_strength < 29){
                    CompassActivity.tvStrength.setText(String.valueOf(magnetic_field_strength)+"\u00B5 T");
                    CompassActivity.tvStrength.setTextColor(Color.RED);
                }else{
                    CompassActivity.tvStrength.setText(String.valueOf(magnetic_field_strength)+"\u00B5 T");
                    CompassActivity.tvStrength.setTextColor(Color.BLACK);
                }


                if (az >= 338) {
                    CompassActivity.tvDegrees.setText(String.valueOf(az) + (char) 0x00B0 + " North");
                }
                if (az <= 22) {
                    CompassActivity.tvDegrees.setText(String.valueOf(az) + (char) 0x00B0 + " North");
                }
                if (az >= 23 && az <= 67) {
                    CompassActivity.tvDegrees.setText(String.valueOf(az) + (char) 0x00B0 + " North East");
                }
                if (az >= 68 && az <= 112) {
                    CompassActivity.tvDegrees.setText(String.valueOf(az) + (char) 0x00B0 + " East");
                }
                if (az >= 113 && az <= 157) {
                    CompassActivity.tvDegrees.setText(String.valueOf(az) + (char) 0x00B0 + " South East");
                }
                if (az >= 158 && az <= 202) {
                    CompassActivity.tvDegrees.setText(String.valueOf(az) + (char) 0x00B0 + " South");
                }
                if (az >= 203 && az <= 247) {
                    CompassActivity.tvDegrees.setText(String.valueOf(az) + (char) 0x00B0 + " South West");
                }
                if (az >= 248 && az <= 292) {
                    CompassActivity.tvDegrees.setText(String.valueOf(az) + (char) 0x00B0 + " West");
                }
                if (az >= 293 && az <= 337) {
                    CompassActivity.tvDegrees.setText(String.valueOf(az) + (char) 0x00B0 + " North West");
                }

            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}
