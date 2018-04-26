package aunguyen.quanlycongviec.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import aunguyen.quanlycongviec.R;

public class InformationActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbarInformation;

    private TextView tvVisionName;
    private TextView tvPhoneSang;
    private TextView tvPhoneNguyen;
    private TextView tvEmail;
    private TextView tvFaceBook;

    private PackageInfo info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        checkAndRequestPermissions();

        setContentView(R.layout.activity_information);

        setUpToolbar();

        addControls();

        addEvent();
    }

    private void addControls() {
        tvVisionName = findViewById(R.id.tv_version_name);
        tvEmail = findViewById(R.id.tv_email);
        tvPhoneSang = findViewById(R.id.tv_phone_sang);
        tvPhoneNguyen = findViewById(R.id.tv_phone_nguyen);
        tvFaceBook = findViewById(R.id.tv_faceBook);

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

    private void addEvent() {
        tvEmail.setOnClickListener(this);
        tvPhoneSang.setOnClickListener(this);
        tvPhoneNguyen.setOnClickListener(this);
        tvFaceBook.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_faceBook:
                facebook();
                break;
            case R.id.tv_phone_sang:
                phone(tvPhoneSang.getText().toString());
                break;

            case R.id.tv_phone_nguyen:
                phone(tvPhoneNguyen.getText().toString());
                break;

            case R.id.tv_email:
                email();
                break;
        }
    }

    public void email()  {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.putExtra(Intent.EXTRA_EMAIL , "nienluanchuyennganh@gmail.com");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Phản hồi góp ý");
        PackageInfo info = null;

        try {
            info = this.getPackageManager().getPackageInfo(getPackageName(),0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        intent.putExtra(Intent.EXTRA_TEXT, "Góp ý về cho nhà phát hành "+info.versionName.toString());
        intent.setData(Uri.parse("mailto: nienluanchuyennganh@gmail.com"));
        startActivity(Intent.createChooser(intent, "Phản hồi với"));
    }

    public void facebook() {
        String uri = "https://www.facebook.com/Job-Management-2126265037390832";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        startActivity(intent);
    }

    public void phone(String phone) {

        //Intent intent = new Intent(Intent.ACTION_DIAL); trả về sdt nhập...
        //import android.Manifest; SDK 27
        //nhớ cấp quyền app

        Intent phoneIntent = new Intent(Intent.ACTION_CALL);
        phoneIntent.setData(Uri.parse("tel:" + phone));
        if (ActivityCompat.checkSelfPermission(InformationActivity.this,
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        startActivity(phoneIntent);
    }

    private void checkAndRequestPermissions() {
        String[] permissions = new String[]{
                Manifest.permission.CALL_PHONE,
                Manifest.permission.GET_ACCOUNTS,
                Manifest.permission.READ_CONTACTS
        };
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(permission);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 1);
        }
    }
}