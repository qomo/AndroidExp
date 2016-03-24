package com.example.liaozongmeng.vibautotest;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketCMDService extends Service {
    private static final String TAG = "VibAutoTest --->> SocketCMDService";

    public SocketCMDService() {
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand...");

        new Server().start();

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy...");
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private class Server extends Thread {
        static final int SocketServerPORT = 11899;

        ServerSocket server = null;
        Socket sk = null;
        BufferedReader rdr = null;

        public Server() {
            try {
                server = new ServerSocket(SocketServerPORT);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void run() {
            while (server != null) {
                Log.d(TAG, "Socket server listening...");
                try {
                    sk = server.accept();
                    ServerHandler sh = new ServerHandler(sk);
                    sh.run();
                    sleep(1000);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private class ServerHandler extends Thread {
        Socket socket_client = null;
        PrintWriter wtr = null;
        BufferedReader rdr = null;
        String line = null;

        JSONObject jsonObject = null;

        PrintStream printStream = null;

        public ServerHandler(Socket sk) {
            socket_client = sk;
            OutputStream output = null;
            try {
                output = socket_client.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            printStream = new PrintStream(output);
        }

        public void run() {
            try {
                wtr = new PrintWriter(socket_client.getOutputStream());
                rdr = new BufferedReader(new InputStreamReader(socket_client.getInputStream()));
                line = rdr.readLine();
                if (line != null) {
                    jsonObject = new JSONObject(line);
                    String cmd = jsonObject.getString("CMD");
                    Log.d(TAG, "JSON COM is: " + cmd);
                    if (cmd.equals("SET_VIB_PARAS")) {
                        int avr_time = jsonObject.getInt("AVR_TIME");
                        int avr_count = jsonObject.getInt("AVR_COUNT");
                        Intent intent = new Intent("SET_VIB_PARAS");
                        intent.putExtra("AVR_TIME", avr_time);
                        intent.putExtra("AVR_COUNT", avr_count);
                        sendBroadcast(intent);
                        printStream.println("OK");
                    } else if (cmd.equals("VIB_TEST")) {
                        Intent intent = new Intent("VIB_TEST");
                        sendBroadcast(intent);
                        Thread.sleep(VibAutoTest.vib_test_time+2000);
                        JSONObject return_json = new JSONObject();
                        return_json.put("VIB_TEST_RESULT_X", TestService.get_acc_mean_x());
                        return_json.put("VIB_TEST_RESULT_Y", TestService.get_acc_mean_y());
                        return_json.put("VIB_TEST_RESULT_Z", TestService.get_acc_mean_z());
                        printStream.println(return_json);
                    } else if (cmd.equals("CONNECT_WIFI")) {
                        String ssid = jsonObject.getString("SSID");
                        String key = jsonObject.getString("KEY");
                        WifiToolService.connect(ssid, key);
                        printStream.println("OK");
                    } else if (cmd.equals("GET_WIFI_INFO")) {
                        JSONObject return_json = new JSONObject();
                        return_json.put("SSID", WifiToolService.getWiFiSSDI());
                        return_json.put("IP", WifiToolService.getWIFILocalIpAdress().replace("\"", ""));
                        printStream.println(return_json);
                    } else if (cmd.equals("PLAY_MUSIC")) {
                        Intent intent = new Intent("PLAY_MUSIC");
                        sendBroadcast(intent);
                        printStream.println("OK");
                    } else if (cmd.equals("STOP_MUSIC")) {
                        Intent intent = new Intent("STOP_MUSIC");
                        sendBroadcast(intent);
                        printStream.println("OK");
                    }
                    Log.d(TAG, "get msg: " + line);
                    socket_client.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
