package aunguyen.quanlycongviec.Activity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
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

public class ManageMyAccountActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbarMyAccount;

    private ImageView imgAvartar;
    private ImageView imgEdit;

    private EditText edtFullName;
    private TextView tvAccountType;
    private TextView tvOffice;
    private TextView tvBirthday;
    private EditText edtPhone;
    private EditText edtAddress;

    private FirebaseDatabase database;

    private boolean flag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_my_account);

        setUpFirebase();

        setUpToolbar();

        addControls();

        addEvents();

        loadData();
    }

    private void addEvents() {
        imgEdit.setOnClickListener(this);
        tvBirthday.setOnClickListener(this);
    }

    private void loadData() {
        SharedPreferences preferences = this.getSharedPreferences(Constant.PREFERENCE_NAME, MODE_PRIVATE);

        String id = preferences.getString(Constant.PREFERENCE_KEY_ID, null);
        if (id != null) {
            DatabaseReference myRef = database.getReference(Constant.NODE_NHAN_VIEN).child(id);
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    EmployeeObject employeeObject = dataSnapshot.getValue(EmployeeObject.class);

                    if (employeeObject != null){
                        Glide.with(getApplicationContext())
                                .load(employeeObject.getUrlAvatar())
                                .into(imgAvartar);

                        edtFullName.setText(employeeObject.getNameEmployee());
                        if ((employeeObject.getAccountType()).equals("0")) {
                            tvAccountType.setText(getString(R.string.message_admin));
                        } else if ((employeeObject.getAccountType()).equals("1")) {
                            tvAccountType.setText(getString(R.string.message_employee));
                        }
                        tvOffice.setText("Office "+employeeObject.getOfficeEmployee());
                        tvBirthday.setText(employeeObject.getBirthdayEmployee());
                        edtPhone.setText(employeeObject.getPhoneEmployee());
                        edtAddress.setText(employeeObject.getAddressEmployee());
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

    private void setUpFirebase() {

        database = FirebaseDatabase.getInstance();
    }

    private void setUpToolbar() {
        toolbarMyAccount = findViewById(R.id.toolbar_my_account);
        setSupportActionBar(toolbarMyAccount);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void addControls() {

        imgAvartar = findViewById(R.id.img_avatar);

        edtFullName = findViewById(R.id.edt_full_name);
        tvAccountType = findViewById(R.id.tv_type_account);
        tvOffice = findViewById(R.id.tv_office);
        tvBirthday = findViewById(R.id.tv_birthday);
        edtPhone = findViewById(R.id.edt_phone);
        edtAddress = findViewById(R.id.edt_address);

        imgEdit = findViewById(R.id.img_edit);

        edtEnable(false);

    }

    private void edtEnable(boolean answer) {
        edtFullName.setEnabled(answer);
        tvBirthday.setEnabled(answer);
        edtPhone.setEnabled(answer);
        edtAddress.setEnabled(answer);
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
        }
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
            imgEdit.setImageResource(R.drawable.ic_save);
        }else{
            upFirebase();
        }
    }

    private void upFirebase() {
        SharedPreferences preferences = this.getSharedPreferences(Constant.PREFERENCE_NAME, MODE_PRIVATE);
        String id = preferences.getString(Constant.PREFERENCE_KEY_ID, null);
        if (id != null) {

            String birthday = tvBirthday.getText().toString();
            String name =  edtFullName.getText().toString();
            String phone = edtPhone.getText().toString();
            String address = edtAddress.getText().toString();

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

            flag = true;
            edtEnable(false);
            imgEdit.setImageResource(R.drawable.ic_edit);

        }
    }

    @Override
    public void onBackPressed() {

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
