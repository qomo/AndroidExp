package com.example.liaozongmeng.vibagingtest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.Switch;


public class VibAgingTest extends Activity {
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vib_aging_test);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        intent = new Intent(this, VibService.class);
        startService(intent);

        Switch hw_vib_sw = (Switch) findViewById(R.id.hw_vib_mode_sw);
        hw_vib_sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    VibService.hwVibMode();
                } else {
                    VibService.hwVibStop();
                }
            }
        });

        Switch a_vib_sw = (Switch) findViewById(R.id.a_vib_mode_sw);
        a_vib_sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    VibService.aVibMode();
                } else {
                    VibService.aVibStop();
                }
            }
        });

        Switch s2_vib_sw = (Switch) findViewById(R.id.vib_2s_mode_sw);
        s2_vib_sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    VibService.s2VibMode();
                } else {
                    VibService.s2VibStop();
                }
            }
        });

        Switch always_vib_sw = (Switch) findViewById(R.id.vib_always_sw);
        always_vib_sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    VibService.alwaysVibMode();
                } else {
                    VibService.alwaysVibStop();
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopService(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_vib_aging_test, menu);
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
