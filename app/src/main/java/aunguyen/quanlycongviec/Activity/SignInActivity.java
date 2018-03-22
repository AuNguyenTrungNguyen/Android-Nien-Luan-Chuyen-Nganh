package aunguyen.quanlycongviec.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import aunguyen.quanlycongviec.Object.Constant;
import aunguyen.quanlycongviec.R;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbarSignIn;

    private EditText edtUsername;
    private EditText edtPassword;
    private Button btnSignIn;
    private Button btnSignUp;

    //Firebase database
    private FirebaseDatabase databaseSignIn;
    private DatabaseReference referenceSignIn;

    //Firebase Auth
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        setUpToolbar();

        init();
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void init() {
        edtUsername = findViewById(R.id.edt_username);
        edtPassword = findViewById(R.id.edt_password);

        btnSignIn = findViewById(R.id.btn_sign_in);
        btnSignIn.setOnClickListener(this);

        btnSignUp = findViewById(R.id.btn_sign_up);
        btnSignUp.setOnClickListener(this);

        //Database
        databaseSignIn = FirebaseDatabase.getInstance();
        referenceSignIn = databaseSignIn.getReference();

        //Auth
        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sign_in:
                signIn();
                break;

            case R.id.btn_sign_up:
                signUp();
                break;
        }
    }

    private void signUp() {
        Intent intentSignUp = new Intent(this, SignUpActivity.class);
        startActivity(intentSignUp);
    }

    private void signIn() {

        String email = edtUsername.getText().toString();
        String password = edtPassword.getText().toString();

        if(!email.equals("") && !password.equals("")){

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                FirebaseUser user = mAuth.getCurrentUser();
                                String email = user.getEmail();
                                String domain = email.substring(email.indexOf('@'));
                                SharedPreferences preferences = SignInActivity.this.getSharedPreferences(Constant.PREFERENCE_NAME, MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString(Constant.PREFERENCE_DOMAIN, domain);
                                editor.apply();
                                updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(SignInActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                updateUI(null);
                            }
                        }
                    });

        }else{
            Toast.makeText(this, "Username hoặc Password rỗng!", Toast.LENGTH_SHORT).show();
        }
    }

    private void setUpToolbar(){
        toolbarSignIn = findViewById(R.id.toolbar_sign_in);
        setSupportActionBar(toolbarSignIn);
    }

    private void updateUI(FirebaseUser user) {
        SharedPreferences preferences = this.getSharedPreferences(Constant.PREFERENCE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        if (user != null) {
            String id = user.getUid();
            editor.putString(Constant.PREFERENCE_KEY_ID, id);

            Intent intentMain = new Intent(this, MainActivity.class);
            startActivity(intentMain);
            finish();

        } else {
            editor.putString(Constant.PREFERENCE_KEY_ID, null);
        }
        editor.apply();
    }


}
