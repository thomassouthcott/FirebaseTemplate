package com.thomassouthcott.firebasetemplate.activities.base;

public interface BaseContract {
    interface View {
        void showMessage(String message);
        void showDialog();
        void dismissDialog();
    }

    interface Presenter {

    }

    interface Interactor {

    }

    interface Listener {
        void onFailure(String message);
    }
}
