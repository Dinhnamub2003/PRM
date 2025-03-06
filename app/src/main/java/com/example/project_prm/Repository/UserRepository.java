package com.example.project_prm.Repository;

import android.content.Context;
import com.example.project_prm.Dao.UserDao;
import com.example.project_prm.Database.ClothingDatabase;
import com.example.project_prm.Entities.User;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UserRepository {
    private final UserDao userDao;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public UserRepository(Context context) {
        ClothingDatabase db = ClothingDatabase.getInstance(context);
        userDao = db.userDao();
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

    public User login(String username, String password) {
        return userDao.login(username, password);
    }

    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }
}
