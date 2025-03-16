package com.example.project_prm.Dao;

import androidx.lifecycle.LiveData;
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

    @Query("SELECT * FROM user Order By created_at desc")
    LiveData<List<User>> getAllUsers();



    @Query("UPDATE user SET isDelete = 1 WHERE id = :userId")
    void softDeleteUser(int userId);
    @Query("UPDATE user SET isDelete = 0 WHERE id = :userId")
    void restoreUser(int userId);

    @Query("SELECT username FROM user WHERE id = :userId")
    String getUserNameByIdOrder(int userId);

}
