package org.taskntech.tech_flow.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;

    private String email;
    private String displayName;
    private String profilePicturePath;
    private String githubId; // Unique GitHub ID
    private String googleSubId; // Unique Google Sub ID

    public User() {
    }

    public User(String email, String displayName) {
        this.email = email;
        this.displayName = displayName;
        this.profilePicturePath = "https://www.tenforums.com/attachments/user-accounts-family-safety/322690d1615743307-user-account-image-log-user.png";
    }

    public User(String email, String displayName, String githubId, String googleSubId){
        this.email = email;
        this.displayName = displayName;
        this.githubId = githubId;
        this.googleSubId = googleSubId;
        this.profilePicturePath = "https://www.tenforums.com/attachments/user-accounts-family-safety/322690d1615743307-user-account-image-log-user.png";
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getProfilePicturePath() {
        return profilePicturePath != null && !profilePicturePath.isEmpty()
                ? profilePicturePath
                : "https://www.tenforums.com/attachments/user-accounts-family-safety/322690d1615743307-user-account-image-log-user.png";
    }

    public void setProfilePicturePath(String profilePicturePath) {
        this.profilePicturePath = profilePicturePath;
    }

    public String getGithubId() {
        return githubId;
    }

    public void setGithubId(String githubId) {
        this.githubId = githubId;
    }

    public String getGoogleSubId() {
        return googleSubId;
    }

    public void setGoogleSubId(String googleSubId) {
        this.googleSubId = googleSubId;
    }
}