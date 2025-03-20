package com.example.project_prm.Repository;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.project_prm.Dao.CategoryDao;
import com.example.project_prm.Database.ClothingDatabase;
import com.example.project_prm.Entities.Category;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CategoryRepository {
    private final CategoryDao categoryDao;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final MutableLiveData<List<Category>> allCategories = new MutableLiveData<>();

    public CategoryRepository(Application application) {
        ClothingDatabase db = ClothingDatabase.getInstance(application);
        categoryDao = db.categoryDao();

        // Chạy trên background thread
        executorService.execute(() -> {
            List<Category> categories = categoryDao.getAllCategories();
            allCategories.postValue(categories);
        });
    }

    public LiveData<List<Category>> getAllCategories() {
        return allCategories;
    }

    public void insert(Category category) {
        executorService.execute(() -> categoryDao.insert(category));
    }
}
