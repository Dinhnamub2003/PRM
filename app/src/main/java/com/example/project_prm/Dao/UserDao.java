package com.example.project_prm.Dao;

import androidx.room.*;
import com.example.project_prm.Entities.User;
import java.util.List;

@Dao
public interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(User user);

    @Update
    void update(User user);

    @Delete
    void delete(User user);

    @Query("SELECT * FROM user WHERE id = :userId")
    User getUserById(int userId);

    @Query("SELECT * FROM user WHERE username = :username AND password = :password")
    User login(String username, String password);

    @Query("SELECT * FROM user")
    List<User> getAllUsers();
}
