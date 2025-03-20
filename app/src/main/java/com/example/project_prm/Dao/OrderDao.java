package com.example.project_prm.Dao;

import androidx.room.*;
import com.example.project_prm.Entities.Order;
import java.util.List;

@Dao
public interface OrderDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Order order);

    @Update
    void update(Order order);

    @Delete
    void delete(Order order);

    @Query("SELECT * FROM orders WHERE id = :orderId")
    Order getOrderById(int orderId);

    @Query("SELECT * FROM orders WHERE user_id = :userId")
    List<Order> getOrdersByUser(int userId);

    @Query("SELECT * FROM orders")
    List<Order> getAllOrders();
}
