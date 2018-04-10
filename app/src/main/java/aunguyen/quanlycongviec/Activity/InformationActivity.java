package aunguyen.quanlycongviec.Activity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import aunguyen.quanlycongviec.R;

public class InformationActivity extends AppCompatActivity {

    private Toolbar toolbarInformation;
    private TextView tvVisionName;
    private PackageInfo info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        setUpToolbar();

        addControls();
    }

    private void addControls() {
        tvVisionName = findViewById(R.id.tv_version_name);
        info = null;
        try {
            info = this.getPackageManager().getPackageInfo(getPackageName(),0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        tvVisionName.setText(info.versionName.toString());

    }

    private void setUpToolbar(){
        toolbarInformation = findViewById(R.id.toolbar_information);
        setSupportActionBar(toolbarInformation);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
