package com.diplom.qrstudent.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UserDTO implements Serializable {

    @SerializedName("id")
    private long id;

    @SerializedName("username")
    private String username;

    @SerializedName("password")
    private String password;

    @SerializedName("firstName")
    private String firstName;

    @SerializedName("lastName")
    private String lastName;

    @SerializedName("userType")
    private String userType;

    @SerializedName("groupName")
    private String groupName;

    @SerializedName("specialization")
    private String specialization;
    @SerializedName("taughtSubject")
    private String taughtSubject;

    @SerializedName("startTime")
    private String startTime;
    @SerializedName("endTime")
    private String endTime;
    @SerializedName("imageUrl")
    private String imageUrl;
    @SerializedName("verified")
    private boolean verified = false;

    @SerializedName("description")
    private String description;

    @SerializedName("user")
    private UserDTO user;

    public UserDTO(long id, String username, String password, String firstName, String lastName, String imageUrl, String userType) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.imageUrl = imageUrl;
        this.userType = userType;
    }




    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroup(String groupName) {
        this.groupName = groupName;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpec(String specialization) {
        this.specialization = specialization;
    }

    public String getTaughtSubject() {
        return taughtSubject;
    }

    public void setSubject(String taughtSubject) {
        this.taughtSubject = taughtSubject;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean getVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }
}
