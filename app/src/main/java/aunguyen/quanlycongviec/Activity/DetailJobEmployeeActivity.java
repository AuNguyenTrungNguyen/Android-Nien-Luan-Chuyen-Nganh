package aunguyen.quanlycongviec.Activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import aunguyen.quanlycongviec.Adapter.MemberAdapter;
import aunguyen.quanlycongviec.Object.Constant;
import aunguyen.quanlycongviec.Object.JobObject;
import aunguyen.quanlycongviec.Object.StatusJob;
import aunguyen.quanlycongviec.R;

public class DetailJobEmployeeActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbarDetailJob;

    private FirebaseDatabase database;

    private EditText edtTitle;
    private EditText edtDescription;

    private TextView tvStart;
    private TextView tvEnd;

    private Button btnReceiveJob;
    private Button btnCompleteJob;

    private RecyclerView rvEmployee;
    private List<StatusJob> listMember;
    MemberAdapter memberAdapter;

    private boolean check = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_job_employee);
        setUpToolbar();

        setUpFirebase();

        addControls();

        addEvents();

        loadData();

    }

    private void setUpToolbar() {
        toolbarDetailJob = findViewById(R.id.toolbar_detail_job_employee);
        setSupportActionBar(toolbarDetailJob);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void addControls() {
        edtTitle = findViewById(R.id.edt_title);
        edtDescription = findViewById(R.id.edt_description);

        tvStart = findViewById(R.id.tv_start);
        tvEnd = findViewById(R.id.tv_end);

        btnReceiveJob = findViewById(R.id.btn_receive_job);
        btnCompleteJob = findViewById(R.id.btn_complete_job);

        rvEmployee = findViewById(R.id.rv_employee);


        edtEnable(false);
    }

    private void edtEnable(boolean answer) {
        edtTitle.setEnabled(answer);
        edtDescription.setEnabled(answer);
    }

    private void addEvents() {
        btnReceiveJob.setOnClickListener(this);
        btnCompleteJob.setOnClickListener(this);
    }

    private void setUpFirebase() {

        database = FirebaseDatabase.getInstance();
    }

    private void loadData() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle(getResources().getString(R.string.dialog));
        progressDialog.setCancelable(false);
        if (getIntent() != null) {
            String idJob = getIntent().getStringExtra("IDJob");
            if (idJob != null) {

                progressDialog.show();
                final SharedPreferences preferences = getSharedPreferences(Constant.PREFERENCE_NAME, MODE_PRIVATE);
                final String idEmployee = preferences.getString(Constant.PREFERENCE_KEY_ID, null);

                DatabaseReference myRef = database.getReference(Constant.NODE_CONG_VIEC).child(idJob);
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        JobObject jobObject = dataSnapshot.getValue(JobObject.class);
                        if (jobObject != null) {

                            edtTitle.setText(jobObject.getTitleJob());
                            edtDescription.setText(jobObject.getDescriptionJob());

                            tvStart.setText(getString(R.string.job_start) + ": " + jobObject.getStartDateJob());
                            tvEnd.setText(getString(R.string.job_end) + ": " + jobObject.getEndDateJob());

                            for (int i = 0; i < jobObject.getListIdMember().size(); i++) {
                                if (idEmployee.equals(jobObject.getListIdMember().get(i).getIdMember())) {
                                    String status[] = jobObject.getListIdMember().get(i).getStatus().split("/");
                                    if (status[1].equals(Constant.PAST_DEADLINE) || status[1].equals(Constant.COMPLETE)) {
                                        btnReceiveJob.setEnabled(false);
                                        btnCompleteJob.setEnabled(false);
                                    } else {
                                        if (status[0].equals(Constant.RECEIVED)) {
                                            btnReceiveJob.setEnabled(false);
                                            btnCompleteJob.setEnabled(true);
                                        } else {
                                            btnReceiveJob.setEnabled(true);
                                            btnCompleteJob.setEnabled(false);
                                        }
                                    }
                                }
                            }

                            listMember = jobObject.getListIdMember();
                            memberAdapter = new MemberAdapter(DetailJobEmployeeActivity.this, listMember);
                            RecyclerView.LayoutManager manager = new LinearLayoutManager(DetailJobEmployeeActivity.this, LinearLayoutManager.VERTICAL, false);

                            rvEmployee.setAdapter(memberAdapter);
                            rvEmployee.setLayoutManager(manager);
                            progressDialog.dismiss();
                        }else {
                            progressDialog.dismiss();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        progressDialog.dismiss();
                    }
                });
            }
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_receive_job:
                receiveJob();
                break;
            case R.id.btn_complete_job:
                completeJob();
                break;
        }
    }

    private void completeJob() {
        if (getIntent() != null) {
            final String idJob = getIntent().getStringExtra("IDJob");
            DatabaseReference myRef = database.getReference(Constant.NODE_CONG_VIEC).child(idJob);
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    JobObject jobObject = dataSnapshot.getValue(JobObject.class);
                    SharedPreferences preferences = getSharedPreferences(Constant.PREFERENCE_NAME, MODE_PRIVATE);
                    final String idEmployee = preferences.getString(Constant.PREFERENCE_KEY_ID, null);

                    if (jobObject != null) {

                        for (int i = 0; i < jobObject.getListIdMember().size(); i++) {
                            if (idEmployee.equals(jobObject.getListIdMember().get(i).getIdMember())) {

                                String status[] = jobObject.getListIdMember().get(i).getStatus().split("/");
                                database.getReference(Constant.NODE_CONG_VIEC)
                                        .child(idJob)
                                        .child("listIdMember")
                                        .child(String.valueOf(i))
                                        .child("status")
                                        .setValue(status[0] + "/" + Constant.COMPLETE);

                                break;
                            }
                        }

                        listMember = jobObject.getListIdMember();
                        memberAdapter = new MemberAdapter(DetailJobEmployeeActivity.this, listMember);
                        RecyclerView.LayoutManager manager = new LinearLayoutManager(DetailJobEmployeeActivity.this, LinearLayoutManager.VERTICAL, false);

                        rvEmployee.setAdapter(memberAdapter);
                        rvEmployee.setLayoutManager(manager);
                    }

                }

                @Override
                public void onCancelled(DatabaseError error) {
                }
            });
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    JobObject jobObject = dataSnapshot.getValue(JobObject.class);
                    int count = 0;

                    if (jobObject != null) {

                        for (int i = 0; i < jobObject.getListIdMember().size(); i++) {

                            String status[] = jobObject.getListIdMember().get(i).getStatus().split("/");

                            if (!status[1].equals(Constant.COMPLETE)) {
                                break;
                            } else {
                                count++;
                            }
                        }

                        String status[] = jobObject.getStatusJob().split("/");

                        if (count == jobObject.getListIdMember().size()) {
                            database.getReference(Constant.NODE_CONG_VIEC)
                                    .child(idJob)
                                    .child("statusJob")
                                    .setValue(status[0] + "/" + Constant.COMPLETE);
                        }
                    }

                }

                @Override
                public void onCancelled(DatabaseError error) {
                }
            });
        }
    }

    private void receiveJob() {
        if (getIntent() != null) {
            final String idJob = getIntent().getStringExtra("IDJob");
            DatabaseReference myRef = database.getReference(Constant.NODE_CONG_VIEC).child(idJob);
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    JobObject jobObject = dataSnapshot.getValue(JobObject.class);
                    SharedPreferences preferences = getSharedPreferences(Constant.PREFERENCE_NAME, MODE_PRIVATE);
                    final String idEmployee = preferences.getString(Constant.PREFERENCE_KEY_ID, null);

                    if (jobObject != null) {

                        for (int i = 0; i < jobObject.getListIdMember().size(); i++) {
                            if (idEmployee.equals(jobObject.getListIdMember().get(i).getIdMember())) {

                                String status[] = jobObject.getListIdMember().get(i).getStatus().split("/");
                                database.getReference(Constant.NODE_CONG_VIEC)
                                        .child(idJob)
                                        .child("listIdMember")
                                        .child(String.valueOf(i))
                                        .child("status")
                                        .setValue(Constant.RECEIVED + "/" + status[1]);

                                /*database.getReference(Constant.NODE_CONG_VIEC)
                                        .child(idJob)
                                        .child("listIdMember")
                                        .child(String.valueOf(i))
                                        .child("notify")
                                        .setValue(Constant.NOTIFY);*/

                                break;
                            }
                        }

                        listMember = jobObject.getListIdMember();
                        memberAdapter = new MemberAdapter(DetailJobEmployeeActivity.this, listMember);
                        RecyclerView.LayoutManager manager = new LinearLayoutManager(DetailJobEmployeeActivity.this, LinearLayoutManager.VERTICAL, false);

                        rvEmployee.setAdapter(memberAdapter);
                        rvEmployee.setLayoutManager(manager);
                    }

                }

                @Override
                public void onCancelled(DatabaseError error) {
                }
            });
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    JobObject jobObject = dataSnapshot.getValue(JobObject.class);
                    int count = 0;

                    if (jobObject != null) {

                        for (int i = 0; i < jobObject.getListIdMember().size(); i++) {

                            String status[] = jobObject.getListIdMember().get(i).getStatus().split("/");

                            if (status[0].equals(Constant.NOT_RECEIVED)) {
                                break;
                            } else {
                                count++;
                            }
                        }

                        String status[] = jobObject.getStatusJob().split("/");

                        if (count == jobObject.getListIdMember().size()) {
                            database.getReference(Constant.NODE_CONG_VIEC)
                                    .child(idJob)
                                    .child("statusJob")
                                    .setValue(Constant.RECEIVED + "/" + status[1]);
                        }
                    }

                }

                @Override
                public void onCancelled(DatabaseError error) {
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        if (!check) {
            finish();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(false);

            builder.setMessage(getString(R.string.message_edit_and_save));

            builder.setPositiveButton(getString(R.string.message_positive_save), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                }
            });

            builder.setNegativeButton(getString(R.string.message_negative_save), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }
}