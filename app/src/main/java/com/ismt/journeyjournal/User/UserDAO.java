package com.ismt.journeyjournal.User;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.ismt.journeyjournal.User.User;

import java.util.List;

//below line of code is for creating the user dao for getting live data from user table, inserting, deleting.
@Dao
public interface UserDAO {
    @Query("SELECT * FROM user")
    List<User> getAllUsers();

    @Query("SELECT * FROM user WHERE Uid IN (:userIds)")
    List<User> loadAllByIds(int[] userIds);

    @Query("select * from user")
    LiveData<List<User>> getUsersLiveData();


    @Insert
    void insertUser(User... users);

    @Delete
    void delete(User user);

    @Query("select * from user where email=:email and password=:pass")
    User getUser(String email, String pass);


}
