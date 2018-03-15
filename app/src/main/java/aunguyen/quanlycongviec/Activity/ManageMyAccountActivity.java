package aunguyen.quanlycongviec.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import aunguyen.quanlycongviec.R;

public class ManageMyAccountActivity extends AppCompatActivity {

    private Toolbar toolbarMyAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_my_account);

        setUpToolbar();
    }

    private void setUpToolbar(){
        toolbarMyAccount = findViewById(R.id.toolbar_my_account);
        setSupportActionBar(toolbarMyAccount);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
