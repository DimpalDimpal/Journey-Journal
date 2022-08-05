package com.ismt.journeyjournal.User;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

//creating the user entity on our database
@Entity(tableName = "user")
public class User {

    @PrimaryKey(autoGenerate = true)


    private int uid=0;
    public String fullName;

    private String userName;

    private String email;

    private String password;

    //below line of code is for creating the constructor class for our user entity.
    @Ignore
    public User(int uid, String fullName, String userName, String email, String password) {
        this.uid = uid;
        this.fullName = fullName;
        this.userName = userName;
        this.email = email;
        this.password = password;
    }

    public User(String fullName, String userName, String email, String password) {
        this.fullName = fullName;
        this.userName = userName;
        this.email = email;
        this.password = password;
    }

    //below line of code is for getting
    public int getUid() {
        return uid;
    }

    public String getFullName() {
        return fullName;
    }

    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    //below line of code is for getting

    public void setUid(int uid) {
        this.uid = uid;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
