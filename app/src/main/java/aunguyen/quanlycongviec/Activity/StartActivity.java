package aunguyen.quanlycongviec.Activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import aunguyen.quanlycongviec.R;

public class StartActivity extends AppCompatActivity {

    private BroadcastReceiver receiver;
    //private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        if(isNetworkConnected()){
            Toast.makeText(this, "Có", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Không", Toast.LENGTH_SHORT).show();
        }
    }


    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }
}
