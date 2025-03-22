package com.example.project_prm;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.project_prm.Activity.Admin.Account.ManageAccountActivity;
import com.example.project_prm.Activity.Admin.Order.ManageOrderActivity;
import com.example.project_prm.Activity.Admin.Product.ManageProductActivity;
import com.example.project_prm.Activity.Admin.Statistic.StatisticActivity;
import com.example.project_prm.Activity.User.LoginActivity;
import com.example.project_prm.Activity.User.ProfileActivity;
import com.google.android.material.navigation.NavigationView;

public class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    protected DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private static final String KEY_USERNAME = "username";
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

        View headerView = navigationView.getHeaderView(0);
        TextView tvWelcome = headerView.findViewById(R.id.tvWelcome);
        tvWelcome.setText("Welcome");


        updateMenuBasedOnRole(navigationView);
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

        if (id == R.id.nav_statistic) {
            startActivity(new Intent(this, StatisticActivity.class));
        } else if (id == R.id.nav_product) {
            startActivity(new Intent(this, ManageProductActivity.class));
        } else if (id == R.id.nav_account) {
            startActivity(new Intent(this, ManageAccountActivity.class));
        } else if (id == R.id.nav_order) {
            startActivity(new Intent(this, ManageOrderActivity.class));
        } else if (id == R.id.nav_profile) {
            SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
            int userId = sharedPreferences.getInt("user_id", -1);

            if (userId != -1) {
                Intent intent = new Intent(this, ProfileActivity.class);
                intent.putExtra("USER_ID", userId);
                startActivity(intent);
            }
        }else{
            showLogoutConfirmationDialog();
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void logout() {
        SharedPreferences sharedPreferences = getSharedPreferences("MY_APP_PREFS", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("IS_LOGGED_IN", false);
        editor.putInt("USER_ID", -1);
        editor.apply();

        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
    private void showLogoutConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Yes", (dialog, which) -> logout())
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .show();
    }
    private void updateMenuBasedOnRole(NavigationView navigationView) {
        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        String role = sharedPreferences.getString("role", "User"); // Mặc định là User

        // Lấy Menu từ NavigationView
        Menu menu = navigationView.getMenu();

        if (role.equals("User")) {
            // Ẩn các mục không dành cho User
            menu.findItem(R.id.nav_statistic).setVisible(false);
            menu.findItem(R.id.nav_product).setVisible(false);
            menu.findItem(R.id.nav_account).setVisible(false);
            menu.findItem(R.id.nav_order).setVisible(false);
        }
    }
}
