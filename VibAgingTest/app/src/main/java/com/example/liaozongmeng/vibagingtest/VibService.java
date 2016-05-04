package com.example.liaozongmeng.vibagingtest;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Vibrator;
import android.util.Log;

public class VibService extends Service {
    static final String TAG = "VibService";
    private static Vibrator mVibrator;
    private static boolean hwVibFlag = false;
    private static boolean aVibFlag = false;

    public VibService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        flags = START_STICKY;

        mVibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mVibrator != null){
            mVibrator.cancel();
            mVibrator = null;
        }
        stopForeground(true);
    }

    public static void hwVibMode(){
        hwVibFlag = true;
        mVibrator.cancel();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (hwVibFlag){
                        for(int i=0; i<10; i++){
                            vib1on05off();
                            vib1on05off();
                            for (int t1=0; t1<4; t1++){
                                vib05on05off();
                            }
                            for (int t2=0; t2<20; t2++){
                                vib01tap();
                            }
                            for (int t3=0; t3<40; t3++){
                                vib005tap();
                            }
                            if(!hwVibFlag){
                                break;
                            }
                        }
                        if(!hwVibFlag){
                            break;
                        }
                        vib5000();
                    }
                } catch (Throwable t) {
                    Log.i(TAG, "Thread exception " + t);
                }
            }
        }).start();
    }

    public static void aVibMode() {
        aVibFlag = true;
        mVibrator.cancel();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (aVibFlag){
                        for(int i=0; i<10; i++){
                            vib1on05off();
                            for (int t2=0; t2<20; t2++){
                                vib01tap();
                            }
                            for (int t3=0; t3<40; t3++){
                                vib005tap();
                            }
                            if(!aVibFlag){
                                break;
                            }
                        }
                        if(!aVibFlag){
                            break;
                        }
                        vib5000();
                    }
                } catch (Throwable t) {
                    Log.i(TAG, "Thread exception " + t);
                }
            }
        }).start();
    }

    public static void alwaysVibMode() {
        mVibrator.vibrate(new long[]{0, 5000}, 0);
    }

    public static void s2VibMode() {
        mVibrator.vibrate(new long[]{2000, 2000}, 0);
    }

    public static void hwVibStop() {
        hwVibFlag = false;
    }

    public static void aVibStop() {
        aVibFlag = false;
    }

    public static void s2VibStop() {
        mVibrator.cancel();
    }

    public static void alwaysVibStop() {
        mVibrator.cancel();
    }

    public static void vib1on05off() throws InterruptedException {
        mVibrator.vibrate(new long[]{0, 1000, 500}, -1);
        Thread.sleep(1500);
    }

    public static void vib05on05off() throws InterruptedException {
        mVibrator.vibrate(new long[]{0, 500, 500}, -1);
        Thread.sleep(1000);
    }

    public static void vib01tap() throws InterruptedException {
        mVibrator.vibrate(new long[]{0, 100, 100}, -1);
        Thread.sleep(200);
    }

    public static void vib005tap() throws InterruptedException {
        mVibrator.vibrate(new long[]{0, 50, 50}, -1);
        Thread.sleep(100);
    }

    public static void vib5000() throws InterruptedException {
        mVibrator.vibrate(new long[]{0, 5000, 2000}, -1);
        Thread.sleep(7000);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
