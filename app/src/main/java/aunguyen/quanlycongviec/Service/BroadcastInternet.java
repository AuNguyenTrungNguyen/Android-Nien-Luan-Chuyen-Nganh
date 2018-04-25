package aunguyen.quanlycongviec.Service;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import aunguyen.quanlycongviec.Activity.SignInActivity;

public class BroadcastInternet extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getActiveNetworkInfo();

        if (mWifi != null){
            if (mWifi.isConnected()) {
                Intent i = new Intent(context, SignInActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
                PackageManager pm  = context.getPackageManager();
                ComponentName componentName = new ComponentName(context, BroadcastInternet.class);
                pm.setComponentEnabledSetting(componentName,PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                        PackageManager.DONT_KILL_APP);

                Log.i("ANTN", "isConnected");
            }
        }
    }
}
