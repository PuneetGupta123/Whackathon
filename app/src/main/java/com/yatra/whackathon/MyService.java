package com.yatra.whackathon;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by vineet on 1/4/16.
 */
public class MyService extends Service {

    private final String TAG = "MyService";
    @Nullable

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }
    Receiver receiver;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Let it continue running until it is stopped.
        //Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "MyService created");
        IntentFilter screenStateFilter = new IntentFilter();
        screenStateFilter.addAction(Intent.ACTION_SCREEN_ON);
        screenStateFilter.addAction(Intent.ACTION_SCREEN_OFF);
        screenStateFilter.addAction(Intent.ACTION_HEADSET_PLUG);
        receiver = new Receiver(this);
        getApplicationContext().registerReceiver(receiver, screenStateFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
        //Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
    }

    Receiver getReceiver() {
        return receiver;
    }
}
