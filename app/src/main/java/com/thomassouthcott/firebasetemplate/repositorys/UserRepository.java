package com.thomassouthcott.firebasetemplate.repositorys;
import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import com.thomassouthcott.firebasetemplate.models.UserModel;

/**
 * @title UserRepository
 * @author Thomas Edward Southcott
 *
 */
public class UserRepository extends FirestoreRepository<UserModel> {
    private static final UserRepository INSTANCE = new UserRepository();

    private static final String COLLECTION_PATH = "users";

    /**
     * @title Private constructor
     *
     */
    private UserRepository() {
        super(UserModel.class, COLLECTION_PATH);
    }

    /**
     * @title Constructor
     * @return
     */
    public static UserRepository getInstance() {
        return INSTANCE;
    }

    /**
     * @title Create Current User
     *
     * @param model
     * @return
     */
    @Override
    public Task<Boolean> create(final UserModel model) {
        return collectionReference.document(model.getDocumentKey()).set(model.getUpdates()).continueWith(new Continuation<Void, Boolean>() {
            @Override
            public Boolean then(@NonNull Task<Void> task) throws Exception {
                if(task.isSuccessful()) {
                    return true;
                }
                return false;
            }
        });
    }
}
