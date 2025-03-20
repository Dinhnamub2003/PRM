package com.example.project_prm;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.project_prm.Activity.Admin.Account.ManageAccountActivity;
import com.example.project_prm.Activity.Admin.Order.ManageOrderActivity;
import com.example.project_prm.Activity.Admin.Product.ManageProductActivity;
import com.example.project_prm.Activity.Admin.Statistic.StatisticActivity;
import com.google.android.material.navigation.NavigationView;

public class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    protected DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        // Ánh xạ Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar); // Đặt Toolbar làm ActionBar

        // Ánh xạ DrawerLayout
        drawer = findViewById(R.id.drawer_layout);

        // Thêm icon menu vào Toolbar
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState(); // Đồng bộ trạng thái với Drawer

        // Ánh xạ NavigationView
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true; // Xử lý mở/đóng Navigation Drawer
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            startActivity(new Intent(this, StatisticActivity.class));
        } else if (id == R.id.nav_product) {
            startActivity(new Intent(this, ManageProductActivity.class));
        } else if (id == R.id.nav_account) {
            startActivity(new Intent(this, ManageAccountActivity.class));
        } else if (id == R.id.nav_order) {
            startActivity(new Intent(this, ManageOrderActivity.class));
        } else if (id == R.id.nav_view) {
//            startActivity(new Intent(this, LoginActivity.class));
            finish(); // Đóng MainActivity sau khi logout
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
