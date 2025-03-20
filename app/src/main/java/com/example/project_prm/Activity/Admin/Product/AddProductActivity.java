package com.example.project_prm.Activity.Admin.Product;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.project_prm.Entities.Category;
import com.example.project_prm.Entities.Product;
import com.example.project_prm.R;
import com.example.project_prm.Utils.ImageUtils;
import com.example.project_prm.ViewModel.Admin.CategoryViewModel;
import com.example.project_prm.ViewModel.Admin.ProductViewModel;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddProductActivity extends AppCompatActivity {
    private EditText etUnit, etProductName, etBrand, etProductCode, etStock, etSalePrice, etDiscount, etDealerPrice, etManufacturer;
    private Spinner spinnerCategory;
    private Button btnSaveProduct, btnUploadImage;
    private ImageView imgProduct;
    private ProductViewModel productViewModel;
    private CategoryViewModel categoryViewModel;
    private List<Category> categoryList = new ArrayList<>();
    private ArrayAdapter<String> categoryAdapter;
    private int selectedCategoryId = -1;
    private String imagePath = null;  // Lưu đường dẫn ảnh thay vì Base64

    private ActivityResultLauncher<Intent> imagePickerLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        etProductName = findViewById(R.id.etProductName);
        etBrand = findViewById(R.id.etBrand);
        etProductCode = findViewById(R.id.etProductCode);
        etStock = findViewById(R.id.etStock);
        etSalePrice = findViewById(R.id.etSalePrice);
        etDiscount = findViewById(R.id.etDiscount);
        etDealerPrice = findViewById(R.id.etDealerPrice);
        etManufacturer = findViewById(R.id.etManufacturer);
        etUnit = findViewById(R.id.etUnit);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        btnSaveProduct = findViewById(R.id.btnSaveProduct);
        imgProduct = findViewById(R.id.imgProduct);
        btnUploadImage = findViewById(R.id.btnUploadImage);

        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);
        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);

        loadCategories();
        btnSaveProduct.setOnClickListener(v -> saveProduct());

        // Đăng ký Activity Result API để chọn ảnh
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Uri imageUri = result.getData().getData();
                        imgProduct.setImageURI(imageUri);

                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                            String fileName = "product_" + System.currentTimeMillis() + ".png";
                            imagePath = ImageUtils.saveImageToInternalStorage(this, bitmap, fileName);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );

        btnUploadImage.setOnClickListener(v -> openFileChooser());
        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish()); // Quay lại màn hình trước đó

    }

    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        imagePickerLauncher.launch(intent);
    }

    private void loadCategories() {
        categoryViewModel.getAllCategories().observe(this, categories -> {
            categoryList = categories;
            List<String> categoryNames = new ArrayList<>();
            for (Category category : categories) {
                categoryNames.add(category.getName());
            }
            categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categoryNames);
            categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerCategory.setAdapter(categoryAdapter);
        });
    }

    private void saveProduct() {
        String name = etProductName.getText().toString().trim();
        String brand = etBrand.getText().toString().trim();
        String productCode = etProductCode.getText().toString().trim();
        String stockStr = etStock.getText().toString().trim();
        String salePriceStr = etSalePrice.getText().toString().trim();
        String discountStr = etDiscount.getText().toString().trim();
        String dealerPriceStr = etDealerPrice.getText().toString().trim();
        String manufacturer = etManufacturer.getText().toString().trim();
        String unit = etUnit.getText().toString().trim();

        if (unit.isEmpty() || name.isEmpty() || brand.isEmpty() || productCode.isEmpty() || stockStr.isEmpty() || salePriceStr.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        int selectedPosition = spinnerCategory.getSelectedItemPosition();
        if (selectedPosition >= 0 && selectedPosition < categoryList.size()) {
            selectedCategoryId = categoryList.get(selectedPosition).getId();
        }

        int stock = Integer.parseInt(stockStr);
        double salePrice = Double.parseDouble(salePriceStr);
        double discount = discountStr.isEmpty() ? 0.0 : Double.parseDouble(discountStr);
        double dealerPrice = dealerPriceStr.isEmpty() ? 0.0 : Double.parseDouble(dealerPriceStr);

        String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

        Product newProduct = new Product(
                name, selectedCategoryId, brand, productCode, stock, unit, salePrice,
                discount, dealerPrice, manufacturer, imagePath, currentTime, "", 0
        );
        productViewModel.insert(newProduct);

        Toast.makeText(this, "Sản phẩm đã được thêm thành công", Toast.LENGTH_SHORT).show();
        finish();
    }
}
