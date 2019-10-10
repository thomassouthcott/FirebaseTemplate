package com.thomassouthcott.firebasetemplate.activities.login;

import android.app.Activity;

import com.google.firebase.auth.FirebaseUser;
import com.thomassouthcott.firebasetemplate.activities.base.BaseContract;

public interface LoginContract extends BaseContract {
    interface View extends BaseContract.View{
        void onSuccess(FirebaseUser firebaseUser);
    }

    interface Presenter extends BaseContract.Presenter{
        void login(Activity activity, String email, String password);
    }

    interface Intractor extends BaseContract.Interactor{
        void login(Activity activity, String email, String password);
    }

    interface Listener extends BaseContract.Listener{
        void onSuccess(FirebaseUser firebaseUser);
    }
}
