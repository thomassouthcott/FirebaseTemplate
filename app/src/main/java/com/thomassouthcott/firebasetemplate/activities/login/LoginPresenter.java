package com.thomassouthcott.firebasetemplate.activities.login;

import android.app.Activity;

import com.google.firebase.auth.FirebaseUser;
import com.thomassouthcott.firebasetemplate.activities.base.BasePresenter;

public class LoginPresenter extends BasePresenter implements LoginContract.Presenter, LoginContract.Listener {
    private LoginContract.View mLoginView;
    private LoginInteractor mLoginInteractor;

    public LoginPresenter(LoginContract.View mLoginView) {
        super(mLoginView);
        this.mLoginView = mLoginView;
        mLoginInteractor = new LoginInteractor(this);
    }

    @Override
    public void login(Activity activity, String email, String password) {
        mLoginInteractor.login(activity, email, password);

    }

    @Override
    public void onSuccess(FirebaseUser firebaseUser) {
        mLoginView.onSuccess(firebaseUser);

    }
}
