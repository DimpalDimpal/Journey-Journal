package com.ismt.journeyjournal;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.ismt.journeyjournal.User.JournalDb;
import com.ismt.journeyjournal.User.User;
import com.ismt.journeyjournal.User.UserDAO;

import java.util.List;

public class UserRepository {
    //below line of code is for creating a variable
    private final JournalDb db;
    private final UserDAO userDao;

    //below line of code is for creating a constructor class for user repository
    public UserRepository(Context context){
        this.db = JournalDb.getDBInstance(context);
        this.userDao = db.userDao();
    }

    //below line of code is for getting live data from user table
    public LiveData<List<User>> getLiveUsers(){
        return userDao.getUsersLiveData();
    }

    //below line of code is for inserting the user to the user table
    void insertUser(User user){
        db.userDao().insertUser(user);
    }

    // below line of code is for selecting the user's email and password from the user table
    public User selectUser(String email, String pass) {
        return userDao.getUser(email, pass);
    }


}
