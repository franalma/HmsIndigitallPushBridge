package com.demo.huawei.push.app;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import lib.bridge.huawei.indigital.PushBridge;
import lib.bridge.huawei.indigital.PushBridgeDelegate;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PushBridge.start(getApplicationContext(), new PushBridgeDelegate() {
            @Override
            public void onInit(boolean status) {
                System.out.println("-->Connected: "+status);
            }

            @Override
            public void onNewToken(String token) {
                System.out.println("-->Token: "+token);
            }

            @Override
            public void onNotificationReceived(String message, String title) {
                System.out.println("-->Notification. Message: "+message+ " Title: "+ title);
            }
        });
    }
}