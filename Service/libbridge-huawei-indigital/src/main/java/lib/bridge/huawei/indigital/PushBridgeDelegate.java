package lib.bridge.huawei.indigital;

public interface PushBridgeDelegate {

    void onInit(boolean result);
    void onNewToken(String token);
    void onNotificationReceived(String message, String title);
}
