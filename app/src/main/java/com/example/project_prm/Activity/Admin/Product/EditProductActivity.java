package com.example.project_prm.Activity.Admin.Product;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.project_prm.Entities.Category;
import com.example.project_prm.Entities.Product;
import com.example.project_prm.R;
import com.example.project_prm.Repository.CategoryRepository;
import com.example.project_prm.Repository.ProductRepository;
import com.example.project_prm.ViewModel.Admin.ProductViewModel;

import java.util.ArrayList;
import java.util.List;

public class EditProductActivity extends AppCompatActivity {
    private ProductRepository productRepository;
    private CategoryRepository categoryRepository;
    private EditText etProductNameEdit, etBrandEdit, etProductCodeEdit, etStockEdit, etUnitEdit,
            etSalePriceEdit, etDiscountEdit, etDealerPriceEdit, etManufacturerEdit;
    private ImageView imgProductEdit;
    private Spinner spinnerCategoryEdit;  // Nếu dùng Spinner (chọn danh mục)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);

        // Ánh xạ view từ XML
        etProductNameEdit = findViewById(R.id.etProductNameEdit);
        etBrandEdit = findViewById(R.id.etBrandEdit);
        etProductCodeEdit = findViewById(R.id.etProductCodeEdit);
        etStockEdit = findViewById(R.id.etStockEdit);
        etUnitEdit = findViewById(R.id.etUnitEdit);
        etSalePriceEdit = findViewById(R.id.etSalePriceEdit);
        etDiscountEdit = findViewById(R.id.etDiscountEdit);
        etDealerPriceEdit = findViewById(R.id.etDealerPriceEdit);
        etManufacturerEdit = findViewById(R.id.etManufacturerEdit);
        imgProductEdit = findViewById(R.id.imgProductEdit);
        spinnerCategoryEdit = findViewById(R.id.spinnerCategoryEdit);
        // Khởi tạo repository
        productRepository = new ProductRepository(getApplication());

        int productId = getIntent().getIntExtra("PRODUCT_ID", -1);
        if (productId != -1) {
            loadProductData(productId);
        }
        Button btnBack = findViewById(R.id.btnBackEdit);
        btnBack.setOnClickListener(v -> finish());
    }

    private void loadProductData(int productId) {
        ProductViewModel productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);
        productViewModel.getProductById(productId).observe(this, this::updateUI);
    }

    private void updateUI(Product product) {
        if (product != null) {
            etProductNameEdit.setText(product.getName());
            etBrandEdit.setText(product.getBrand());
            etProductCodeEdit.setText(product.getProduct_code());
            etStockEdit.setText(String.valueOf(product.getStock()));
            etUnitEdit.setText(product.getUnit());
            etSalePriceEdit.setText(String.valueOf(product.getSale_price()));
            etDiscountEdit.setText(String.valueOf(product.getDiscount()));
            etDealerPriceEdit.setText(String.valueOf(product.getDealer_price()));
            etManufacturerEdit.setText(product.getManufacturer());

            // Hiển thị ảnh sản phẩm
            if (product.getImage() != null) {
                imgProductEdit.setImageURI(Uri.parse(product.getImage()));
            }
            if (spinnerCategoryEdit != null) {
                loadCategorySpinner(product.getCategory_id());
            }
        }

    }
    private void loadCategorySpinner(int selectedCategoryId) {
        // Lấy danh sách danh mục từ database
        List<Category> categoryList = (List<Category>) categoryRepository.getAllCategories();
        List<String> categoryNames = new ArrayList<>();

        // Duyệt danh sách và lấy tên danh mục
        for (Category category : categoryList) {
            categoryNames.add(category.getName());
        }

        // Tạo Adapter cho Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categoryNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategoryEdit.setAdapter(adapter);

        // Tìm vị trí của danh mục được chọn
        int selectedPosition = -1;
        for (int i = 0; i < categoryList.size(); i++) {
            if (categoryList.get(i).getId() == selectedCategoryId) {
                selectedPosition = i;
                break;
            }
        }

        // Nếu tìm thấy, đặt Spinner vào đúng vị trí
        if (selectedPosition != -1) {
            spinnerCategoryEdit.setSelection(selectedPosition);
        }
    }


}


