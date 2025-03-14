package com.example.project_prm.Repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.project_prm.Dao.UserDao;
import com.example.project_prm.Database.ClothingDatabase;
import com.example.project_prm.Entities.Product;
import com.example.project_prm.Entities.User;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UserRepository {
    private final UserDao userDao;
    private  ExecutorService executorService;
    private LiveData<List<User>> getAllUsers;
    public UserRepository(Context context) {
        ClothingDatabase db = ClothingDatabase.getInstance(context);
        userDao = db.userDao();
        getAllUsers = userDao.getAllUsers();
        executorService = Executors.newSingleThreadExecutor();


    }

    public void insert(User user) {
        executorService.execute(() -> userDao.insert(user));
    }

    public void update(User user) {
        executorService.execute(() -> userDao.update(user));
    }

    public void delete(User user) {
        executorService.execute(() -> userDao.delete(user));
    }

    public User getUserById(int userId) {
        return userDao.getUserById(userId);
    }


    public LiveData<List<User>> getAllUsers() {
        return getAllUsers;
    }


    public void softDeleteUser(int userId) {
        executorService.execute(() -> userDao.softDeleteUser(userId));
    }
    public void restoreUser(int userId) {
        executorService.execute(() -> userDao.restoreUser(userId));
    }


}
