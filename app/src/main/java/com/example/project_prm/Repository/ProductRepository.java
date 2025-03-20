package com.example.project_prm.Repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import com.example.project_prm.Dao.ProductDao;
import com.example.project_prm.Database.ClothingDatabase;
import com.example.project_prm.Database.ClothingDatabase;
import com.example.project_prm.Entities.Product;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProductRepository {
    private ProductDao productDao;
    private LiveData<List<Product>> allProducts;
    private ExecutorService executorService;

    public ProductRepository(Application application) {
        ClothingDatabase db = ClothingDatabase.getInstance(application);
        productDao = db.productDao();
        allProducts = productDao.getAllProductsAdmin();
        executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<Product>> getAllProducts() {
        return allProducts;
    }

    public void insert(Product product) {
        new InsertProductAsyncTask(productDao).execute(product);
    }
    public Product getProductById(int productId) {
        return productDao.getProductById(productId);
    }
    private static class InsertProductAsyncTask extends AsyncTask<Product, Void, Void> {
        private ProductDao productDao;

        private InsertProductAsyncTask(ProductDao productDao) {
            this.productDao = productDao;
        }

        @Override
        protected Void doInBackground(Product... products) {
            productDao.insert(products[0]);
            return null;
        }
    }
}
