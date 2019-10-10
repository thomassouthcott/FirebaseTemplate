package com.thomassouthcott.firebasetemplate.models;

import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;
import java.util.Map;

/**
 * @title User Model
 * @author Thomas Edward Southcott
 */
public class UserModel extends FirestoreDocumentModel {
    private String m_username = "",
        m_email = "",
        m_profile_url = "";

    /**
     * @title Empty Constructor
     */
    public UserModel() {
        super();
    }

    /**
     * @title Firebase User Constructor
     *
     * @param user
     */
    public UserModel(FirebaseUser user) {
        super();
        setDocumentKey(user.getUid());
        setUsername(user.getDisplayName());
        setEmail(user.getEmail());
        setProfileurl(user.getPhotoUrl().toString());
    }

    /**
     * @title Get Username
     *
     * @return
     */
    public String getUsername() {
        return m_username;
    }

    /**
     * @title Set Username
     *
     * @param m_username
     */
    public void setUsername(String m_username) {
        this.m_username = m_username;
    }

    /**
     * @title Get Email
     *
     * @return
     */
    public String getEmail() {
        return m_email;
    }

    /**
     * @title Set Email
     *
     * @param m_email
     */
    public void setEmail(String m_email) {
        this.m_email = m_email;
    }

    /**
     * @title Get Profile Url
     *
     * @return
     */
    public String getProfileurl() {
        return m_profile_url;
    }

    /**
     * @title Set Profile Url
     *
     * @param m_profile_url
     */
    public void setProfileurl(String m_profile_url) {
        this.m_profile_url = m_profile_url;
    }

    /**
     * @title Get Updates
     *
     * @return
     */
    @Override
    public Map<String, Object> getUpdates() {
        Map<String, Object> map = new HashMap<>();
        map.put("username", m_username);
        map.put("email", m_email);
        map.put("profileurl", m_profile_url);
        return map;
    }

    /**
     * @title To String
     * @return Serialized User String
     */
    public String toString(){
        return super.getDocumentKey() + ", "
                + this.getEmail() + ", "
                + this.getUsername() + ", "
                + this.getProfileurl();
    }
}
