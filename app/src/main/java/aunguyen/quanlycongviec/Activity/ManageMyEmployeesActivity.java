package aunguyen.quanlycongviec.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import aunguyen.quanlycongviec.Adapter.AddEmployeeAdapter;
import aunguyen.quanlycongviec.Object.Constant;
import aunguyen.quanlycongviec.Object.EmployeeObject;
import aunguyen.quanlycongviec.R;

public class ManageMyEmployeesActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbarMyEmployees;

    private FloatingActionButton btnAddEmployee;

    private RecyclerView rvEmployee;
    private List<EmployeeObject> listEmployee;
    private AddEmployeeAdapter employeeAdapter;

    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_my_employees);

        setUpToolbar();

        addControls();

        addEvents();

        loadData();
    }


    private void loadData() {
        SharedPreferences preferences = this.getSharedPreferences(Constant.PREFERENCE_NAME, MODE_PRIVATE);

        final String id = preferences.getString(Constant.PREFERENCE_KEY_ID, null);
        if (id != null) {
            DatabaseReference myRef = database.getReference(Constant.NODE_NHAN_VIEN);
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        EmployeeObject employeeObject = snapshot.getValue(EmployeeObject.class);

                        if(id.equals(employeeObject.getIdManage())){
                            listEmployee.add(employeeObject);
                            employeeAdapter.notifyDataSetChanged();
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.i("ABC", "Failed to read value.", error.toException());
                }
            });
        }

    }


    //Thêm mặc định
    private void addControls() {

        database = FirebaseDatabase.getInstance();

        btnAddEmployee = findViewById(R.id.btn_add_employee);

        rvEmployee = findViewById(R.id.rv_employees);
        listEmployee = new ArrayList<>();
        employeeAdapter = new AddEmployeeAdapter(this, listEmployee);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        rvEmployee.setAdapter(employeeAdapter);
        rvEmployee.setLayoutManager(manager);

    }

    private void addEvents() {
        btnAddEmployee.setOnClickListener(this);

        //rvJob.setOnClickListener();

    }

    private void setUpToolbar() {
        toolbarMyEmployees = findViewById(R.id.toolbar_my_employees);
        setSupportActionBar(toolbarMyEmployees);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
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
