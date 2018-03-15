package aunguyen.quanlycongviec.Activity;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import aunguyen.quanlycongviec.Object.Constant;
import aunguyen.quanlycongviec.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbarMain;

    private FloatingActionButton btnAddJod;

    private RecyclerView rvJob;
    private List<String> listJob;
    //private AdapterJob adapterJob;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpToolbar();

        addControls();

        init();

        addEvents();

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
        listJob = new ArrayList<>();
        //adapterJob = new AdapterJob();
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

        Intent intentSignIn = new Intent(MainActivity.this, SignInActivity.class);
        startActivity(intentSignIn);
        finish();

        editor.apply();
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
