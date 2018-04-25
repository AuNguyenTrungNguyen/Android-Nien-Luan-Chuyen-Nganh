package aunguyen.quanlycongviec.Activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import aunguyen.quanlycongviec.R;
import aunguyen.quanlycongviec.Service.BroadcastInternet;

public class StartActivity extends AppCompatActivity {

    private AlertDialog.Builder builder;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        setupDialog();

        PackageManager pm  = this.getPackageManager();
        ComponentName componentName = new ComponentName(this, BroadcastInternet.class);
        pm.setComponentEnabledSetting(componentName,PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);


        if (isNetworkConnected()) {

            Intent intent = new Intent(this, SignInActivity.class);
            startActivity(intent);
        } else {
            dialog.show();
        }
    }

    private void setupDialog() {
        builder = new AlertDialog.Builder(this);
        builder.setMessage(getResources().getString(R.string.dialog_change_wifi));

        builder.setPositiveButton(getString(R.string.dialog_change_wifi_positive), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                dialogInterface.dismiss();
            }
        });

        builder.setNegativeButton(getString(R.string.dialog_change_wifi_negative), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                finish();
            }
        });
        dialog = builder.create();
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }
}
