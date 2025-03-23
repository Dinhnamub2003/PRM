package com.example.project_prm.Activity.User.Cart;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.project_prm.Adapter.User.CartAdapter;
import com.example.project_prm.R;
import com.example.project_prm.ViewModel.User.CartViewModel;

public class CartActivity extends AppCompatActivity {
    private CartViewModel cartViewModel;
    private CartAdapter cartAdapter;
    private int currentUserId = 1; // Giả sử từ hệ thống đăng nhập

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        RecyclerView recyclerView = findViewById(R.id.recyclerCartView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        cartViewModel = new CartViewModel(getApplication(), currentUserId);
        cartAdapter = new CartAdapter(cartWithProduct -> cartViewModel.delete(cartWithProduct.getCart()));
        recyclerView.setAdapter(cartAdapter);

        cartViewModel.getCartItems().observe(this, cartWithProducts -> cartAdapter.setCartList(cartWithProducts));

        Button btnPay = findViewById(R.id.btnPay);
        btnPay.setOnClickListener(v -> {
            Toast.makeText(this, "Payment processing...", Toast.LENGTH_SHORT).show();
        });
    }
}
