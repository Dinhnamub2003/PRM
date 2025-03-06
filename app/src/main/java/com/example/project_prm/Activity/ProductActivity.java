package com.example.project_prm.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_prm.Adapter.ProductAdapter;
import com.example.project_prm.Entities.Product;
import com.example.project_prm.MainActivity;
import com.example.project_prm.R;
import com.example.project_prm.ViewModel.ProductViewModel;

public class ProductActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private ProductViewModel productViewModel;

    // ActivityResultLauncher để thay thế startActivityForResult
    private final ActivityResultLauncher<Intent> addProductLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                            Intent data = result.getData();
                            String name = data.getStringExtra("name");
                            double price = data.getDoubleExtra("price", 0);

                            Product newProduct = new Product(name, price);
                            productViewModel.insert(newProduct);

                            Toast.makeText(this, "Sản phẩm đã được thêm!", Toast.LENGTH_SHORT).show();
                        }
                    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        productAdapter = new ProductAdapter(null);
        recyclerView.setAdapter(productAdapter);

        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);
        productViewModel.getAllProducts().observe(this, productAdapter::setProductList);
        findViewById(R.id.btnAddProduct).setOnClickListener(v -> {
            Intent intent = new Intent(this, AddProductActivity.class);
            startActivity(intent);
        });
    }



}
