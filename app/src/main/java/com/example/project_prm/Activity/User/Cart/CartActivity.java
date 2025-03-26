package com.example.project_prm.Activity.User.Cart;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_prm.Activity.User.Shop.ProductListActivity;
import com.example.project_prm.Adapter.User.CartAdapter;
import com.example.project_prm.BaseActivity;
import com.example.project_prm.Entities.CartWithProduct;
import com.example.project_prm.Entities.Order;
import com.example.project_prm.Entities.OrderDetail;
import com.example.project_prm.R;
import com.example.project_prm.ViewModel.Admin.ManageOrderViewModel;
import com.example.project_prm.ViewModel.User.CartViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CartActivity extends BaseActivity {
    private CartViewModel cartViewModel;
    private ManageOrderViewModel orderViewModel;
    private CartAdapter cartAdapter;
    TextView tvTotalPrice;

    private int currentUserId ; // Giả sử từ hệ thống đăng nhập

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getLayoutInflater().inflate(R.layout.activity_cart, findViewById(R.id.content_frame));
        RecyclerView recyclerView = findViewById(R.id.recyclerCartView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        currentUserId = sharedPreferences.getInt("user_id", -1);
        cartViewModel = new CartViewModel(getApplication(), currentUserId);
        orderViewModel = new ViewModelProvider(this).get(ManageOrderViewModel.class);
        cartAdapter = new CartAdapter(cartWithProduct -> cartViewModel.delete(cartWithProduct.getCart()), cartViewModel, this::updateTotalPrice);
        recyclerView.setAdapter(cartAdapter);

        cartViewModel.getCartItems().observe(this, cartWithProducts -> {
            cartAdapter.setCartList(cartWithProducts);
            updateTotalPrice();
        });

        Button btnPay = findViewById(R.id.btnPay);
        btnPay.setOnClickListener(v -> showConfirmDialog());
    }

    private void updateTotalPrice() {
        cartViewModel.getCartItems().observe(this, cartWithProducts -> {
            double totalPrice = 0;
            for (CartWithProduct item : cartWithProducts) {
                totalPrice += item.getCart().getQuantity() * item.getProductPrice();
            }
            tvTotalPrice.setText("Total: " + String.format("%,.0f", totalPrice) + " VND");
        });
    }
    private void showConfirmDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Confirm Payment")
                .setMessage("Are you sure you want to place this order?")
                .setPositiveButton("Yes", (dialog, which) -> processOrder())
                .setNegativeButton("No", null)
                .show();
    }
    private void processOrder() {
        cartViewModel.getCartItems().observe(this, cartWithProducts -> {
            if (cartWithProducts.isEmpty()) {
                Toast.makeText(this, "Your cart is empty!", Toast.LENGTH_SHORT).show();
                return;
            }

            double totalPrice = 0;
            List<OrderDetail> orderDetails = new ArrayList<>();

            for (CartWithProduct item : cartWithProducts) {
                totalPrice += item.getCart().getQuantity() * item.getProductPrice();
                orderDetails.add(new OrderDetail(0, 0, item.getCart().getProduct_id(), item.getCart().getQuantity(), item.getProductPrice()));
            }

            String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
            Order order = new Order(0, currentUserId, totalPrice, "Pending", currentTime, currentTime);

            orderViewModel.insertOrderWithDetails(order, orderDetails, () -> {
                cartViewModel.deleteAllCartItems(currentUserId);
                Toast.makeText(this, "Order placed successfully!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, ProductListActivity.class));
                finish();
            });
        });
    }
}
