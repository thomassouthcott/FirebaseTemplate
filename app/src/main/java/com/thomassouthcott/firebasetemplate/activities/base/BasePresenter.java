package com.thomassouthcott.firebasetemplate.activities.base;

public class BasePresenter implements BaseContract.Presenter, BaseContract.Listener {
    BaseContract.View view;
    public BasePresenter(BaseContract.View view) {
        this.view = view;
    }
    @Override
    public void onFailure(String message) {
        view.dismissDialog();
        view.showMessage(message);
    }
}
