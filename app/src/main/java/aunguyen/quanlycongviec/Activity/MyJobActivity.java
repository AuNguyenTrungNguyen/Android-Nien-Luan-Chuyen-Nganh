package aunguyen.quanlycongviec.Activity;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

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
import aunguyen.quanlycongviec.Object.StatusJob;
import aunguyen.quanlycongviec.R;

public class MyJobActivity extends AppCompatActivity {

    private Toolbar toolbarMyJob;
    private TextView tvMessage;

    private RecyclerView rvMyJob;
    private List<JobObject> listJobs;
    private JobAdapter jobAdapter;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_job);

        setUpToolbar();

        addControls();

        addEvents();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadDataFromFireBase();
    }

    private void setUpToolbar() {
        //Setup layout_toolbar
        toolbarMyJob = findViewById(R.id.toolbar_my_job);
        setSupportActionBar(toolbarMyJob);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void addControls() {
        rvMyJob = findViewById(R.id.rv_my_job);
        tvMessage = findViewById(R.id.tv_message);
        listJobs = new ArrayList<>();

        jobAdapter = new JobAdapter(this, listJobs);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        rvMyJob.setAdapter(jobAdapter);
        rvMyJob.setLayoutManager(manager);
    }

    private void addEvents() {}

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

                    listJobs.clear();
                    jobAdapter.notifyDataSetChanged();

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        JobObject jobObject = snapshot.getValue(JobObject.class);

                        assert jobObject != null;
                        List<StatusJob> list = jobObject.getListIdMember();

                        for (int i = 0; i < list.size(); i++) {
                            if (id.equals(list.get(i).getIdMember())) {
                                String status = list.get(i).getStatus();
                                jobObject.setStatusJob(status);
                                listJobs.add(jobObject);
                                jobAdapter.notifyDataSetChanged();
                                break;
                            }
                        }
                    }
                    if (listJobs.size() > 0){
                        tvMessage.setVisibility(View.GONE);
                    }else{
                        tvMessage.setVisibility(View.VISIBLE);
                    }

                    progressDialog.dismiss();
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    progressDialog.dismiss();
                }
            });

        } else {
            progressDialog.dismiss();
        }
    }

}
