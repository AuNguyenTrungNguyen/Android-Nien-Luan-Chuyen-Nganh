package aunguyen.quanlycongviec.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import aunguyen.quanlycongviec.R;

public class AboutMeActivity extends AppCompatActivity {

    private Toolbar toolbarAboutMe;

    private ListView lstAboutMe;
    private String[] list = {"Job Management in Facebook",
            "Phone: 0949593457", "Email: nienluanchuyennganh@gmail.com"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_me);

        setUpToolbar();

        addControls();

        addEvents();
    }

    private void addEvents() {
        lstAboutMe.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        facebook();
                        break;
                    case 1:
                        phone();
                        break;
                    case 2:
                        email();
                        break;
                }
            }
        });
    }

    private void addControls() {
        lstAboutMe = (ListView) findViewById(R.id.lst_about_me);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);
        lstAboutMe.setAdapter(adapter);
    }

    private void setUpToolbar(){
        toolbarAboutMe = findViewById(R.id.toolbar_about_me);
        setSupportActionBar(toolbarAboutMe);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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

    public void phone() {

        //Intent intent = new Intent(Intent.ACTION_DIAL);
        //import android.Manifest; SDK 27

        Intent phoneIntent = new Intent(Intent.ACTION_CALL);
        phoneIntent.setData(Uri.parse("tel:0949593457"));
        if (ActivityCompat.checkSelfPermission(AboutMeActivity.this,
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        startActivity(phoneIntent);
    }
}
