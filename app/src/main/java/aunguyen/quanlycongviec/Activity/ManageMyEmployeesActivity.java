package aunguyen.quanlycongviec.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import aunguyen.quanlycongviec.R;

public class ManageMyEmployeesActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbarMyEmployees;

    private FloatingActionButton btnAddEmployee;

    private RecyclerView rvEmployee;
    private List<String> listEmployee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_my_employees);

        setUpToolbar();

        addControls();

        addEvents();
    }

    //Thêm mặc định
    private void addControls() {
        btnAddEmployee = findViewById(R.id.btn_add_employee);

        rvEmployee = findViewById(R.id.rv_job);
        listEmployee = new ArrayList<>();
        //adapterJob = new AdapterJob();
    }

    private void addEvents() {
        btnAddEmployee.setOnClickListener(this);

        //rvJob.setOnClickListener();

    }

    private void setUpToolbar(){
        toolbarMyEmployees = findViewById(R.id.toolbar_my_employees);
        setSupportActionBar(toolbarMyEmployees);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_add_employee:
                addEmployee();
                break;
        }
    }

    private void addEmployee() {
        Intent intentAddEmployee = new Intent(this, AddEmployeeActivity.class);
        startActivity(intentAddEmployee);
    }
}
