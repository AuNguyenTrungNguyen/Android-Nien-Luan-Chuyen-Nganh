package aunguyen.quanlycongviec.Activity;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import aunguyen.quanlycongviec.Object.Constant;
import aunguyen.quanlycongviec.Object.EmployeeObject;
import aunguyen.quanlycongviec.R;

public class AddEmployeeActivity extends AppCompatActivity implements View.OnClickListener{

    private Toolbar toolbarAddEmployee;

    private EditText edtUsername;
    private EditText edtPassword;
    private EditText edtFullName;

    private RadioButton rdbAdmin;
    private RadioButton rdbEmployee;

    private TextView tvBirthday;

    private RadioButton rdbMale;
    private RadioButton rdbFemale;

    private EditText edtPhone;
    private EditText edtAddress;

    private Button btnRegister;

    private FirebaseAuth mAuth;
    private FirebaseDatabase databaseEmployee;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_employee);

        setUpToolbar();

        addControls();

        addEvents();
    }

    private void addControls() {

        databaseEmployee = FirebaseDatabase.getInstance();
        myRef = databaseEmployee.getReference(Constant.NODE_NHAN_VIEN);
        mAuth = FirebaseAuth.getInstance();

        edtUsername = findViewById(R.id.edt_username_employee);
        edtPassword = findViewById(R.id.edt_password_employee);

        rdbAdmin = findViewById(R.id.rdb_admin);
        rdbEmployee = findViewById(R.id.rdb_employee);
        edtFullName = findViewById(R.id.edt_full_name_employee);

        edtPhone = findViewById(R.id.edt_phone_employee);
        edtAddress = findViewById(R.id.edt_address_employee);

        tvBirthday = findViewById(R.id.tv_birthday_employee);
        rdbMale = findViewById(R.id.rdb_male_employee);
        rdbFemale = findViewById(R.id.rdb_female_employee);

        //Default
        rdbMale.setChecked(true);
        rdbEmployee.setChecked(true);

        btnRegister = findViewById(R.id.btn_register_employee);


    }

    private void addEvents() {
        btnRegister.setOnClickListener(this);
        tvBirthday.setOnClickListener(this);
    }

    private void setUpToolbar(){
        toolbarAddEmployee = findViewById(R.id.toolbar_add_employee);
        setSupportActionBar(toolbarAddEmployee);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setBirthday() {
        String birthday = tvBirthday.getText().toString();
        int dayDef = 1;
        int mouthDef = 0;
        int yearDef = 1995;

        if(!birthday.equals(getString(R.string.birth_day))){
            String split[] = birthday.split("/");
            dayDef =  Integer.parseInt(split[0]);
            mouthDef =  Integer.parseInt(split[1]) - 1;
            yearDef =  Integer.parseInt(split[2]);
        }

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

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_register_employee:
                signUp();
                break;

            case R.id.tv_birthday_employee:
                setBirthday();
                break;
        }
    }

    private void signUp() {
        SharedPreferences preferences = AddEmployeeActivity.this.getSharedPreferences(Constant.PREFERENCE_NAME, MODE_PRIVATE);

        final String id = preferences.getString(Constant.PREFERENCE_KEY_ID, null);
        final String domain = preferences.getString(Constant.PREFERENCE_DOMAIN, null);

        final String username = edtUsername.getText().toString();
        final String password = edtPassword.getText().toString();
        final String fullName = edtFullName.getText().toString();

        final String phone = edtPhone.getText().toString();
        final String address = edtAddress.getText().toString();

        final DatabaseReference referenceEmployee;

        referenceEmployee = databaseEmployee.getReference(Constant.NODE_NHAN_VIEN);

        if(!username.equals("") && !password.equals("") && !fullName.equals("")){
            mAuth.createUserWithEmailAndPassword(username+domain, password)
                    .addOnCompleteListener(AddEmployeeActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();

                                EmployeeObject employeeObject = new EmployeeObject();


                                if (rdbAdmin.isChecked()){
                                    employeeObject.setAccountType("0");
                                }else{
                                    employeeObject.setAccountType("1");
                                }

                                employeeObject.setIdEmployee(user.getUid());
                                employeeObject.setIdManage(id);
                                employeeObject.setUsernameEmployee(username+domain);

                                if(!phone.equals("")){
                                    employeeObject.setPhoneEmployee(phone);
                                }else{
                                    employeeObject.setPhoneEmployee("");
                                }

                                if(!address.equals("")){
                                    employeeObject.setAddressEmployee(address);
                                }else{
                                    employeeObject.setAddressEmployee("");
                                }

                                employeeObject.setNameEmployee(fullName);

                                if (rdbMale.isChecked()){
                                    employeeObject.setGenderEmployee("Nam");
                                    employeeObject.setUrlAvatar(Constant.URL_MALE);
                                }else{
                                    employeeObject.setGenderEmployee("Nu");
                                    employeeObject.setUrlAvatar(Constant.URL_FEMALE);
                                }

                                employeeObject.setBirthdayEmployee("01/01/1990");

                                referenceEmployee.child(user.getUid()).setValue(employeeObject);
                                Toast.makeText(AddEmployeeActivity.this, "Dang ky thanh cong!!!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
}
