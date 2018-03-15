package aunguyen.quanlycongviec.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import aunguyen.quanlycongviec.R;

public class AddEmployeeActivity extends AppCompatActivity {

    private Toolbar toolbarAddEmployee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_employee);

        setUpToolbar();
    }

    private void setUpToolbar(){
        toolbarAddEmployee = findViewById(R.id.toolbar_add_employee);
        setSupportActionBar(toolbarAddEmployee);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
