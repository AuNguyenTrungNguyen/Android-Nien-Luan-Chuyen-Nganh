package aunguyen.quanlycongviec.Activity;

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
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import aunguyen.quanlycongviec.Object.Constant;
import aunguyen.quanlycongviec.R;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbarSignIn;

    private EditText edtUsername;

    private boolean isShow = false;
    private TextInputLayout layoutPassword;
    private TextInputEditText edtPassword;

    private Button btnSignIn;
    private Button btnSignUp;

    //Firebase Auth
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        setUpFirebase();

        setUpToolbar();

        addControls();

        addEvents();
    }

    private void setUpFirebase() {
        mAuth = FirebaseAuth.getInstance();
    }

    private void addEvents() {
        btnSignIn.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);
        layoutPassword.setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void addControls() {
        edtUsername = findViewById(R.id.edt_username);
        edtPassword = findViewById(R.id.edt_password);
        layoutPassword = findViewById(R.id.lo_password);

        btnSignIn = findViewById(R.id.btn_sign_in);
        btnSignUp = findViewById(R.id.btn_sign_up);

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

            case R.id.lo_password:
                showPass();
                break;
        }
    }

    private void showPass() {
        if (!isShow) {
            edtPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            isShow = true;
            layoutPassword.setPasswordVisibilityToggleEnabled(false);
        } else {
            edtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            isShow = false;
            layoutPassword.setPasswordVisibilityToggleEnabled(true);
        }
        edtPassword.setSelection(edtPassword.length());
    }

    private void signUp() {
        Intent intentSignUp = new Intent(this, SignUpActivity.class);
        startActivity(intentSignUp);
    }

    private void signIn() {
        String email = edtUsername.getText().toString();
        String password = edtPassword.getText().toString();

        if (email.equals("")) {
            Toast.makeText(this, getString(R.string.toast_username_empty), Toast.LENGTH_SHORT).show();
        } else if (!checkInputPassword(password)) {
            Toast.makeText(this, getString(R.string.toast_password_incorrect), Toast.LENGTH_SHORT).show();
        } else {
            final ProgressDialog progressDialog;
            progressDialog = new ProgressDialog(this);
            progressDialog.setTitle(getResources().getString(R.string.dialog));
            progressDialog.setCancelable(false);
            progressDialog.show();

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                String email = user.getEmail();
                                String domain = email.substring(email.indexOf('@'));
                                SharedPreferences preferences = SignInActivity.this.getSharedPreferences(Constant.PREFERENCE_NAME, MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString(Constant.PREFERENCE_DOMAIN, domain);
                                editor.apply();
                                updateUI(user);
                                progressDialog.dismiss();
                            } else {
                                Toast.makeText(SignInActivity.this, getResources().getString(R.string.toast_account_incorrect),
                                        Toast.LENGTH_SHORT).show();
                                updateUI(null);
                                progressDialog.dismiss();
                            }
                        }
                    });
        }
    }

    private void setUpToolbar() {
        toolbarSignIn = findViewById(R.id.toolbar_sign_in);
        setSupportActionBar(toolbarSignIn);
    }

    private void updateUI(FirebaseUser user) {
        SharedPreferences preferences = this.getSharedPreferences(Constant.PREFERENCE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        if (user != null) {
            String idRef = preferences.getString(Constant.PREFERENCE_KEY_ID, "");
            String id = user.getUid();

            if (idRef.equals("")) {
                editor.putString(Constant.PREFERENCE_KEY_ID, id);

            }

            Intent intentMain = new Intent(this, MainActivity.class);
            startActivity(intentMain);
            finish();

        } else {
            editor.putString(Constant.PREFERENCE_KEY_ID, null);
        }
        editor.apply();
    }

    private boolean checkInputPassword(String pass) {

        pass = pass.trim();

        return !pass.equals("") && pass.length() >= 6;
    }

}
