package com.thomassouthcott.firebasetemplate.activities.profile;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;
import com.thomassouthcott.firebasetemplate.activities.base.BaseActivity;
import com.thomassouthcott.firebasetemplate.R;
import com.thomassouthcott.firebasetemplate.activities.login.LoginActivity;
import com.thomassouthcott.firebasetemplate.models.UserModel;

public class ProfileActivity extends BaseActivity
    implements ProfileContract.View, View.OnClickListener, TextWatcher {

    ImageButton btnSave;
    Button btnLogout;
    FloatingActionButton fabChooseImage;
    ImageView imgProfile;
    EditText editUsername, editEmail;
    private ProfilePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initViews();
        presenter = new ProfilePresenter(this);
        showDialog();
        presenter.getUser(FirebaseAuth.getInstance().getCurrentUser().getUid());
    }

    @Override
    protected void initViews() {
        super.initViews();
        setTitle("Profile");
        this.setFinishOnTouchOutside(false);
        btnSave = findViewById(R.id.btn_save_Profile);
            btnSave.setOnClickListener(this);
        btnLogout = findViewById(R.id.btn_logout_Profile);
            btnLogout.setOnClickListener(this);
        fabChooseImage = findViewById(R.id.fab_choose_image_Profile);
            fabChooseImage.setOnClickListener(this);
        imgProfile = findViewById(R.id.img_profile_picture_Profile);
        editUsername = findViewById(R.id.edit_username_Profile);
            editUsername.addTextChangedListener(this);
        editEmail = findViewById(R.id.edit_email_Profile);
    }

    @Override
    public void updateUser(UserModel userModel) {
        editUsername.setText(userModel.getUsername());
        btnSave.setEnabled(false);
        editEmail.setText(userModel.getEmail());
        if(userModel.getProfileurl() != null) {
            Picasso.get().load(Uri.parse(userModel.getProfileurl())).into(imgProfile);
        }
        dismissDialog();
    }

    @Override
    public void onLogoutListener() {
        dismissDialog();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.fab_choose_image_Profile:
                ImagePicker.Companion.with(this)
                        .cropSquare()
                        .compress(1024)         //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)  //Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
                break;
            case R.id.btn_save_Profile:
                if(!TextUtils.isEmpty(editUsername.getText().toString())) {
                    showDialog();
                    presenter.changeUsername(editUsername.getText().toString());
                }
                break;
            case R.id.btn_logout_Profile:
                showDialog();
                presenter.logout();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            showDialog();
            presenter.changeProfileImage(data.getData());
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.Companion.getError(data), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if(!FirebaseAuth.getInstance().getCurrentUser().getDisplayName().equals(s.toString())) {
            btnSave.setVisibility(View.VISIBLE);
            btnSave.setEnabled(true);

        } else {
            btnSave.setVisibility(View.GONE);
            btnSave.setEnabled(false);
        }
    }
}
