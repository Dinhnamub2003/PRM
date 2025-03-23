package com.example.project_prm.Activity.User.Product;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_prm.Adapter.User.ProductListAdapter;
import com.example.project_prm.Entities.Cart;
import com.example.project_prm.Entities.Product;
import com.example.project_prm.R;
import com.example.project_prm.ViewModel.Admin.ProductViewModel;
import com.example.project_prm.ViewModel.User.CartViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ProductListActivity extends AppCompatActivity {
    private ProductViewModel productViewModel;
    private CartViewModel cartViewModel;
    private ProductListAdapter productAdapter;
    private int currentUserId = 1; // Giả sử userId được lấy từ hệ thống đăng nhập

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        // Khởi tạo RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Khởi tạo ViewModel
        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);
        cartViewModel = new CartViewModel(getApplication(), currentUserId);

        // Khởi tạo Adapter với callback "Add to Cart"
        productAdapter = new ProductListAdapter(new ArrayList<>(), product -> {
            Cart cart = new Cart();
            cart.setUser_id(currentUserId);
            cart.setProduct_id(product.getId());
            cart.setQuantity(1); // Mặc định thêm 1 sản phẩm
            cart.setCreated_at(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date()));
            cartViewModel.insert(cart);
            Toast.makeText(this, "Added to cart: " + product.getName(), Toast.LENGTH_SHORT).show();
        });
        recyclerView.setAdapter(productAdapter);

        List<Product> mockProducts = createMockProducts();
        productAdapter.setProductList(mockProducts);

        // Quan sát danh sách sản phẩm từ ProductViewModel
        productViewModel.getAllProductsForUser().observe(this, products -> productAdapter.setProductList(products));

        // Xử lý sự kiện lọc (sort)
        ImageView ivFilter = findViewById(R.id.ivFilterList);
        ivFilter.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(ProductListActivity.this, ivFilter);
            popup.getMenuInflater().inflate(R.menu.filter_menu, popup.getMenu());

            popup.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.filter_by_price_desc) {
                    productAdapter.sortByPrice(false); // Giá giảm dần
                } else if (item.getItemId() == R.id.filter_by_price_inc) {
                    productAdapter.sortByPrice(true); // Giá tăng dần
                }
                return true;
            });

            popup.show();
        });

        // Xử lý tìm kiếm
        EditText etSearch = findViewById(R.id.etSearchBar);
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                productAdapter.filterByName(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private List<Product> createMockProducts() {
        List<Product> mockProducts = new ArrayList<>();

        // Sản phẩm 1
        Product product1 = new Product();
        product1.setId(1);
        product1.setName("Áo sơ mi nam");
        product1.setBrand("Gucci");
        product1.setSale_price(500000);
        product1.setImage(""); // Để trống hoặc đường dẫn ảnh nếu có
        product1.setCreated_at("2025-03-23 10:00:00");
        mockProducts.add(product1);

        // Sản phẩm 2
        Product product2 = new Product();
        product2.setId(2);
        product2.setName("Quần jeans nữ");
        product2.setBrand("Levi's");
        product2.setSale_price(800000);
        product2.setImage("");
        product2.setCreated_at("2025-03-23 12:00:00");
        mockProducts.add(product2);

        // Sản phẩm 3
        Product product3 = new Product();
        product3.setId(3);
        product3.setName("Giày thể thao");
        product3.setBrand("Nike");
        product3.setSale_price(1200000);
        product3.setImage("");
        product3.setCreated_at("2025-03-23 14:00:00");
        mockProducts.add(product3);

        // Sản phẩm 4
        Product product4 = new Product();
        product4.setId(4);
        product4.setName("Áo thun unisex");
        product4.setBrand("Uniqlo");
        product4.setSale_price(300000);
        product4.setImage("");
        product4.setCreated_at("2025-03-23 16:00:00");
        mockProducts.add(product4);

        return mockProducts;
    }
}
