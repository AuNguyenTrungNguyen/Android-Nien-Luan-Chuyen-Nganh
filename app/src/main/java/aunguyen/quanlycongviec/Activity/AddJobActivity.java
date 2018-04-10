package aunguyen.quanlycongviec.Activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import aunguyen.quanlycongviec.Adapter.EmployeeSelectedAdapter;
import aunguyen.quanlycongviec.Object.Constant;
import aunguyen.quanlycongviec.Object.EmployeeObject;
import aunguyen.quanlycongviec.Object.JobObject;
import aunguyen.quanlycongviec.Object.StatusJob;
import aunguyen.quanlycongviec.R;

public class AddJobActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbarAddJob;

    private EditText edtTitle;
    private EditText edtDescription;
    private TextView tvStart;
    private TextView tvEnd;
    private Button btnAddEmployee;
    private Button btnAddJob;

    private RecyclerView rvEmployee;
    private List<EmployeeObject> listEmployees;
    private EmployeeSelectedAdapter employeeAdapter;

    private int maxSize = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_job);

        setUpToolbar();

        addControls();

        addEvents();
    }

    private void addEvents() {
        tvStart.setOnClickListener(this);
        tvEnd.setOnClickListener(this);
        btnAddJob.setOnClickListener(this);
        btnAddEmployee.setOnClickListener(this);
    }

    private void addControls() {
        edtTitle = findViewById(R.id.edt_title);
        edtDescription = findViewById(R.id.edt_description);

        tvStart = findViewById(R.id.tv_start);
        tvEnd = findViewById(R.id.tv_end);
        btnAddEmployee = findViewById(R.id.btn_add_employee);
        btnAddJob = findViewById(R.id.btn_add_job);

        //setup Recycle
        rvEmployee = findViewById(R.id.rv_employee);
        listEmployees = new ArrayList<>();
        employeeAdapter = new EmployeeSelectedAdapter(this, listEmployees);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        rvEmployee.setAdapter(employeeAdapter);
        rvEmployee.setLayoutManager(manager);
    }

    private void setUpToolbar() {
        toolbarAddJob = findViewById(R.id.toolbar_add_job);
        setSupportActionBar(toolbarAddJob);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_start:
                setTime(tvStart, getResources().getString(R.string.job_start));
                break;
            case R.id.tv_end:
                setTime(tvEnd, getResources().getString(R.string.job_end));
                break;
            case R.id.btn_add_employee:
                addEmployee();
                break;
            case R.id.btn_add_job:
                addJob();
                break;
        }
    }

    private void addEmployee() {
        Log.i("ANTN", "MaxSize: " + maxSize);
        if (listEmployees.size() < maxSize) {
            Intent intent = new Intent(this, SelectEmployeeToJobActivity.class);
            intent.putExtra("LIST_EMPLOYEE_ADDED", (Serializable) listEmployees);
            startActivityForResult(intent, Constant.REQUEST_CODE);
        } else {
            Toast.makeText(this, "Bạn đã thêm toàn bộ nhân viên!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.REQUEST_CODE && resultCode == Constant.RESULT_CODE && data != null) {

            ArrayList<EmployeeObject> list = (ArrayList<EmployeeObject>) data.getSerializableExtra("LIST_EMPLOYEE");
            listEmployees.addAll(list);
            employeeAdapter.notifyDataSetChanged();

            maxSize = data.getIntExtra("MAX", -1);
        }
    }

    private void addJob() {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setTitle(getResources().getString(R.string.dialog));
        String title = edtTitle.getText().toString();
        String description = edtDescription.getText().toString();
        String timeStart = tvStart.getText().toString();
        String timeEnd = tvEnd.getText().toString();

        if (!title.equals("")
                && !description.equals("")
                && !timeStart.equals(getResources().getString(R.string.job_start))
                && !timeEnd.equals(getResources().getString(R.string.job_end))
                && listEmployees.size() > 0) {

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date date = new Date();
            String now = dateFormat.format(date);

            if (compareDate(now, timeStart) && compareDate(timeStart, timeEnd)) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference reference = database.getReference().child(Constant.NODE_CONG_VIEC);

                String idJob = reference.push().getKey();

                SharedPreferences preferences = getSharedPreferences(Constant.PREFERENCE_NAME, MODE_PRIVATE);
                String idManage = preferences.getString(Constant.PREFERENCE_KEY_ID, "");

                List<StatusJob> listStatus = new ArrayList();

                for (EmployeeObject object : listEmployees) {
                    StatusJob statusJob = new StatusJob();
                    statusJob.setIdMember(object.getIdEmployee());
                    statusJob.setStatus(Constant.NOT_RECEIVED);
                    listStatus.add(statusJob);
                }

                JobObject jobObject = new JobObject();
                jobObject.setIdJob(idJob);
                jobObject.setIdManageJob(idManage);
                jobObject.setTitleJob(title);
                jobObject.setDescriptionJob(description);
                jobObject.setStartDateJob(timeStart);
                jobObject.setEndDateJob(timeEnd);
                jobObject.setStatusJob(Constant.NOT_RECEIVED);
                jobObject.setListIdMember(listStatus);

                dialog.show();

                reference.child(idJob).setValue(jobObject).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        dialog.dismiss();
                        Toast.makeText(AddJobActivity.this, "Success!", Toast.LENGTH_SHORT).show();
                    }
                });


            } else {
                Toast.makeText(this, "Date is fail!", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(this, "Data is empty!", Toast.LENGTH_SHORT).show();
        }

    }

    private void setTime(final TextView textView, String text) {
        String title = textView.getText().toString();

        //Lấy ngày hiện tại
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        String now = dateFormat.format(date);
        String splitNow[] = now.split("/");
        int dayDef = Integer.parseInt(splitNow[0]);
        int mouthDef = Integer.parseInt(splitNow[1]) - 1;
        int yearDef = Integer.parseInt(splitNow[2]);

        //Chọn lại ngày
        if (!title.equals(text)) {
            String split[] = title.split("/");
            dayDef = Integer.parseInt(split[0]);
            mouthDef = Integer.parseInt(split[1]) - 1;
            yearDef = Integer.parseInt(split[2]);
        }

        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                String day, month, year;
                day = (i2 < 10) ? "0" + i2 : "" + i2;

                i1 += 1;
                month = (i1 < 10) ? "0" + i1 : "" + i1;
                year = "" + i;

                textView.setText(day + "/" + month + "/" + year);

            }
        }, yearDef, mouthDef, dayDef);

        dialog.show();
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
