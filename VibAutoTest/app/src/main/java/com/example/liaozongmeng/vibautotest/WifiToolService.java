package com.example.liaozongmeng.vibautotest;

import android.app.Service;
import android.content.Intent;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.util.Log;

public class WifiToolService extends Service {
    private static final String TAG = "VibAutoTest --->> WifiToolService";
    private static WifiManager wifiManager = null;

    public WifiToolService() {
    }

    @Override
    public void onCreate(){
        super.onCreate();
        Log.d(TAG, "onCreate...");
        wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand...");
        return START_STICKY;
    }

    public static void connect(String ssid, String key) {
        wifiManager.setWifiEnabled(true);
        WifiConfiguration config = new WifiConfiguration();
        config.SSID = "\"" + ssid + "\"";
        config.preSharedKey = "\""+ key +"\"";

        int netId = wifiManager.addNetwork(config);
        wifiManager.saveConfiguration();
        wifiManager.disconnect();
        wifiManager.enableNetwork(netId, true);
        wifiManager.reconnect();
    }

    public static String getWiFiSSDI() {
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        return wifiInfo.getSSID();
    }

    /***
     * 使用WIFI时，获取本机IP地址
     * @param
     * @return
     */
    public static String getWIFILocalIpAdress() {
        //判断wifi是否开启
        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipAddress = wifiInfo.getIpAddress();
        String ip = formatIpAddress(ipAddress);
        return ip;
    }

    private static String formatIpAddress(int ipAdress) {

        return (ipAdress & 0xFF ) + "." +
                ((ipAdress >> 8 ) & 0xFF) + "." +
                ((ipAdress >> 16 ) & 0xFF) + "." +
                ( ipAdress >> 24 & 0xFF) ;
    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
