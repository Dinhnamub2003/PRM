package com.example.project_prm.Repository;

import android.content.Context;
import com.example.project_prm.Dao.CartDao;
import com.example.project_prm.Database.ClothingDatabase;
import com.example.project_prm.Entities.Cart;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CartRepository {
    private final CartDao cartDao;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public CartRepository(Context context) {
        ClothingDatabase db = ClothingDatabase.getInstance(context);
        cartDao = db.cartDao();
    }

    public void insert(Cart cart) {
        executorService.execute(() -> cartDao.insert(cart));
    }


    public List<Cart> getCartByUser(int userId) {
        return cartDao.getCartByUser(userId);
    }
}
