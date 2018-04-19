package aunguyen.quanlycongviec.Activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import aunguyen.quanlycongviec.Object.Constant;
import aunguyen.quanlycongviec.Object.EmployeeObject;
import aunguyen.quanlycongviec.R;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbarSignUp;

    private EditText edtUsername;
    private EditText edtDomain;
    private EditText edtFullName;

    private boolean isShow = false;
    private TextInputLayout layoutPassword;
    private TextInputEditText edtPassword;


    private TextView tvBirthday;

    private RadioButton rdbMale;
    private RadioButton rdbFemale;

    private EditText edtPhone;
    private EditText edtAddress;

    private Button btnSignUp;

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        setUpFirebase();

        setUpToolbar();

        addControls();

        addEvents();

    }

    private void setUpFirebase() {
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
    }

    private void addEvents() {
        btnSignUp.setOnClickListener(this);
        tvBirthday.setOnClickListener(this);
        layoutPassword.setOnClickListener(this);
    }

    private void addControls() {
        edtUsername = findViewById(R.id.edt_username);
        edtPassword = findViewById(R.id.edt_password);
        layoutPassword = findViewById(R.id.lo_password);
        edtDomain = findViewById(R.id.edt_domain);
        edtFullName = findViewById(R.id.edt_full_name);

        edtPhone = findViewById(R.id.edt_phone);
        edtAddress = findViewById(R.id.edt_address);

        tvBirthday = findViewById(R.id.tv_birthday);
        rdbMale = findViewById(R.id.rdb_male);
        rdbFemale = findViewById(R.id.rdb_female);

        //Default
        rdbMale.setChecked(true);

        btnSignUp = findViewById(R.id.btn_sign_up);
    }

    private void setUpToolbar() {
        //Setup layout_toolbar
        toolbarSignUp = findViewById(R.id.toolbar_sign_up);
        setSupportActionBar(toolbarSignUp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_sign_up:
                signUp();
                break;

            case R.id.tv_birthday:
                setBirthday();
                break;

            case R.id.lo_password:
                showPass();
                break;
        }
    }

    private void showPass() {
        if(!isShow){
            edtPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            isShow = true;
            layoutPassword.setPasswordVisibilityToggleEnabled(false);
        }else{
            edtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD );
            isShow = false;
            layoutPassword.setPasswordVisibilityToggleEnabled(true);
        }
        edtPassword.setSelection(edtPassword.length());
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

    private void signUp() {

        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle(getResources().getString(R.string.dialog));
        progressDialog.setCancelable(false);
        progressDialog.show();

        final boolean[] isFirst = {true};

        final List<String> listDomain = new ArrayList<>();

        final String username = edtUsername.getText().toString();
        final String password = edtPassword.getText().toString();
        final String domain = edtDomain.getText().toString();
        final String fullName = edtFullName.getText().toString();

        final String phone = edtPhone.getText().toString();
        final String address = edtAddress.getText().toString();

        final DatabaseReference referenceDomain, referenceEmployee;

        referenceDomain = database.getReference(Constant.NODE_DOMAIN);
        referenceEmployee = database.getReference(Constant.NODE_NHAN_VIEN);

        //Check domain exist
        referenceDomain.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    String domain = snapshot.getValue(String.class);
                    listDomain.add(domain);
                }

                if(listDomain.contains(domain) && isFirst[0]){
                    Toast.makeText(SignUpActivity.this, getResources().getString(R.string.toast_domain_exist), Toast.LENGTH_SHORT).show();
                    isFirst[0] = false;
                    progressDialog.dismiss();
                }else if(isFirst[0]){
                    if(!username.equals("") && !password.equals("") && !domain.equals("") && !fullName.equals("")){
                        mAuth.createUserWithEmailAndPassword(username+domain, password)
                                .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            FirebaseUser user = mAuth.getCurrentUser();

                                            EmployeeObject employeeObject = new EmployeeObject();
                                            employeeObject.setAccountType("0");
                                            employeeObject.setIdEmployee(user.getUid());
                                            employeeObject.setIdManage("");
                                            employeeObject.setUsernameEmployee(username+domain);
                                            //employeeObject.setOfficeEmployee(getResources().getString(R.string.office_default));

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
                                                employeeObject.setGenderEmployee(getResources().getString(R.string.male));
                                                employeeObject.setUrlAvatar(Constant.URL_MALE);
                                            }else{
                                                employeeObject.setGenderEmployee(getResources().getString(R.string.female));
                                                employeeObject.setUrlAvatar(Constant.URL_FEMALE);
                                            }

                                            employeeObject.setBirthdayEmployee("01/01/1995");

                                            referenceEmployee.child(user.getUid()).setValue(employeeObject);
                                            referenceDomain.push().setValue(domain);

                                            progressDialog.dismiss();
                                            Toast.makeText(SignUpActivity.this, getResources().getString(R.string.toast_sign_up_success), Toast.LENGTH_SHORT).show();
                                            isFirst[0] = false;

                                            SharedPreferences preferences = SignUpActivity.this.getSharedPreferences(Constant.PREFERENCE_NAME, MODE_PRIVATE);
                                            SharedPreferences.Editor editor = preferences.edit();
                                            editor.putString(Constant.PREFERENCE_DOMAIN, domain);
                                            editor.apply();

                                            startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
                                        }
                                    }
                                });
                    }else{
                        progressDialog.dismiss();
                        Toast.makeText(SignUpActivity.this, getResources().getString(R.string.toast_data_fail), Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
