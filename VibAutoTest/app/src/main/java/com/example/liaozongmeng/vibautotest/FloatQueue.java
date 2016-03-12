package com.example.liaozongmeng.vibautotest;

import android.util.Log;

/**
 * Created by liaozongmeng on 16/3/9.
 */
public class FloatQueue {
    final private static String TAG = "VibAutoTest --->> FloatQueue";
    private float[] queue_data = new float[100];
    private int QUEUE_LENGTH;
    private int index;

    public FloatQueue(int queue_length){
        QUEUE_LENGTH = queue_length;
        index = 0;
    }

    public void add(float element){
        queue_data[index] = element;
        index ++;
        if(index>=QUEUE_LENGTH){
            index = 0;
        }
    }

    public float maxDiff(){
        return max()-min();
    }

    public float max(){
        float max_value = queue_data[0];
        for (int i=0; i<QUEUE_LENGTH; i++){
            if(queue_data[i]>max_value){
                max_value = queue_data[i];
            }
        }
        return max_value;
    }

    public float min(){
        float min_value = queue_data[0];
        for (int i=0; i<QUEUE_LENGTH; i++){
            if(queue_data[i]<min_value){
                min_value = queue_data[i];
            }
        }
        return min_value;
    }

    public void printQueue() {
        String log_str = "";
        for (int i=0; i<QUEUE_LENGTH; i++){
            log_str += String.valueOf(queue_data[i])+", ";
        }
        Log.d(TAG, log_str);
//        System.out.println(log_str);
    }
}
