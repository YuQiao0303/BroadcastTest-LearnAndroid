package com.example.broadcasttest;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private IntentFilter intentFilter;
    private NetworkChangeReceiver networkChangeReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //广播接收器想要监听什么广播， 就在这里add相应的action
        //当网络状态发生变化时， 系统发出的正是一条值为android.net.conn.CONNECTIVITY_CHANGE 的广播
        intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        networkChangeReceiver = new NetworkChangeReceiver();
        //下面这一句：NetworkChangeReceiver 就会收到所有值为android.net.conn.CONNECTIVITY_CHANGE 的广播
        registerReceiver(networkChangeReceiver, intentFilter);

        //发送标准广播
        Button sendButton = (Button) findViewById(R.id.button);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent("com.example.broadcasttest.MY_BROADCAST");
                //7.0的教程里没有这一句，高版本要加上，否则接收不到广播
                intent.setComponent( new ComponentName( "com.example.broadcasttest" ,
                        "com.example.broadcasttest.MyBroadcastReceiver") );

                sendBroadcast(intent);
                Log.d(TAG, "onClick: click the button!!");
            }
        });

        //发送有序广播
        Button sendOrderedButton = (Button) findViewById(R.id.button_order);
        sendOrderedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent("com.example.broadcasttest.MY_BROADCAST");
                //7.0的教程里没有这一句，高版本要加上，否则接收不到广播
                intent.setComponent( new ComponentName( "com.example.broadcasttest" ,
                        "com.example.broadcasttest.MyBroadcastReceiver") );

                sendOrderedBroadcast(intent, null);
                Log.d(TAG, "onClick: cli ck the button!!");
            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(networkChangeReceiver);//动态注册的广播接收器一定都要取消注册才行
    }

    /**
     * 动态注册的例子：监听网络变化
     * activity类内类receiver
     * activity类内registerReceiver和unregisterReceiver
     */
    class NetworkChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connectionManager = (ConnectivityManager)
                    getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectionManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isAvailable()) {
                Toast.makeText(context, "network is available",
                        Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(context, "network is unavailable",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
}