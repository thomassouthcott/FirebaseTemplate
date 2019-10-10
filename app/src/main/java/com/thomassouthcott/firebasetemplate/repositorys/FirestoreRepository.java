package com.thomassouthcott.firebasetemplate.repositorys;


import androidx.annotation.NonNull;

import com.thomassouthcott.firebasetemplate.models.FirestoreDocumentModel;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.ArrayList;
import java.util.List;

/**
 * @title FirestoreRepository
 * @author Thomas Edward Southcott
 *
 * @param <Model>
 */
public class FirestoreRepository<Model extends FirestoreDocumentModel> implements Repository<Model> {

    private final String collectionPath;
    protected final CollectionReference collectionReference;

    private final Class<Model> modelClass;

    /**
     * @title Constructor
     *
     * @param modelClass
     * @param collectionPath
     */
    public FirestoreRepository(@NonNull Class<Model> modelClass, @NonNull String collectionPath) {
        this.collectionPath = collectionPath;
        collectionReference = FirebaseFirestore.getInstance().collection(collectionPath);

        this.modelClass = modelClass;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Task<Boolean> exists(final String documentId) {
        if (documentId == null) return Tasks.forResult(false);
        return collectionReference.document(documentId).get().continueWith(
                new Continuation<DocumentSnapshot, Boolean>() {
                    @Override
                    public Boolean then(@NonNull Task<DocumentSnapshot> task) throws Exception {
                        DocumentSnapshot snapshot = task.getResult();
                        return snapshot != null && snapshot.exists();
                    }
                }
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Task<Boolean> create(final Model model) {
        return collectionReference.add(model.getUpdates()).continueWith(new Continuation<DocumentReference, Boolean>() {
            @Override
            public Boolean then(@NonNull Task<DocumentReference> task) throws Exception {
                DocumentReference docRef = task.getResult();
                if (task.isSuccessful() && docRef != null) {
                    model.setDocumentKey(docRef.getId());
                    return true;
                }
                return false;
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Task<List<Model>> read() {
        return getModelsFromQuerySnapshot(collectionReference.get());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Task<Model> read(final String documentId) {
        if (documentId == null) return Tasks.forResult(null);
        return collectionReference.document(documentId).get().continueWith(
                new Continuation<DocumentSnapshot, Model>() {
                    @Override
                    public Model then(@NonNull Task<DocumentSnapshot> task) throws Exception {
                        DocumentSnapshot snapshot = task.getResult();
                        Model model = null;
                        if (snapshot != null && snapshot.exists())
                        {
                            model = snapshot.toObject(modelClass);
                            model.setDocumentKey(snapshot.getId());
                        }
                        return model;
                    }
                }
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Task<Void> update(final Model model) {
        return collectionReference.document(model.getDocumentKey()).update(model.getUpdates());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Task<Void> delete(final String documentId)
    {
        return collectionReference.document(documentId).delete();
    }

    /**
     * @title Get Models from a Query Snapshot
     *
     * @param task
     * @return
     */
    protected Task<List<Model>> getModelsFromQuerySnapshot(Task<QuerySnapshot> task) {
        return task.continueWith(new Continuation<QuerySnapshot, List<Model>>() {
            @Override
            public List<Model> then(@NonNull Task<QuerySnapshot> task) throws Exception {
                // Create the list to store results in
                List<Model> models = new ArrayList<>();

                // Get the data from the snapshot (if it's there
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot != null) {
                    for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                        models.add(getModelFromSnapshot(document));
                    }
                }

                // Return the constructed list of models
                return models;
            }
        });
    }

    /**
     * @title Get a Model from a Query Snapshot
     *
     * @param snapshot
     * @return
     */
    protected Model getModelFromSnapshot(DocumentSnapshot snapshot) {
        if (snapshot == null) return null;

        Model model = snapshot.toObject(modelClass);

        if (model != null) {
            model.setDocumentKey(snapshot.getId());
        }

        return model;
    }
}
