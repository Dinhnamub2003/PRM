package com.example.project_prm.Database;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.example.project_prm.Dao.*;
import com.example.project_prm.Entities.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {
        User.class, Role.class, Category.class, Product.class,
        Order.class, OrderDetail.class, Cart.class, Rating.class
}, version = 1, exportSchema = false)
public abstract class ClothingDatabase extends RoomDatabase {

    private static volatile ClothingDatabase INSTANCE;


    private static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(4);

    public abstract UserDao userDao();
    public abstract RoleDao roleDao();
    public abstract CategoryDao categoryDao();
    public abstract ProductDao productDao();
    public abstract OrderDao orderDao();
    public abstract OrderDetailDao orderDetailDao();
    public abstract CartDao cartDao();
    public abstract RatingDao ratingDao();

    public static ClothingDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (ClothingDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    ClothingDatabase.class, "clothing_database")
                            .fallbackToDestructiveMigration()
                            .addCallback(roomDatabaseCallback) // Thêm callback để insert dữ liệu mẫu
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static final RoomDatabase.Callback roomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            databaseWriteExecutor.execute(() -> {
                ClothingDatabase database = INSTANCE;
                if (database != null) {
                    ProductDao productDao = database.productDao();

                    // Thêm dữ liệu mẫu
                    Product p1 = new Product("Laptop", 1, "Dell", "LAP123", 10, "pcs",
                            1500, 10, 1400.0, "Dell Inc.", "", "2025-03-04", "2025-03-04",
                            null, 0);

                    Product p2 = new Product("Smartphone", 2, "Samsung", "S23", 20, "pcs",
                            999, 5, 950.0, "Samsung Electronics", "", "2025-03-04", "2025-03-04",
                            null, 0);

                    Product p3 = new Product("Headphones", 3, "Sony", "HP789", 15, "pcs",
                            299, 15, 250.0, "Sony Corp.", "", "2025-03-04", "2025-03-04",
                            null, 0);

                    productDao.insert(p1);
                    productDao.insert(p2);
                    productDao.insert(p3);

                }
            });
        }
    };

}
