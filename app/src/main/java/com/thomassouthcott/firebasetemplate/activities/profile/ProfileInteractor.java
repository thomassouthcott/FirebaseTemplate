package com.thomassouthcott.firebasetemplate.activities.profile;

import android.net.Uri;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.thomassouthcott.firebasetemplate.models.UserModel;
import com.thomassouthcott.firebasetemplate.repositorys.UserRepository;

public class ProfileInteractor implements ProfileContract.Interactor {
    ProfileContract.Listener profileListener;


    public ProfileInteractor(ProfileContract.Listener profileListener) {
        this.profileListener = profileListener;
    }

    @Override
    public void getUser(String documentKey) {
        UserRepository.getInstance().read(documentKey).addOnCompleteListener(new OnCompleteListener<UserModel>() {
            @Override
            public void onComplete(@NonNull Task<UserModel> task) {
                if(!task.isSuccessful()) {
                    profileListener.onFailure(task.getException().getMessage());
                } else {
                    profileListener.onSuccess(task.getResult());
                }
            }
        });
    }

    @Override
    public void changeProfileImage(Uri imasge) {
        final StorageReference ref = FirebaseStorage.getInstance().getReference().child("images/" + FirebaseAuth.getInstance().getCurrentUser().getUid());
        UploadTask uploadTask = ref.putFile(imasge);
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
                            final Uri downloadUri = task.getResult();
                            UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                                    .setPhotoUri(downloadUri).build();
                            FirebaseAuth.getInstance().getCurrentUser().updateProfile(profileChangeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        UserRepository.getInstance().read(FirebaseAuth.getInstance().getUid()).addOnCompleteListener(new OnCompleteListener<UserModel>() {
                                            @Override
                                            public void onComplete(@NonNull Task<UserModel> task) {
                                                if(task.isSuccessful()){
                                                    UserModel u = task.getResult();
                                                    u.setProfileurl(downloadUri.toString());
                                                    UserRepository.getInstance().update(u).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if(task.isSuccessful()) {
                                                                getUser(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                                            } else {
                                                                profileListener.onFailure(task.getException().getMessage());
                                                            }
                                                        }
                                                    });
                                                } else {
                                                    profileListener.onFailure(task.getException().getMessage());
                                                }
                                            }
                                        });
                                    }
                                    else {
                                        profileListener.onFailure(task.getException().getMessage());
                                    }
                                };
                            });
                        } else {
                            profileListener.onFailure(task.getException().getMessage());
                        }
                    }
                });
    }

    @Override
    public void changeUsername(final String username) {
        UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                .setDisplayName(username).build();
        FirebaseAuth.getInstance().getCurrentUser().updateProfile(profileChangeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    UserRepository.getInstance().read(FirebaseAuth.getInstance().getUid()).addOnCompleteListener(new OnCompleteListener<UserModel>() {
                        @Override
                        public void onComplete(@NonNull Task<UserModel> task) {
                            if(task.isSuccessful()){
                                UserModel u = task.getResult();
                                u.setUsername(username);
                                UserRepository.getInstance().update(u).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()) {
                                            getUser(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                        } else {
                                            profileListener.onFailure(task.getException().getMessage());
                                        }
                                    }
                                });
                            } else {
                                profileListener.onFailure(task.getException().getMessage());
                            }
                        }
                    });
                }
                else {
                    profileListener.onFailure(task.getException().getMessage());
                }
            };
        });
    }

    @Override
    public void logout() {
        FirebaseAuth.getInstance().signOut();

        if(FirebaseAuth.getInstance().getCurrentUser() != null) {
            profileListener.onFailure("Error signing out.");
        } else {
            profileListener.onSuccess();
        }
    }
}
