package aunguyen.quanlycongviec.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
    private EditText edtPassword;
    private EditText edtDomain;

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
    }

    private void addControls() {
        edtUsername = findViewById(R.id.edt_username);
        edtPassword = findViewById(R.id.edt_password);
        edtDomain = findViewById(R.id.edt_domain);

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
        }
    }

    private void signUp() {

        final boolean[] isFirst = {true};

        final List<String> listDomain = new ArrayList<>();

        final String username = edtUsername.getText().toString();
        final String password = edtPassword.getText().toString();
        final String domain = edtDomain.getText().toString();

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
                    Toast.makeText(SignUpActivity.this, "Domain cua ban da ton tai!!!", Toast.LENGTH_SHORT).show();
                    isFirst[0] = false;
                }else if(isFirst[0]){
                    if(!username.equals("") && !password.equals("")){
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

                                            referenceEmployee.child(user.getUid()).setValue(employeeObject);
                                            referenceDomain.push().setValue(domain);
                                            Toast.makeText(SignUpActivity.this, "Dang ky thanh cong!!!", Toast.LENGTH_SHORT).show();
                                            isFirst[0] = false;
                                            startActivity(new Intent(SignUpActivity.this, SignInActivity.class));

                                        }
                                    }
                                });
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
