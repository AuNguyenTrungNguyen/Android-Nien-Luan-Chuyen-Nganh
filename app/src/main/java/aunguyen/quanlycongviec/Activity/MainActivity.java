package aunguyen.quanlycongviec.Activity;


import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
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
import aunguyen.quanlycongviec.Object.EmployeeObject;
import aunguyen.quanlycongviec.Object.JobObject;
import aunguyen.quanlycongviec.Object.StatusJob;
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
    private boolean notification;

    @Override
    protected void onStart() {
        super.onStart();

        if(notification){
            SharedPreferences preferences = this.getSharedPreferences(Constant.PREFERENCE_NAME, MODE_PRIVATE);
            final String id = preferences.getString(Constant.PREFERENCE_KEY_ID, null);

            if (id != null) {
                DatabaseReference myRef = FirebaseDatabase.getInstance().getReference(Constant.NODE_CONG_VIEC);
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        int count = 0;

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            JobObject jobObject = snapshot.getValue(JobObject.class);

                            List<StatusJob> list = jobObject.getListIdMember();
                            String status[] = jobObject.getStatusJob().split("/");

                            for(int i = 0; i < list.size(); i++){
                                if (id.equals(list.get(i).getIdMember())
                                        && status[0].equals(Constant.NOT_RECEIVED)
                                        && !status[1].equals(Constant.PAST_DEADLINE)) {
                                    Intent intent = new Intent(MainActivity.this, InformationActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this, 0, intent, 0);
                                    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(MainActivity.this)
                                            .setSmallIcon(R.drawable.ic_mail)
                                            .setContentTitle("Thông báo")
                                            .setContentText("Có công việc chưa nhận: " + jobObject.getTitleJob())
                                            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                            .setContentIntent(pendingIntent)
                                            .setAutoCancel(true);;
                                    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(MainActivity.this);
                                    notificationManager.notify(count++, mBuilder.build());
                                    break;
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                    }
                });
            }
            notification = false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpToolbar();

        addControls();

        setupNavigation();

        addEvents();

    }

    private void loadDataFromFireBase() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle(getResources().getString(R.string.dialog));
        progressDialog.setCancelable(false);
        progressDialog.show();
        SharedPreferences preferences = this.getSharedPreferences(Constant.PREFERENCE_NAME, MODE_PRIVATE);
        final String id = preferences.getString(Constant.PREFERENCE_KEY_ID, null);

        if (id != null) {
            DatabaseReference myRef = FirebaseDatabase.getInstance().getReference(Constant.NODE_CONG_VIEC);
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    listJobs.clear();
                    jobAdapter.notifyDataSetChanged();

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        JobObject jobObject = snapshot.getValue(JobObject.class);

                        if (id.equals(jobObject.getIdManageJob())) {
                            listJobs.add(jobObject);
                        }
                    }
                    progressDialog.dismiss();
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    progressDialog.dismiss();
                    Log.i("ANTN", "onCancelled() - Main", error.toException());
                }
            });

        } else {
            progressDialog.dismiss();
            Log.i("ANTN", "ID Manage is null!");
        }
    }

    private void setupNavigation() {
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

        navigationView = findViewById(R.id.navigation);

        SharedPreferences preferences = this.getSharedPreferences(Constant.PREFERENCE_NAME, MODE_PRIVATE);
        String id = preferences.getString(Constant.PREFERENCE_KEY_ID, null);

        if (id != null) {
            DatabaseReference myRef = FirebaseDatabase.getInstance().getReference(Constant.NODE_NHAN_VIEN).child(id);
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    EmployeeObject employeeObject = dataSnapshot.getValue(EmployeeObject.class);

                    if (employeeObject != null){
                        String accountType = employeeObject.getAccountType();

                        if (accountType.equals("0")){
                            navigationAdmin();
                        }else{
                            navigationEmployee();
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {

                }
            });
        }

    }

    private void navigationAdmin(){

        btnAddJod.setVisibility(View.VISIBLE);

        navigationView.getMenu().clear();
        navigationView.inflateMenu(R.menu.item_navigation_admin);

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

                    case R.id.mn_my_job:
                        myJob();
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

                }
                return true;
            }
        });
    }

    private void navigationEmployee(){

        btnAddJod.setVisibility(View.INVISIBLE);

        navigationView.getMenu().clear();
        navigationView.inflateMenu(R.menu.item_navigation_employee);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {

                switch (item.getItemId()) {

                    case R.id.mn_statistic:
                        statistic();
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

                }
                return true;
            }
        });

    }

    private void addEvents() {
        btnAddJod.setOnClickListener(this);
    }

    private void addControls() {
        btnAddJod = findViewById(R.id.btn_add_job);

        rvJob = findViewById(R.id.rv_job);
        listJobs = new ArrayList<>();

        jobAdapter = new JobAdapter(this, listJobs);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        rvJob.setAdapter(jobAdapter);
        rvJob.setLayoutManager(manager);

        notification = true;
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

    private void myJob() {
        Intent intent = new Intent(this, MyJobActivity.class);
        startActivity(intent);
    }

    private void statistic() {
        Intent intent = new Intent(this, StatisticActivity.class);
        startActivity(intent);
    }

    private void information() {
        Intent intent = new Intent(this, InformationActivity.class);
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

    private void addJob() {
        Intent intentAddJob = new Intent(this, AddJobActivity.class);
        startActivity(intentAddJob);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadDataFromFireBase();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add_job:
                addJob();
                break;
        }
    }

}
