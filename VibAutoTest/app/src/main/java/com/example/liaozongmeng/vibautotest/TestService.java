package com.example.liaozongmeng.vibautotest;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;

public class TestService extends Service implements SensorEventListener {
    final private static String TAG = "VibAutoTest --->> TestService";

    private static int queue_num = 30;
    private FloatQueue floatQueue_x = null;
    private FloatQueue floatQueue_y = null;
    private FloatQueue floatQueue_z = null;

    private static int count_base = 0;
    private static int count_avr = 0;
    private static float acc_mean_x = 0;
    private static float acc_mean_y = 0;
    private static float acc_mean_z = 0;

    private SensorManager mSensorManager = null;
    private Sensor mSensor = null;

    private Intent intent = new Intent("UPDATE_VIBRATION");


    public TestService() {

    }

    public static float get_acc_mean_x() {
        return acc_mean_x/count_avr;
    }

    public static float get_acc_mean_y() {
        return acc_mean_y/count_avr;
    }

    public static float get_acc_mean_z() {
        return acc_mean_z/count_avr;
    }

    public static void set_queue_num(int n) {
        queue_num = n;
    }

    @Override
    public void onCreate() {
        count_base = 0;
        count_avr = 0;
        acc_mean_x = 0;
        acc_mean_y = 0;
        acc_mean_z = 0;
        floatQueue_x = new FloatQueue(queue_num);
        floatQueue_y = new FloatQueue(queue_num);
        floatQueue_z = new FloatQueue(queue_num);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSensorManager.unregisterListener(this, mSensor);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        count_base ++;
        float acc_x = event.values[0];
        float acc_y = event.values[1];
        float acc_z = event.values[2];
        floatQueue_x.add(acc_x);
        floatQueue_y.add(acc_y);
        floatQueue_z.add(acc_z);

        intent.putExtra("vib_x", floatQueue_x.maxDiff());
        intent.putExtra("vib_y", floatQueue_y.maxDiff());
        intent.putExtra("vib_z", floatQueue_z.maxDiff());
        sendBroadcast(intent);

        if (count_base > queue_num){
            acc_mean_x += floatQueue_x.maxDiff();
            acc_mean_y += floatQueue_y.maxDiff();
            acc_mean_z += floatQueue_z.maxDiff();
            count_avr += 1;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
