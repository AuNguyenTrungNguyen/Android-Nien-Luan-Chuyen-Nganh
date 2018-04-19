package aunguyen.quanlycongviec.Activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

public class DetailJobActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbarDetailJob;

    private FirebaseDatabase database;

    private EditText edtTitle;
    private EditText edtDescription;

    private TextView tvStart;
    private TextView tvEnd;

    private Button btnEditJob;
    private Button btnDeleteJob;

    private RecyclerView rvEmployee;
    private List<StatusJob> listMember;
    MemberAdapter memberAdapter;

    private boolean flag = true;
    private boolean check = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_job);

        setUpToolbar();

        setUpFirebase();

        addControls();

        addEvents();

        loadData();

    }

    private void setUpToolbar() {
        toolbarDetailJob = findViewById(R.id.toolbar_detail_job);
        setSupportActionBar(toolbarDetailJob);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void addControls() {
        edtTitle = findViewById(R.id.edt_title);
        edtDescription = findViewById(R.id.edt_description);

        tvStart = findViewById(R.id.tv_start);
        tvEnd = findViewById(R.id.tv_end);

        btnEditJob = findViewById(R.id.btn_edit_job);
        btnDeleteJob = findViewById(R.id.btn_delete_job);

        rvEmployee = findViewById(R.id.rv_employee);




        edtEnable(false);
    }

    private void edtEnable(boolean answer) {
        edtTitle.setEnabled(answer);
        edtDescription.setEnabled(answer);
    }

    private void addEvents() {
        btnEditJob.setOnClickListener(this);
        btnDeleteJob.setOnClickListener(this);
    }
    private void setUpFirebase() {

        database = FirebaseDatabase.getInstance();
    }

    private void loadData() {
        if(getIntent()!=null) {
            String id = getIntent().getStringExtra("IDJob");
            Log.i("ABCE", id);
            if (id != null) {
                DatabaseReference myRef = database.getReference(Constant.NODE_CONG_VIEC).child(id);
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        JobObject jobObject = dataSnapshot.getValue(JobObject.class);
                        if(jobObject !=null){
                            edtTitle.setText(jobObject.getTitleJob());
                            edtDescription.setText(jobObject.getDescriptionJob());

                            tvStart.setText(getString(R.string.job_start) + ": " + jobObject.getStartDateJob());
                            tvEnd.setText(getString(R.string.job_end) + ": " + jobObject.getEndDateJob());

                            listMember = jobObject.getListIdMember();
                            memberAdapter = new MemberAdapter(DetailJobActivity.this,listMember);
                            RecyclerView.LayoutManager manager = new LinearLayoutManager(DetailJobActivity.this, LinearLayoutManager.VERTICAL, false);

                            rvEmployee.setAdapter(memberAdapter);
                            rvEmployee.setLayoutManager(manager);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                    }
                });
            }
        }

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_edit_job:
                edit();
                break;
            case R.id.btn_delete_job:
                deleteJob();
                break;
        }
    }

    private void edit() {
        if (flag){
            edtEnable(true);
            flag = false;
            check = true;
            btnEditJob.setText("SAVE");

        }else{
            save();
        }
    }

    private void save() {
        if(getIntent()!=null) {
            String id = getIntent().getStringExtra("IDJob");
            if (id != null) {
                String title = edtTitle.getText().toString();
                String description = edtDescription.getText().toString();

                if (!title.equals("") && !description.equals("")) {
                    JobObject jobObject = new JobObject();

                    database.getReference(Constant.NODE_CONG_VIEC)
                            .child(id)
                            .child(Constant.KEY_TITLE_JOB)
                            .setValue(title);

                    database.getReference(Constant.NODE_CONG_VIEC)
                            .child(id)
                            .child(Constant.KEY_DESCRIPTION_JOB)
                            .setValue(description);

                    flag = true;
                    edtEnable(false);
                    btnEditJob.setText("EDIT");
                    check = false;
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        if(!check){
            finish();
        }else {
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

    private void deleteJob() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);

        builder.setMessage(getString(R.string.message_delete_job));

        builder.setPositiveButton(getString(R.string.message_positive_delete_job), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        builder.setNegativeButton(getString(R.string.message_negative_delete_job), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialogInterface, int i) {

                if(getIntent()!= null) {
                    String id = getIntent().getStringExtra("IDJob");
                    Log.i("ABCE", id);
                    if (id != null) {
                        DatabaseReference myRef = database.getReference(Constant.NODE_CONG_VIEC).child(id);
                        myRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                dataSnapshot.getRef().removeValue();
                                Toast.makeText(DetailJobActivity.this, "Xóa thành công", Toast.LENGTH_SHORT).show();
                                finish();
                            }

                            @Override
                            public void onCancelled(DatabaseError error) {
                                dialogInterface.dismiss();
                                Toast.makeText(DetailJobActivity.this, "Xóa thất bại", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }else{
                    Toast.makeText(DetailJobActivity.this, "Xóa thất bại", Toast.LENGTH_SHORT).show();
                    dialogInterface.dismiss();
                }
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}