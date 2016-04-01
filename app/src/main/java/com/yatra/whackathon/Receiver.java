package com.yatra.whackathon;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;


public class Receiver extends BroadcastReceiver {

    private final String TAG = "Receiver";
    int countPowerOff=0;
    private Service service;
    private Thread thread;

    public Receiver (Service service) {
        this.service=service;

        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                    countPowerOff = 0;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        Log.v(TAG, "Power button is pressed.");

        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF) || intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            Log.d(TAG,"Count power of " + countPowerOff);
            countPowerOff++;
        }

        if (countPowerOff == 1) {
            thread.start();

        } else if(countPowerOff==5) {
            thread.interrupt();
            countPowerOff = 0;
            Intent i =new Intent(context, SplashScreen.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }

    }
}
