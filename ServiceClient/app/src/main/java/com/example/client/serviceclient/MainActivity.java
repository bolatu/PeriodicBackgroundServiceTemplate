package com.example.client.serviceclient;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private Intent intent;
    private IntentFilter intentFilter;

    private Button button;
    private TextView textView;

    private boolean isServiceStarted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (Button) findViewById(R.id.button);
        textView = (TextView) findViewById(R.id.textView);


        intent = new Intent("com.example.service.periodicbackgroundservice.Counter");
        intent.setPackage("com.example.service.periodicbackgroundservice");


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isServiceStarted) {
                    startService(intent);
                    intentFilter = new IntentFilter(
                            "com.example.service.periodicbackgroundservice.Counter.Intent");
                    registerReceiver(broadcastReceiver, intentFilter);
                    isServiceStarted = true;
                    button.setText("STOP SERVICE");
                }
                else{
                    unregisterReceiver(broadcastReceiver);
                    stopService(intent);
                    isServiceStarted = false;
                    button.setText("START SERVICE");
                }
            }
        });

    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(broadcastReceiver);
        stopService(intent);
        super.onDestroy();
    }


    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updatePosition(intent);
        }
    };

    private void updatePosition(Intent intent) {

        if (!intent.getStringExtra("Counter").equals("")) {
            String counter = intent.getStringExtra("Counter");
            Log.d(TAG, counter);
            textView.setText(counter);
        }
    }
}
