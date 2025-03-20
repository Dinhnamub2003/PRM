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
}, version = 2, exportSchema = false) // Tăng version lên 2
public abstract class ClothingDatabase extends RoomDatabase {

    private static volatile ClothingDatabase INSTANCE;
    private static final String DATABASE_NAME = "DBClothing";
    private static final int THREAD_POOL_SIZE = 4;
    private static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(THREAD_POOL_SIZE);

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
                                    ClothingDatabase.class, DATABASE_NAME)
                            .fallbackToDestructiveMigration()
                            .addCallback(roomDatabaseCallback)
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
                    try {
                        CategoryDao categoryDao = database.categoryDao();
                        categoryDao.insert(new Category("Áo"));
                        categoryDao.insert(new Category("Quần"));
                        categoryDao.insert(new Category("Giày"));

                        Log.i("ClothingDatabase", "Database initialized with sample categories.");
                    } catch (Exception e) {
                        Log.e("ClothingDatabase", "Error populating database: " + e.getMessage());
                    }
                }
            });
        }

        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            Log.i("ClothingDatabase", "Database opened.");
        }
    };

    public static ExecutorService getDatabaseWriteExecutor() {
        return databaseWriteExecutor;
    }

    public static void destroyInstance() {
        if (INSTANCE != null) {
            INSTANCE.close();
            INSTANCE = null;
        }
        if (!databaseWriteExecutor.isShutdown()) {
            databaseWriteExecutor.shutdown();
        }
    }
}