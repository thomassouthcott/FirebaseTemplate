package com.thomassouthcott.firebasetemplate.activities.profile;

import android.net.Uri;

import com.thomassouthcott.firebasetemplate.activities.base.BaseContract;
import com.thomassouthcott.firebasetemplate.models.UserModel;

public interface ProfileContract extends BaseContract {
    interface View extends BaseContract.View {
        void updateUser(UserModel userModel);
        void onLogoutListener();
    }

    interface Presenter extends BaseContract.Presenter {
        void getUser(String documentKey);
        void changeProfileImage(Uri imasge);
        void changeUsername(String username);
        void logout();
    }

    interface Interactor extends BaseContract.Interactor {
        void getUser(String documentKey);
        void changeProfileImage(Uri imasge);
        void changeUsername(String username);
        void logout();
    }

    interface Listener  extends BaseContract.Listener {
        void onSuccess(UserModel userModel);
        void onSuccess();
        void onSuccess(String message);
    }
}
