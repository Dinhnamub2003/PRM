package com.example.project_prm.Repository;

import android.content.Context;
import com.example.project_prm.Dao.CategoryDao;
import com.example.project_prm.Database.ClothingDatabase;
import com.example.project_prm.Entities.Category;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CategoryRepository {
    private final CategoryDao categoryDao;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public CategoryRepository(Context context) {
        ClothingDatabase db = ClothingDatabase.getInstance(context);
        categoryDao = db.categoryDao();
    }

    public void insert(Category category) {
        executorService.execute(() -> categoryDao.insert(category));
    }

    public List<Category> getAllCategories() {
        return categoryDao.getAllCategories();
    }
}
