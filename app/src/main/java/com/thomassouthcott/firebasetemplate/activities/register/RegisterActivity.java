package com.thomassouthcott.firebasetemplate.activities.register;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.thomassouthcott.firebasetemplate.activities.base.BaseActivity;
import com.thomassouthcott.firebasetemplate.activities.login.LoginActivity;
import com.thomassouthcott.firebasetemplate.R;
import com.google.firebase.auth.FirebaseUser;


import com.github.dhaval2404.imagepicker.ImagePicker;
import com.thomassouthcott.firebasetemplate.activities.profile.ProfileActivity;

public class RegisterActivity extends BaseActivity implements View.OnClickListener, RegisterContract.View {
    Button btnRegister, btnLogin, btnPicture;
    EditText editEmail, editUsername, editPassword, editRepeatPassword;
    private RegisterPresenter mRegisterPresenter;
    ImageView imgProfile;
    private Uri profileUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initViews();
    }
    protected void initViews() {
        super.initViews();
        btnRegister = findViewById(R.id.btn_register_Register);
            btnRegister.setOnClickListener(this);
        btnLogin = findViewById(R.id.btn_login_Register);
            btnLogin.setOnClickListener(this);
        btnPicture = findViewById(R.id.btn_picture_Register);
            btnPicture.setOnClickListener(this);
        editEmail = findViewById(R.id.edit_email_Register);
        editUsername = findViewById(R.id.edit_username_Register);
        editPassword = findViewById(R.id.edit_password_Register);
        editRepeatPassword = findViewById(R.id.edit_repeat_password_Register);
        imgProfile = findViewById(R.id.img_profile_picture_Register);
        mRegisterPresenter = new RegisterPresenter(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btn_register_Register:
                checkRegisterDetails();
                break;
            case R.id.btn_login_Register:
                moveToLoginActivity();
                break;
            case R.id.btn_picture_Register:
                chooseImage();
                break;
        }
    }

    private void checkRegisterDetails() {
        if(!TextUtils.isEmpty(editEmail.getText().toString())
                && !TextUtils.isEmpty(editUsername.getText().toString())
                && !TextUtils.isEmpty(editPassword.getText().toString())
                && !TextUtils.isEmpty(editRepeatPassword.getText().toString())
                && editPassword.getText().toString().equals(editRepeatPassword.getText().toString())
                && profileUri != null){
            initRegister(editEmail.getText().toString(), editUsername.getText().toString(), editPassword.getText().toString(),profileUri);
        }else{
            if(TextUtils.isEmpty(editEmail.getText().toString())){
                editEmail.setError("Please enter a valid email");
            }if(TextUtils.isEmpty(editUsername.getText().toString())){
                editUsername.setError("Please enter a username");
            }if(!editPassword.getText().toString().equals(editRepeatPassword.getText().toString())){
                editPassword.setError("Please enter matching passwords");
                editRepeatPassword.setError("Please enter matching passwords");
            } else {
                if(TextUtils.isEmpty(editPassword.getText().toString())){
                    editPassword.setError("Please enter password");
                }if(TextUtils.isEmpty(editRepeatPassword.getText().toString())){
                    editRepeatPassword.setError("Please enter password");
                }
            }
        }
    }

    private void moveToLoginActivity() {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        RegisterActivity.this.overridePendingTransition(0,0);
        finish();
    }

    private void chooseImage() {
        ImagePicker.Companion.with(this)
                .cropSquare()
                .compress(1024)         //Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)  //Final image resolution will be less than 1080 x 1080(Optional)
                .start();
    }

    private void initRegister(String email, String username, String password, Uri profileImage) {
        showDialog();
        mRegisterPresenter.register(this, email, username, password, profileImage);
    }

    @Override
    public void onSuccess(FirebaseUser firebaseUser) {
        dismissDialog();
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finish();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            profileUri = data.getData();
            imgProfile.setImageURI(profileUri);
            /*//You can get File object from intent
            val file:File = ImagePicker.getFile(data)

            //You can also get File Path from intent
            val filePath:String = ImagePicker.getFilePath(data)*/
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.Companion.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
        }
    }
}
