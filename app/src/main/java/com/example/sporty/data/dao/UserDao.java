package com.example.sporty.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.sporty.data.entities.User;

@Dao
public interface UserDao {
    @Query("SELECT * FROM users WHERE email = :email AND password = :password LIMIT 1")
    User getUser(String email, String password);

    @Insert
    void insert(User user);
}
