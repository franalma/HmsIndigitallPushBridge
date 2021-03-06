package lib.bridge.huawei.indigital;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;

public class PushBridge {

    private PushBridgeDelegate delegate;
    private final static String SERVICE_NAME = "BridgePushService";
    private final static String HMS_BROADCAST_ACTION ="hms.notification";
    private static PushBridge instance;


    private PushBridge(PushBridgeDelegate delegate, Context context){
        this.delegate = delegate;
        this.init(context);

    }


    public static void start(Context context, PushBridgeDelegate delegate){
       if (instance == null){
           instance =new PushBridge(delegate, context);
       }

    }

    private void init(Context context){
        Intent intent = new Intent(SERVICE_NAME);
        Intent explicit = Utils.convertImplicitIntentToExplicitIntent(intent, context);

        final ServiceConnection serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                System.out.println("--> on service connected");
                delegate.onInit(true);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                System.out.println("--> on service disconnected");
                delegate.onInit(false);
            }
        };
        context.bindService(explicit, serviceConnection, Context.BIND_AUTO_CREATE);
        context.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(HMS_BROADCAST_ACTION)) {
                    if (intent.getStringExtra("token") != null){
                        delegate.onNewToken(intent.getStringExtra("token"));
                    }else if (intent.getStringExtra("message") != null ){
                        delegate.onNotificationReceived(intent.getStringExtra("message"), intent.getStringExtra("title"));
                    }
                }
            }
        },new IntentFilter(HMS_BROADCAST_ACTION));
    }


}
