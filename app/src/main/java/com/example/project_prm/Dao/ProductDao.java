package com.example.project_prm.Dao ;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.example.project_prm.Entities.Product;
import java.util.List;

@Dao
public interface ProductDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Product product);

    @Update
    void update(Product product);

    @Delete
    void delete(Product product);

    @Query("SELECT * FROM product WHERE id = :productId")
    Product getProductById(int productId);

    @Query("SELECT * FROM product  Order By created_at desc")
    LiveData<List<Product>> getAllProductsAdmin();


}
