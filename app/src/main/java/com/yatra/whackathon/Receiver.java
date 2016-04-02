package com.yatra.whackathon;

import android.app.KeyguardManager;
import android.app.Service;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import java.util.Timer;
import java.util.TimerTask;


public class Receiver extends BroadcastReceiver {

    private final String TAG = "Receiver";
    int countPowerOff=0;
    private Service service;
    private Thread thread;

    public Receiver (Service service) {
        this.service=service;
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
            thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(3000);
                        countPowerOff = 0;
                        thread = null;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();
        } else if(countPowerOff==5) {
            if (thread != null) {
                thread.interrupt();
                thread = null;
            }
            countPowerOff = 0;

            PowerManager pm = (PowerManager)context.getSystemService(Context.POWER_SERVICE);
            PowerManager.WakeLock wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK
                    | PowerManager.ACQUIRE_CAUSES_WAKEUP
                    | PowerManager.ON_AFTER_RELEASE, "MyWakeLock");
            wakeLock.acquire();

            Intent i =new Intent(context, WakeUpActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }

    }
}
