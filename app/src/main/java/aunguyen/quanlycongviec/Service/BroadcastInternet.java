package aunguyen.quanlycongviec.Service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.util.Log;

public class BroadcastInternet extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.i("ANTN", intent.getAction());

        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

        int state = wifiManager.getWifiState();
        String status = "";
        switch (state){
            case 0:
                status = "WIFI_STATE_DISABLING";
                break;
            case 1:
                status = "WIFI_STATE_DISABLED";
                break;
            case 2:
                status = "WIFI_STATE_ENABLING";
                break;
            case 3:
                status = "WIFI_STATE_ENABLED";
                break;
            case 4:
                status = "WIFI_STATE_UNKNOWN";
                break;
        }

        Log.i("ANTN", "Status: " + status);

        /*ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (wifi.isConnected()){
            Log.i("ANTN", "Có wifi");
        }else{
            Log.i("ANTN", "Không wifi");
        }*/

        //NetworkInfo mobile = conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        /*if (mobile.isConnected()){
            Log.i("ANTN", "Có mobile");
        }else{
            Log.i("ANTN", "Không mobile");
        }*/
    }
}
