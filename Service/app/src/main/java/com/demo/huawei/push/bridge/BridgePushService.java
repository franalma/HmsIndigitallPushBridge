package com.demo.huawei.push.bridge;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.IBinder;
import com.huawei.hms.aaid.HmsInstanceId;
import com.huawei.hms.common.ApiException;
import com.indigitall.android.Indigitall;
import com.indigitall.android.callbacks.InitCallBack;
import com.indigitall.android.models.Device;
import com.indigitall.android.models.Permission;

public class BridgePushService extends Service {

    Binder binder = new Binder();
    final static String HMS_BROADCAST_ACTION ="hms.notification";
    final static String INDIGITALL_APP_KEY = "060ce62f-be2c-4fc0-8ad5-4c00c92a02a7";
    final static String HMS_PUSH_SENDER_ID = "737518067793624016";
    final static String APP_ID = "104835037";



    public  void getTokenForDirectHmsPush(final Context context) {
//         Create a thread.
        new Thread() {
            @Override
            public void run() {
                try {
                    // Enter the token ID HCM.
                    String tokenScope = "HCM";
                    String token = HmsInstanceId.getInstance(context).getToken(APP_ID, tokenScope);
                    System.out.println("token: "+token);
                    sendBroadcastToken(token);
                } catch (ApiException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void initIndigitall(){
        Indigitall.init(this, INDIGITALL_APP_KEY, HMS_PUSH_SENDER_ID,new InitCallBack(this){
            @Override
            public void onNewUserRegistered(Device device) {
                super.onNewUserRegistered(device);
            }

            @Override
            public void onIndigitallInitialized(Permission[] permissions, Device device) {
                super.onIndigitallInitialized(permissions, device);
                sendBroadcastToken(device.getPushToken());

            }
        });
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
//        getTokenForDirectHmsPush();
        initIndigitall();
        return binder;
    }

    public void sendBroadcastToken(String token){
        Intent intent = new Intent();
        intent.setAction(HMS_BROADCAST_ACTION);
        intent.putExtra("token", token);
        sendBroadcast(intent);
    }


}
