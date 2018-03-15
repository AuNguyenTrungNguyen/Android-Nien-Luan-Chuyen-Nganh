package aunguyen.quanlycongviec.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import aunguyen.quanlycongviec.R;

public class ManageMyEmployeesActivity extends AppCompatActivity {

    private Toolbar toolbarMyEmployees;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_my_employees);

        setUpToolbar();
    }

    private void setUpToolbar(){
        toolbarMyEmployees = findViewById(R.id.toolbar_my_employees);
        setSupportActionBar(toolbarMyEmployees);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
