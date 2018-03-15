package aunguyen.quanlycongviec.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import aunguyen.quanlycongviec.R;

public class AddJobActivity extends AppCompatActivity {

    private Toolbar toolbarAddJob;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_job);

        setUpToolbar();
    }

    private void setUpToolbar(){
        toolbarAddJob = findViewById(R.id.toolbar_add_job);
        setSupportActionBar(toolbarAddJob);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
