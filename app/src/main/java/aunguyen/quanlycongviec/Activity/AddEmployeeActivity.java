package aunguyen.quanlycongviec.Activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

public class AddEmployeeActivity extends AppCompatActivity {

    private Toolbar toolbarAddEmployee;

    private EditText edtUsernameEmployee;
    private EditText edtPasswordEmployee;
    private Button btnSignUpEmployee;

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

        edtUsernameEmployee = findViewById(R.id.edt_username_employee);
        edtPasswordEmployee = findViewById(R.id.edt_password_employee);
        btnSignUpEmployee = findViewById(R.id.btn_sign_up_employee);

        mAuth = FirebaseAuth.getInstance();
    }

    private void addEvents() {
        btnSignUpEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = edtUsernameEmployee.getText().toString();
                String password = edtPasswordEmployee.getText().toString();

                if(!username.equals("") && !password.equals("")){
                    SharedPreferences preferences = AddEmployeeActivity.this.getSharedPreferences(Constant.PREFERENCE_NAME, MODE_PRIVATE);

                    username += preferences.getString(Constant.PREFERENCE_DOMAIN, "");
                    Log.i("TAG", username);
                    signUp(username, password);
                }
            }
        });
    }

    private void signUp(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();

                            EmployeeObject employeeObject = new EmployeeObject();
                            employeeObject.setAccountType("1");
                            employeeObject.setIdEmployee(user.getUid());
                            SharedPreferences preferences = getBaseContext().getSharedPreferences(Constant.PREFERENCE_NAME, MODE_PRIVATE);
                            String idManage = preferences.getString(Constant.PREFERENCE_KEY_ID, null);
                            employeeObject.setIdManage(idManage);

                            myRef.child(user.getUid()).setValue(employeeObject);

                        } else {
                            Log.i("TAG", "No Oke");
                        }
                    }
                });
    }

    private void setUpToolbar(){
        toolbarAddEmployee = findViewById(R.id.toolbar_add_employee);
        setSupportActionBar(toolbarAddEmployee);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
