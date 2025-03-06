package com.example.project_prm.Dao;

import androidx.room.*;
import com.example.project_prm.Entities.OrderDetail;
import java.util.List;

@Dao
public interface OrderDetailDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(OrderDetail orderDetail);

    @Update
    void update(OrderDetail orderDetail);

    @Delete
    void delete(OrderDetail orderDetail);

    @Query("SELECT * FROM order_detail WHERE order_id = :orderId")
    List<OrderDetail> getOrderDetailsByOrderId(int orderId);
}
