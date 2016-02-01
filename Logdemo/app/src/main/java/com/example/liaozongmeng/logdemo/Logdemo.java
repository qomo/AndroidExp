package com.example.liaozongmeng.logdemo;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;


public class Logdemo extends Activity implements SensorEventListener {

    private LogWriter mLogWriter;
    private static final String TAG = "Logdemo";

    private SensorManager sMgr;
    private Sensor mLight;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logdemo);

        File logf = new File(Environment.getExternalStorageDirectory()
                + File.separator + "DemoLog.txt");

        try {
            mLogWriter = LogWriter.open(logf.getAbsolutePath());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            Log.d(TAG, e.getMessage());
        }

        log("onCreate()");

        sMgr = (SensorManager) this.getSystemService(SENSOR_SERVICE);

        if(sMgr.getDefaultSensor(Sensor.TYPE_LIGHT) != null)
        {
            log("OK, get LIGHT sensor!");
            mLight = sMgr.getDefaultSensor(Sensor.TYPE_LIGHT);
        }
        else
        {
            log("fault!!!");
        }
    }


    public void log(String msg) {
        Log.d(TAG, msg);

        try {
            mLogWriter.print(Logdemo.class, msg);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            Log.d(TAG, e.getMessage());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_logdemo, menu);
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

    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do something here if sensor accuracy changes.
    }

    @Override
    public final void onSensorChanged(SensorEvent event) {
        // The light sensor returns a single value.
        // Many sensors return 3 values, one for each axis.
        float lux = event.values[0];
        // Do something with this sensor value.
        TextView TextDataLight = (TextView)findViewById(R.id.TViewLight);
        TextDataLight.setText(String.valueOf(lux));
        log(String.valueOf(lux));
    }

    @Override
    protected void onResume() {
        super.onResume();
        log("onResume!");
        sMgr.registerListener(this, mLight, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        log("onPause!");
        sMgr.unregisterListener(this);
    }
}
