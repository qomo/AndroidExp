package com.example.liaozongmeng.vibautotest;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;


public class VibAutoTest extends Activity {
    static final String TAG = "VibAutoTest";
    Switch sw_test = null;
    public static Intent intent_testservice = null;
    Intent intent_cmd = null;
    Intent intent_wifi = null;
    private MsgReceiver msg_receiver = null;

    private Vibrator mVibrator = null;

    public static long vib_test_time = 4000;

    private Button bt_vib_test = null;


    public class MsgReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals("UPDATE_VIBRATION")) {
                float vib_x = intent.getFloatExtra("vib_x", 0);
                TextView tv_acc_x = (TextView) findViewById(R.id.tv_acc_x);
                tv_acc_x.setText(String.valueOf(vib_x));
                float vib_y = intent.getFloatExtra("vib_y", 0);
                TextView tv_acc_y = (TextView) findViewById(R.id.tv_acc_y);
                tv_acc_y.setText(String.valueOf(vib_y));
                float vib_z = intent.getFloatExtra("vib_z", 0);
                TextView tv_acc_z = (TextView) findViewById(R.id.tv_acc_z);
                tv_acc_z.setText(String.valueOf(vib_z));
            }else if(action.equals("UPDATE_VIBRATION_MEAN")) {
                float vib_mean_x = intent.getFloatExtra("vib_mean_x", 0);
                TextView tv_acc_mean_x = (TextView) findViewById(R.id.tv_acc_mean_x);
                tv_acc_mean_x.setText(String.valueOf(vib_mean_x));
                float vib_mean_y = intent.getFloatExtra("vib_mean_y", 0);
                TextView tv_acc_mean_y = (TextView) findViewById(R.id.tv_acc_mean_y);
                tv_acc_mean_y.setText(String.valueOf(vib_mean_y));
                float vib_mean_z = intent.getFloatExtra("vib_mean_z", 0);
                TextView tv_acc_mean_z = (TextView) findViewById(R.id.tv_acc_mean_z);
                tv_acc_mean_z.setText(String.valueOf(vib_mean_z));
            }else if(action.equals("VIB_TEST")) {
                bt_vib_test.callOnClick();
            }else if(action.equals("SET_VIB_PARAS")) {
                EditText et_avr_timg = (EditText) findViewById(R.id.et_avr_time);
                et_avr_timg.setText(String.valueOf(intent.getIntExtra("AVR_TIME", 4000)));
                EditText et_num_calc = (EditText) findViewById(R.id.et_num_calc);
                et_num_calc.setText(String.valueOf(intent.getIntExtra("AVR_COUNT", 30)));
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate()");
        setContentView(R.layout.activity_vib_auto_test);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        mVibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        intent_cmd = new Intent(this, SocketCMDService.class);
        startService(intent_cmd);
        intent_wifi = new Intent(this, WifiToolService.class);
        startService(intent_wifi);

        msg_receiver = new MsgReceiver();
        IntentFilter intent_filter = new IntentFilter();
        intent_filter.addAction("UPDATE_VIBRATION");
        intent_filter.addAction("UPDATE_VIBRATION_MEAN");
        intent_filter.addAction("VIB_TEST");
        intent_filter.addAction("SET_VIB_PARAS");
        registerReceiver(msg_receiver, intent_filter);

        intent_testservice = new Intent(this, TestService.class);

//        sw_test = (Switch) findViewById(R.id.sw_test);
//        sw_test.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if(isChecked){
//                    Log.d(TAG, "startService()");
//                    System.out.println("startService()");
//                    startService(intent_testservice);
//                    mVibrator.vibrate(new long[] {0, 5000}, 0);
//                } else {
//                    Log.d(TAG, "stopService()");
//                    stopService(intent_testservice);
//                    mVibrator.cancel();
//                }
//            }
//        });

        bt_vib_test = (Button) findViewById(R.id.bt_vib_test);
        bt_vib_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText et_avr_timg = (EditText) findViewById(R.id.et_avr_time);
                vib_test_time = Integer.parseInt(et_avr_timg.getText().toString());
                EditText et_num_calc = (EditText) findViewById(R.id.et_num_calc);
                TestService.set_queue_num(Integer.parseInt(et_num_calc.getText().toString()));
                vibTest();
            }
        });
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        unregisterReceiver(msg_receiver);
        stopService(intent_cmd);
        stopService(intent_wifi);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_vib_auto_test, menu);
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

    public void vibTest() {
        mVibrator.cancel();
        new Thread(new Runnable(){
            public void run(){
                try {
                    mVibrator.vibrate(vib_test_time+2000);
                    Thread.sleep(1000);
                    startService(intent_testservice);
                    Thread.sleep(vib_test_time);
                    stopService(intent_testservice);
                    Thread.sleep(100);
                    mVibrator.cancel();
                    Intent intent = new Intent("UPDATE_VIBRATION_MEAN");
                    intent.putExtra("vib_mean_x", TestService.get_acc_mean_x());
                    intent.putExtra("vib_mean_y", TestService.get_acc_mean_y());
                    intent.putExtra("vib_mean_z", TestService.get_acc_mean_z());
                    Log.d(TAG, String.valueOf(TestService.get_acc_mean_x()));
                    sendBroadcast(intent);
                } catch (Throwable t) {
                    Log.i(TAG, "Thread exception " + t);
                }
            }
        }).start();
    }
}
