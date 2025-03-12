package com.example.project_prm.Activity.Admin.Product;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_prm.Adapter.Admin.ProductAdapter;
import com.example.project_prm.Entities.Product;
import com.example.project_prm.R;
import com.example.project_prm.ViewModel.Admin.ProductViewModel;

import java.util.ArrayList;

public class ProductActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private ProductViewModel productViewModel;
    private ImageView ivFilter;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        productAdapter = new ProductAdapter(new ArrayList<>());

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        findViewById(R.id.btnEdit).setOnClickListener(v -> {
            int selectedPosition = productAdapter.getSelectedPosition();
            if (selectedPosition != -1) {
                Product selectedProduct = productAdapter.getProductAt(selectedPosition);
                Intent intent = new Intent(ProductActivity.this, EditProductActivity.class);
                intent.putExtra("PRODUCT_ID", selectedProduct.getId());
                startActivity(intent);
            }
        });

        recyclerView.setAdapter(productAdapter);

        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);
        productViewModel.getAllProducts().observe(this, productAdapter::setProductList);
        findViewById(R.id.btnAdd).setOnClickListener(v -> {
            Intent intent = new Intent(ProductActivity.this, AddProductActivity.class);
            startActivity(intent);
        });
        findViewById(R.id.btnDelete).setOnClickListener(v -> {
            int selectedPosition = productAdapter.getSelectedPosition();
            if (selectedPosition != -1) {
                Product selectedProduct = productAdapter.getProductAt(selectedPosition);

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Warning!")
                        .setMessage("Continue your action?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            if (selectedProduct.getIsDelete() == 1) {
                                productViewModel.restoreProduct(selectedProduct.getId());

                            } else {
                                productViewModel.softDelete(selectedProduct.getId());
                            }
                            productAdapter.clearSelection();
                        })
                        .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                        .show();
            }else{
                Toast.makeText(this, "Choose one product", Toast.LENGTH_SHORT).show();
            }

        });





//Sap xep theo giá
        ImageView ivFilter = findViewById(R.id.ivFilter);
        ivFilter.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(ProductActivity.this, ivFilter);
            popup.getMenuInflater().inflate(R.menu.filter_menu, popup.getMenu());

            popup.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.filter_by_price_desc) {
                    productAdapter.sortByPrice(false); // Sắp xếp giảm dần
                } else if (item.getItemId() == R.id.filter_by_price_inc) {
                    productAdapter.sortByPrice(true); // Sắp xếp tăng dần
                }
                return true;
            });

            popup.show();
        });

        EditText etSearch = findViewById(R.id.etSearch);
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                productAdapter.filterByName(s.toString()); // Gọi hàm tìm kiếm khi nhập dữ liệu
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });







    }



}
