package com.example.project_prm.Dao;

import androidx.room.*;
import com.example.project_prm.Entities.Cart;
import java.util.List;

@Dao
public interface CartDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Cart cart);

    @Update
    void update(Cart cart);

    @Delete
    void delete(Cart cart);

    @Query("SELECT * FROM cart WHERE user_id = :userId")
    List<Cart> getCartByUser(int userId);
}
