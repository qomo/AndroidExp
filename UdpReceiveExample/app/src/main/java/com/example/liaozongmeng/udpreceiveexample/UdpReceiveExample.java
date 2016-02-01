package com.example.liaozongmeng.udpreceiveexample;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.widget.TextView;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

//http://www.cnblogs.com/zhangzph/p/4475962.html

public class UdpReceiveExample extends Activity implements Runnable{
    private static String LOG_TAG = "UdpReceiveExample";
    String multicastHost = "224.0.0.1";

    MulticastSocket ms = null;
    DatagramPacket dp;
    InetAddress receiveAddress;

    byte buf[] = new byte[1024];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_udp_receive_example);

        WifiManager wifi = (WifiManager)getSystemService(Context.WIFI_SERVICE);
        if (wifi != null){
            WifiManager.MulticastLock lock = wifi.createMulticastLock("mylock");
            lock.acquire();
        }

        try{
            ms = new MulticastSocket(2222);
            receiveAddress = InetAddress.getByName(multicastHost);
            ms.joinGroup(receiveAddress);
            new Thread(this).start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void run() {
        System.out.println("Thread running!!!");
        while (true) {
            try {
                dp = new DatagramPacket(buf, 1024);
                ms.receive(dp);
                System.out.println("cmd: " + new String(buf, 0, dp.getLength()));
                UdpReceiveExample.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                    TextView tv = (TextView) findViewById(R.id.tv);
                    tv.setText("cmd: " + new String(buf, 0, dp.getLength()));
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


//    private class UdpReceiver extends Thread{
//        @Override
//        public void run() {
//            byte[] data = new byte[1024];
//            try{
//                InetAddress groupAddress = InetAddress.getByName("192.168.1.255");
//                ms = new MulticastSocket(22222);
//                ms.joinGroup(groupAddress);
//            } catch (UnknownHostException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            while (true) {
//                try{
//                    dp = new DatagramPacket(data, data.length);
//                    if(ms != null)
//                        ms.receive(dp);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//                if(dp.getAddress() != null){
//                    final String quest_ip = dp.getAddress().toString();
//                    System.out.println("IP quest: " + quest_ip);
//                }
//            }
//        }
//    }
}
