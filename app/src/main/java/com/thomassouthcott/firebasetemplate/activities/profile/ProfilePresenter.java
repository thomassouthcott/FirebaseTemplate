package com.thomassouthcott.firebasetemplate.activities.profile;

import android.net.Uri;

import com.thomassouthcott.firebasetemplate.activities.base.BasePresenter;
import com.thomassouthcott.firebasetemplate.models.UserModel;

public class ProfilePresenter extends BasePresenter implements ProfileContract.Presenter, ProfileContract.Listener {
    ProfileContract.View view;
    ProfileInteractor interactor;

    public ProfilePresenter(ProfileContract.View view) {
        super(view);
        this.view = view;
        interactor = new ProfileInteractor(this);
    }


    @Override
    public void getUser(String documentKey) {
        interactor.getUser(documentKey);
    }

    @Override
    public void changeProfileImage(Uri imasge) {
        interactor.changeProfileImage(imasge);
    }

    @Override
    public void changeUsername(String username) {
        interactor.changeUsername(username);
    }

    @Override
    public void logout() {
        interactor.logout();
    }

    @Override
    public void onSuccess(UserModel userModel) {
        view.updateUser(userModel);
    }

    @Override
    public void onSuccess() {
        view.onLogoutListener();
    }

    @Override
    public void onSuccess(String message) {
        view.showMessage(message);
    }
}
