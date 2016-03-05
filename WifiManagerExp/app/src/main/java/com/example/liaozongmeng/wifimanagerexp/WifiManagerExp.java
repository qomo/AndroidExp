package com.example.liaozongmeng.wifimanagerexp;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class WifiManagerExp extends Activity {

    Button bt_connect = null;
    WifiManager wifiManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_manager_exp);

        bt_connect = (Button) findViewById(R.id.bt_connect);
        bt_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
                wifiManager.setWifiEnabled(true);

                EditText et_ssid = (EditText) findViewById(R.id.et_ssid);
                String ssid = et_ssid.getText().toString();
                EditText et_key = (EditText) findViewById(R.id.et_key);
                String key = et_key.getText().toString();

                WifiConfiguration config = new WifiConfiguration();
//                config.SSID = "\"" + "dd-wrt_2.4g" + "\"";
//                config.preSharedKey = "\""+ "13817486091" +"\"";
                config.SSID = "\"" + ssid + "\"";
                config.preSharedKey = "\""+ key +"\"";

                int netId = wifiManager.addNetwork(config);
                wifiManager.saveConfiguration();
                wifiManager.disconnect();
                wifiManager.enableNetwork(netId, true);
                wifiManager.reconnect();
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_wifi_manager_exp, menu);
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
