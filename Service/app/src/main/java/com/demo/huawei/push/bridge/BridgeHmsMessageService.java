package com.demo.huawei.push.bridge;

import android.content.Intent;
import com.huawei.hms.push.HmsMessageService;
import com.huawei.hms.push.RemoteMessage;


public class BridgeHmsMessageService  extends HmsMessageService {
    @Override
    public void onNewToken(String s){ }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        String title = remoteMessage.getDataOfMap().get("title");
        String body = remoteMessage.getDataOfMap().get("body");
        sendBroadcastNotification(title, body);
    }

    @Override
    public void onMessageDelivered(String s, Exception e) {
        super.onMessageDelivered(s, e);
    }

    @Override
    public void onTokenError(Exception e) {
        super.onTokenError(e);
        e.printStackTrace();
    }

    public void sendBroadcastNotification(String title, String message){
        Intent intent = new Intent();
        intent.setAction(BridgePushService.HMS_BROADCAST_ACTION);
        intent.putExtra("title", title);
        intent.putExtra("message", message);
        sendBroadcast(intent);
    }

}
