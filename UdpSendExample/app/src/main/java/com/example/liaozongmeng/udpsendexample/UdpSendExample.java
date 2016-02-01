package com.example.liaozongmeng.udpsendexample;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;


public class UdpSendExample extends ActionBarActivity {
    /*发送广播的按钮*/
    private Button sendUDPBrocast;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_udp_send_example);

        sendUDPBrocast = (Button) findViewById(R.id.BtnDo);
        sendUDPBrocast.setOnClickListener(new SendUDPBroadcastListener());

    }


    class SendUDPBroadcastListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            new udpBroadCast("hi~~~").start();
            System.out.println("Sending multicast!");
        }
    }

    /* 发送udp多播 */
    private  class udpBroadCast extends Thread {
        MulticastSocket sender = null;
        DatagramPacket dj = null;
        InetAddress group = null;

        byte[] data = new byte[1024];

        public udpBroadCast(String dataString) {
            data = dataString.getBytes();
        }

        @Override
        public void run() {
            try {
                sender = new MulticastSocket();
                group = InetAddress.getByName("224.0.0.1");
                dj = new DatagramPacket(data,data.length,group,2222);
                sender.send(dj);
                sender.close();
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
    }
}
