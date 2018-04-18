package com.example.service.periodicbackgroundservice;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by ugur on 18/04/18.
 */

public class PeriodicBackgroundService extends Service{

    private static final String TAG = "Service";
    public static final String POSITION_ACTION = "com.example.service.periodicbackgroundservice.Counter.Intent";

    private final Handler handler = new Handler();
    private Intent intent;
    private int counter = 0;

    /******* Overrides for service *******/
    // called when the service process is created, by any means of activities
    @Override
    public void onCreate() {
        super.onCreate();
        intent = new Intent(POSITION_ACTION);
    }

    // called when startService() called
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "started");
        handler.postDelayed(sendUpdatesToActivity, 500);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        handler.removeCallbacks(sendUpdatesToActivity);
        Log.d(TAG, "killed");
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    /******* Periodic thread *******/
    private Runnable sendUpdatesToActivity = new Runnable() {
        @Override
        public void run() {
            Log.d(TAG, "sending positioning result-" + counter);
            intent.putExtra("Counter", String.valueOf(counter));
            sendBroadcast(intent);
            counter++;
            handler.postDelayed(this, 1000);
        }
    };
}
