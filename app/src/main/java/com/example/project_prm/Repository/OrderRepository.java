package com.example.project_prm.Repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.project_prm.Dao.OrderDao;
import com.example.project_prm.Database.ClothingDatabase;
import com.example.project_prm.Entities.Order;
import com.example.project_prm.Entities.User;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class OrderRepository {
    private final OrderDao orderDao;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public OrderRepository(Context context) {
        ClothingDatabase db = ClothingDatabase.getInstance(context);
        orderDao = db.orderDao();
    }

    public void insert(Order order) {
        executorService.execute(() -> orderDao.insert(order));
    }

    public LiveData<List<Order>> getAllOrders() {
        return orderDao.getAllOrders();
    }
    public Order getOrderById(int orderId) {
        return orderDao.getOrderById(orderId);
    }
    public void acceptOrder(int userId) {
        executorService.execute(() -> orderDao.acceptOrder(userId));
    }
    public void rejectOrder(int userId) {
        executorService.execute(() -> orderDao.rejectOrder(userId));
    }
}
