package com.thomassouthcott.firebasetemplate.activities.register;

import android.app.Activity;
import android.net.Uri;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.thomassouthcott.firebasetemplate.models.UserModel;
import com.thomassouthcott.firebasetemplate.repositorys.UserRepository;

public class RegisterInteractor implements RegisterContract.Intractor {
    private RegisterContract.Listener mListener;

    public RegisterInteractor(RegisterContract.Listener Listener){
        this.mListener = Listener;
    }

    @Override
    public void register(Activity activity, String email, final String username, String password, final Uri profile) {

        FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(final @NonNull Task<AuthResult> userTask) {
                        if (userTask.isSuccessful()) {
                            final StorageReference ref = FirebaseStorage.getInstance().getReference().child("images/" + userTask.getResult().getUser().getUid());
                            UploadTask uploadTask = ref.putFile(profile);
                            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                @Override
                                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                    if (!task.isSuccessful()) {
                                        throw task.getException();
                                    }

                                    // Continue with the task to get the download URL
                                    return ref.getDownloadUrl();
                                }
                            })
                            //When BOTH tasks finish
                            .addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    if (task.isSuccessful()) {
                                        //Download URL from task result
                                        Uri downloadUri = task.getResult();
                                        UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                                                .setDisplayName(username).setPhotoUri(downloadUri).build();
                                        userTask.getResult().getUser().updateProfile(profileChangeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                UserModel userModel = new UserModel(FirebaseAuth.getInstance().getCurrentUser());
                                                UserRepository.getInstance().create(userModel).addOnCompleteListener(new OnCompleteListener<Boolean>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Boolean> task) {
                                                        if (task.isSuccessful()) {
                                                            mListener.onSuccess(FirebaseAuth.getInstance().getCurrentUser());
                                                        } else {
                                                            mListener.onFailure(task.getException().getMessage());
                                                        }
                                                    }
                                                });
                                                mListener.onSuccess(FirebaseAuth.getInstance().getCurrentUser());
                                            };
                                        });
                                    } else {
                                        mListener.onFailure(task.getException().getMessage());
                                    }
                                }
                            });
                        } else {
                            mListener.onFailure(userTask.getException().getMessage());
                        }
                    }
                });
    }
}
