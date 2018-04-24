package aunguyen.quanlycongviec.Activity;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import aunguyen.quanlycongviec.Adapter.ViewPagerAdapter;
import aunguyen.quanlycongviec.Fragment.NotReceiveFragment;
import aunguyen.quanlycongviec.Fragment.ReceiveFragment;
import aunguyen.quanlycongviec.Fragment.TotalFragment;
import aunguyen.quanlycongviec.Object.Constant;
import aunguyen.quanlycongviec.R;

public class StatisticActivity extends AppCompatActivity implements View.OnClickListener{

    private Toolbar toolbarGraphTotal;

    private ViewPager viewPager;
    private TabLayout tabLayout;

    private Button btnStartTime;
    private Button btnEndTime;
    private Button btnStatistic;

    private TotalFragment totalFragment;
    private ReceiveFragment receiveFragment;
    private NotReceiveFragment notReceiveFragment;
    private ViewPagerAdapter adapterViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);

        setUpToolbar();

        addControls();

        setupViewPager();

        addEvents();

    }

    private void setupViewPager(){
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);

        totalFragment = new TotalFragment();
        receiveFragment = new ReceiveFragment();
        notReceiveFragment = new NotReceiveFragment();

        String id = getIntent().getStringExtra("ID");
        String startTime = btnStartTime.getText().toString();
        String endTime = btnEndTime.getText().toString();

        SharedPreferences preferences = getSharedPreferences(Constant.PREFERENCE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(Constant.KEY_ID_STATISTIC, id);
        editor.putString(Constant.KEY_START_STATISTIC, startTime);
        editor.putString(Constant.KEY_END_STATISTIC, endTime);
        editor.apply();

        adapterViewPager = new ViewPagerAdapter(getSupportFragmentManager());
        adapterViewPager.addFragment(totalFragment, getResources().getString(R.string.lable_tap_total));
        adapterViewPager.addFragment(receiveFragment, getResources().getString(R.string.lable_tap_receive));
        adapterViewPager.addFragment(notReceiveFragment, getResources().getString(R.string.lable_tap_not_receive));
        viewPager.setAdapter(adapterViewPager);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setUpToolbar() {
        toolbarGraphTotal = findViewById(R.id.toolbar_graph_total);
        setSupportActionBar(toolbarGraphTotal);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void addControls() {

        btnStartTime = findViewById(R.id.btn_start_time);
        btnEndTime = findViewById(R.id.btn_end_time);
        btnStatistic = findViewById(R.id.btn_statistic);

        edtEnable(false);

    }

    private void addEvents() {
        btnStartTime.setOnClickListener(this);
        btnEndTime.setOnClickListener(this);
        btnStatistic.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_start_time:
                startTime();
                break;

            case R.id.btn_end_time:
                endTime();
                checkTime();
                break;

            case R.id.btn_statistic:
                statistic();
                break;
        }

    }

    private void checkTime(){
        if(compareDate(btnStartTime.getText().toString(),btnEndTime.getText().toString())){
            edtEnable(true);
        }else {
            edtEnable(false);
        }
    }

    private void startTime() {
        String startTime = btnStartTime.getText().toString();
        int dayDef, mouthDef, yearDef;

        String split[] = startTime.split("/");
        dayDef =  Integer.parseInt(split[0]);
        mouthDef =  Integer.parseInt(split[1]) - 1;
        yearDef =  Integer.parseInt(split[2]);

        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                String day, month, year;
                day = (i2 < 10) ? "0"+i2 : ""+i2;

                i1+=1;
                month = (i1 < 10) ? "0"+i1 : ""+i1;
                year = ""+i;

                btnStartTime.setText(day + "/" + month + "/" + year);

                checkTime();

            }
        }, yearDef, mouthDef, dayDef);

        dialog.show();
    }

    private void endTime() {
        String endTime = btnEndTime.getText().toString();
        int dayDef, mouthDef, yearDef;

        String split[] = endTime.split("/");
        dayDef =  Integer.parseInt(split[0]);
        mouthDef =  Integer.parseInt(split[1]) - 1;
        yearDef =  Integer.parseInt(split[2]);

        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                String day, month, year;
                day = (i2 < 10) ? "0"+i2 : ""+i2;

                i1+=1;
                month = (i1 < 10) ? "0"+i1 : ""+i1;
                year = ""+i;
                btnEndTime.setText(day + "/" + month + "/" + year);
                checkTime();

            }
        }, yearDef, mouthDef, dayDef);

        dialog.show();
    }

    private void statistic() {

        String id = getIntent().getStringExtra("ID");
        String startTime = btnStartTime.getText().toString();
        String endTime = btnEndTime.getText().toString();

        SharedPreferences preferences = getSharedPreferences(Constant.PREFERENCE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(Constant.KEY_ID_STATISTIC, id);
        editor.putString(Constant.KEY_START_STATISTIC, startTime);
        editor.putString(Constant.KEY_END_STATISTIC, endTime);
        editor.apply();

        if(totalFragment.getContext() != null){
            totalFragment.statistic();
        }

        if(receiveFragment.getContext() != null){
            receiveFragment.statistic();
        }

        if(notReceiveFragment.getContext() != null){
            notReceiveFragment.statistic();
        }

        edtEnable(false);
    }

    private void edtEnable(boolean answer) {
        btnStatistic.setEnabled(answer);
    }

    private boolean compareDate(String start, String end) {
        String splitStart[] = start.split("/");
        int dayStart = Integer.parseInt(splitStart[0]);
        int mouthStart = Integer.parseInt(splitStart[1]) - 1;
        int yearStart = Integer.parseInt(splitStart[2]);

        String splitEnd[] = end.split("/");
        int dayEnd = Integer.parseInt(splitEnd[0]);
        int mouthEnd = Integer.parseInt(splitEnd[1]) - 1;
        int yearEnd = Integer.parseInt(splitEnd[2]);

        return (yearEnd >= yearStart && mouthEnd >= mouthStart && dayEnd >= dayStart);
    }
}
