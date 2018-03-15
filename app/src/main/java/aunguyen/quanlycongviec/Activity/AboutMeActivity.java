package aunguyen.quanlycongviec.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import aunguyen.quanlycongviec.R;

public class AboutMeActivity extends AppCompatActivity {

    private Toolbar toolbarAboutMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_me);

        setUpToolbar();
    }

    private void setUpToolbar(){
        toolbarAboutMe = findViewById(R.id.toolbar_about_me);
        setSupportActionBar(toolbarAboutMe);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
