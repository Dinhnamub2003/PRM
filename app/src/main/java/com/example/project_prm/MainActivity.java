package com.example.project_prm;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.project_prm.Activity.Admin.Product.ProductActivity;
import com.example.project_prm.Activity.User.Cart.CartActivity;
import com.example.project_prm.Activity.User.Product.ProductListActivity;
import com.example.project_prm.Database.ClothingDatabase;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Áp dụng padding cho system bar
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Khởi tạo database
        ClothingDatabase.getInstance(this);

        // Sự kiện mở ProductActivity
        findViewById(R.id.btnOpenProductActivity).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ProductActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.btnOpenProductListActivity).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ProductListActivity.class);
            startActivity(intent);
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_home) {
                startActivity(new Intent(MainActivity.this, ProductListActivity.class));
                return true;
            }else if (item.getItemId() == R.id.nav_cart) {
                startActivity(new Intent(MainActivity.this, CartActivity.class));
                return true;
            }else if (item.getItemId() == R.id.nav_admin) {
                startActivity(new Intent(MainActivity.this, ProductActivity.class));
                return true;
            } else if (item.getItemId() == R.id.nav_user) {
                startActivity(new Intent(MainActivity.this, ProductListActivity.class));
                return true;
            }
            return false;
        });
    }
}
