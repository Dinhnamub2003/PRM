package com.example.project_prm.Activity.Admin.Product;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_prm.Adapter.ProductAdapter;
import com.example.project_prm.R;
import com.example.project_prm.ViewModel.ProductViewModel;

import java.util.ArrayList;

public class ProductActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private ProductViewModel productViewModel;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        productAdapter = new ProductAdapter(new ArrayList<>());

        recyclerView.setAdapter(productAdapter);

        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);
        productViewModel.getAllProducts().observe(this, productAdapter::setProductList);
        findViewById(R.id.btnAdd).setOnClickListener(v -> {
            Intent intent = new Intent(ProductActivity.this, AddProductActivity.class);
            startActivity(intent);
        });


    }



}
