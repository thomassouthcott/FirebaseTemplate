package com.thomassouthcott.firebasetemplate.activities.login;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginInteractor implements LoginContract.Intractor {

    private LoginContract.Listener mListener;

    public LoginInteractor(LoginContract.Listener Listener) {
        this.mListener = Listener;
    }

    @Override
    public void login(Activity activity, String email, String password) {
        FirebaseAuth.getInstance()
            .signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        mListener.onSuccess(FirebaseAuth.getInstance().getCurrentUser());
                    } else {
                        mListener.onFailure(task.getException().getMessage());
                    }
                }
            });
    }
}
