package aunguyen.quanlycongviec.Activity;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import aunguyen.quanlycongviec.Adapter.JobAdapter;
import aunguyen.quanlycongviec.Object.Constant;
import aunguyen.quanlycongviec.Object.JobObject;
import aunguyen.quanlycongviec.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbarMain;

    private FloatingActionButton btnAddJod;

    private RecyclerView rvJob;
    private List<JobObject> listJobs;
    private JobAdapter jobAdapter;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpToolbar();

        addControls();

        init();

        addEvents();

        loadDataFromFireBase();

    }

    private void loadDataFromFireBase() {
        progressDialog = new ProgressDialog(this);
        progressDialog.show();
        SharedPreferences preferences = this.getSharedPreferences(Constant.PREFERENCE_NAME, MODE_PRIVATE);
        final String id = preferences.getString(Constant.PREFERENCE_KEY_ID, null);

        if (id != null) {
            DatabaseReference myRef = FirebaseDatabase.getInstance().getReference(Constant.NODE_CONG_VIEC);
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        JobObject jobObject = snapshot.getValue(JobObject.class);

                        if (id.equals(jobObject.getIdManageJob())) {
                            listJobs.add(jobObject);
                            jobAdapter.notifyDataSetChanged();
                        }
                    }

                    progressDialog.dismiss();
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    Log.i("ABC", "Failed to read value.", error.toException());
                }
            });

        } else {
            progressDialog.dismiss();
            Log.i("ANTN", "ID Manage is null!");
        }
    }

    //Thêm những sự kiện click ...
    private void addEvents() {
        btnAddJod.setOnClickListener(this);

        //rvJob.setOnClickListener();

    }

    //Thêm mặc định
    private void addControls() {
        btnAddJod = findViewById(R.id.btn_add_job);

        rvJob = findViewById(R.id.rv_job);
        listJobs = new ArrayList<>();

        jobAdapter = new JobAdapter(this, listJobs);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        rvJob.setAdapter(jobAdapter);
        rvJob.setLayoutManager(manager);
    }

    private void setUpToolbar() {
        //Setup layout_toolbar
        toolbarMain = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbarMain);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void manageMyAccount() {
        Intent intent = new Intent(this, ManageMyAccountActivity.class);
        startActivity(intent);
    }

    private void manageEmployeesAccount() {
        Intent intent = new Intent(this, ManageMyEmployeesActivity.class);
        startActivity(intent);
    }

    private void information() {
        Intent intent = new Intent(this, InformationActivity.class);
        startActivity(intent);
    }

    private void aboutMe() {
        Intent intent = new Intent(this, AboutMeActivity.class);
        startActivity(intent);
    }

    private void signOut() {
        FirebaseAuth.getInstance().signOut();
        SharedPreferences preferences = this.getSharedPreferences(Constant.PREFERENCE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString(Constant.PREFERENCE_KEY_ID, null);
        editor.putString(Constant.PREFERENCE_DOMAIN, null);
        editor.apply();

        Intent intentSignIn = new Intent(MainActivity.this, SignInActivity.class);
        startActivity(intentSignIn);
        finish();
    }

    private void init() {
        //Setup drawer
        drawerLayout = findViewById(R.id.drawer);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.syncState();
        toolbarMain.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        //Setup navigation
        navigationView = findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.mn_my_account:
                        manageMyAccount();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.mn_my_employees:
                        manageEmployeesAccount();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.mn_sign_out:
                        signOut();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.mn_information:
                        information();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.mn_about_me:
                        aboutMe();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                }
                return true;
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_add_job:
                addJob();
                break;
        }
    }

    private void addJob() {
        Intent intentAddJob = new Intent(this, AddJobActivity.class);
        startActivity(intentAddJob);
    }
}
