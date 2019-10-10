package com.thomassouthcott.firebasetemplate.activities.register;

import android.app.Activity;
import android.net.Uri;

import com.google.firebase.auth.FirebaseUser;
import com.thomassouthcott.firebasetemplate.activities.base.BaseContract;

public interface RegisterContract  extends BaseContract {
    interface View extends BaseContract.View {
        void onSuccess(FirebaseUser firebaseUser);
    }

    interface Presenter extends BaseContract.Presenter{
        void register(Activity activity, String email, String username, String password, Uri profile);
    }

    interface Intractor extends BaseContract.Interactor {
        void register(Activity activity, String email, String username, String password, Uri profile);
    }

    interface Listener extends BaseContract.Listener {
        void onSuccess(FirebaseUser firebaseUser);
    }
}
