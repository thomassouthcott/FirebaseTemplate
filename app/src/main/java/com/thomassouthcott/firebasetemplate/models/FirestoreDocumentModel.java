package com.thomassouthcott.firebasetemplate.models;

import java.util.Map;

/**
 * @title Firestore Docuement Model
 * @author Thomas Edward Southcott
 */
public abstract class FirestoreDocumentModel implements Model {
    private String documentKey = "";

    /**
     * @title Constructor
     */
    public FirestoreDocumentModel(){

    }

    /**
     * @title Get Docuemnent Key
     *
     * @return
     */
    public String getDocumentKey() { return documentKey; }

    /**
     * @title Set Document Key
     *
     * @param documentKey
     */
    public void setDocumentKey(String documentKey) { this.documentKey = documentKey; }

    /**
     * @title Get Updates
     *
     * @return
     */
    public abstract Map<String, Object> getUpdates();
}
