package aunguyen.quanlycongviec.Activity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import aunguyen.quanlycongviec.Object.Constant;
import aunguyen.quanlycongviec.Object.EmployeeObject;
import aunguyen.quanlycongviec.R;

public class DetailAccountActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbarDetailAccount;

    private ImageView imgAvatar;
    private ImageView imgEdit;

    private EditText edtFullName;
    private TextView tvAccountType;
    private TextView tvBirthday;
    private EditText edtPhone;
    private EditText edtAddress;

    private Button btnStatistic;

    private FirebaseDatabase database;

    private boolean flag = true;
    private boolean check = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_account);
        setUpFirebase();

        setUpToolbar();

        addControls();

        addEvents();

        loadData();
    }

    private void addEvents() {
        imgEdit.setOnClickListener(this);
        tvBirthday.setOnClickListener(this);
        tvAccountType.setOnClickListener(this);
        btnStatistic.setOnClickListener(this);
    }

    private void loadData() {
        if(getIntent()!=null){
            String id = getIntent().getStringExtra("ID");
            if (id != null) {
                DatabaseReference myRef = database.getReference(Constant.NODE_NHAN_VIEN).child(id);
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        EmployeeObject employeeObject = dataSnapshot.getValue(EmployeeObject.class);

                        Glide.with(getApplicationContext())
                                .load(employeeObject.getUrlAvatar())
                                .into(imgAvatar);

                        edtFullName.setText(employeeObject.getNameEmployee());
                        if ((employeeObject.getAccountType()).equals("0")) {
                            tvAccountType.setText(getString(R.string.message_admin));
                        } else if ((employeeObject.getAccountType()).equals("1")) {
                            tvAccountType.setText(getString(R.string.message_employee));
                        }
                        tvBirthday.setText(employeeObject.getBirthdayEmployee());
                        edtPhone.setText(employeeObject.getPhoneEmployee());
                        edtAddress.setText(employeeObject.getAddressEmployee());
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                    }
                });
            }
        }

    }

    private void setUpFirebase() {

        database = FirebaseDatabase.getInstance();
    }

    private void setUpToolbar() {
        toolbarDetailAccount = findViewById(R.id.toolbar_detail_account);
        setSupportActionBar(toolbarDetailAccount);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void addControls() {

        imgAvatar = findViewById(R.id.img_avatar);
        edtFullName = findViewById(R.id.edt_full_name);
        tvAccountType = findViewById(R.id.tv_type_account);
        tvBirthday = findViewById(R.id.tv_birthday);
        edtPhone = findViewById(R.id.edt_phone);
        edtAddress = findViewById(R.id.edt_address);
        imgEdit = findViewById(R.id.img_edit);
        btnStatistic = findViewById(R.id.btn_statistic);

        edtEnable(false);

    }

    private void edtEnable(boolean answer) {
        edtFullName.setEnabled(answer);
        tvBirthday.setEnabled(answer);
        edtPhone.setEnabled(answer);
        edtAddress.setEnabled(answer);
        tvAccountType.setEnabled(answer);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_edit:
                edit();
                break;

            case R.id.tv_birthday:
                setBirthday();
                break;

            case R.id.tv_type_account:
                showDialog();
                break;

            case R.id.btn_statistic:
                Intent intent = new Intent(DetailAccountActivity.this, StatisticActivity.class);
                String id = getIntent().getStringExtra("ID");
                intent.putExtra("ID",id);
                SharedPreferences preferences = getSharedPreferences(Constant.PREFERENCE_NAME, MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(Constant.KEY_ID_EMPLOYEE_STATISTIC, id);
                editor.apply();
                startActivity(intent);
        }
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(getResources().getString(R.string.dialog_choose_account_type));

        builder.setPositiveButton(getString(R.string.message_employee), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                tvAccountType.setText(getString(R.string.message_employee));
                dialogInterface.dismiss();
            }
        });

        builder.setNegativeButton(getString(R.string.message_admin), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                tvAccountType.setText(getString(R.string.message_admin));
                dialogInterface.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void setBirthday() {
        String birthday = tvBirthday.getText().toString();
        int dayDef, mouthDef, yearDef;

        String split[] = birthday.split("/");
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

                tvBirthday.setText(day + "/" + month + "/" + year);

            }
        }, yearDef, mouthDef, dayDef);

        dialog.show();
    }

    private void edit() {
        if (flag){
            edtEnable(true);
            flag = false;
            check = true;
            imgEdit.setImageResource(R.drawable.ic_save);

        }else{
            upFirebase();
        }
    }

    private void upFirebase() {
        if(getIntent()!=null) {
            String id = getIntent().getStringExtra("ID");
            if (id != null) {

                String birthday = tvBirthday.getText().toString();
                String name = edtFullName.getText().toString();
                String accountType = tvAccountType.getText().toString();
                String phone = edtPhone.getText().toString();
                String address = edtAddress.getText().toString();
                String type;
                if(accountType.equals(getString(R.string.message_employee))){
                    type = "1";
                }else {
                    type = "0";
                }

                database.getReference(Constant.NODE_NHAN_VIEN).child(id).
                        child(Constant.KEY_ADDRESS).
                        setValue(address);
                database.getReference(Constant.NODE_NHAN_VIEN).child(id).
                        child(Constant.KEY_BIRTHDAY).
                        setValue(birthday);
                database.getReference(Constant.NODE_NHAN_VIEN).child(id).
                        child(Constant.KEY_PHONE).
                        setValue(phone);
                database.getReference(Constant.NODE_NHAN_VIEN).child(id).
                        child(Constant.KEY_NAME).
                        setValue(name);
                database.getReference(Constant.NODE_NHAN_VIEN).child(id).
                        child(Constant.KEY_ACCOUNT_TYPE).
                        setValue(type);

                flag = true;
                edtEnable(false);
                imgEdit.setImageResource(R.drawable.ic_edit);
                check = false;
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
}
