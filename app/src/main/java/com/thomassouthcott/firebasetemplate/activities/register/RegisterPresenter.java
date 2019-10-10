package com.thomassouthcott.firebasetemplate.activities.register;

import android.app.Activity;
import android.net.Uri;

import com.google.firebase.auth.FirebaseUser;
import com.thomassouthcott.firebasetemplate.activities.base.BasePresenter;

public class RegisterPresenter extends BasePresenter implements RegisterContract.Presenter, RegisterContract.Listener {
    private RegisterContract.View mRegisterView;
    private RegisterInteractor mRegisterInteractor;


    public RegisterPresenter(RegisterContract.View registerView) {
        super(registerView);
        this.mRegisterView = registerView;
        this.mRegisterInteractor = new RegisterInteractor(this);
    }

    @Override
    public void register(Activity activity, String email, String username, String password, Uri profile) {
        mRegisterInteractor.register(activity,email, username,password, profile);
    }

    @Override
    public void onSuccess(FirebaseUser firebaseUser) {
        mRegisterView.onSuccess(firebaseUser);
    }
}
