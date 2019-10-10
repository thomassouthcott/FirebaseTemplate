package com.thomassouthcott.firebasetemplate.activities.login;

import android.content.Intent;
import android.os.Bundle;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.thomassouthcott.firebasetemplate.activities.base.BaseActivity;
import com.thomassouthcott.firebasetemplate.R;
import com.thomassouthcott.firebasetemplate.activities.profile.ProfileActivity;
import com.thomassouthcott.firebasetemplate.activities.register.RegisterActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends BaseActivity implements View.OnClickListener, LoginContract.View {

    Button btnLogin, btnRegister;
    EditText editEmail, editPassword;
    private LoginPresenter mLoginPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        checkLoginStatus();
        initViews();
    }
    @Override
    protected void initViews() {
        super.initViews();
        btnLogin = findViewById(R.id.btn_login_Login);
            btnLogin.setOnClickListener(this);
        btnRegister = findViewById(R.id.btn_register_Login);
            btnRegister.setOnClickListener(this);
        editEmail = findViewById(R.id.edit_email_Login);
        editPassword = findViewById(R.id.edit_password_Login);

        mLoginPresenter = new LoginPresenter(this);
    }

    public void checkLoginStatus() {
        if(FirebaseAuth.getInstance().getCurrentUser() != null) {
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login_Login:
                checkLoginDetails();
                break;
            case R.id.btn_register_Login:
                moveToRegisterActivity();
                break;
        }
    }

    private void checkLoginDetails() {
        if(!TextUtils.isEmpty(editEmail.getText().toString()) && !TextUtils.isEmpty(editPassword.getText().toString())){
            initLogin(editEmail.getText().toString(), editPassword.getText().toString());
        }else{
            if(TextUtils.isEmpty(editEmail.getText().toString())){
                editEmail.setError("Please enter a valid email");
            }if(TextUtils.isEmpty(editPassword.getText().toString())){
                editPassword.setError("Please enter password");
            }
        }
    }

    private void initLogin(String email, String password) {
        showDialog();
        mLoginPresenter.login(this, email, password);
    }

    private void moveToRegisterActivity() {
        Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
        startActivity(intent);
        LoginActivity.this.overridePendingTransition(0,0);
        finish();
    }

    @Override
    public void onSuccess(FirebaseUser firebaseUser) {
        dismissDialog();
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
        finish();
    }
}
