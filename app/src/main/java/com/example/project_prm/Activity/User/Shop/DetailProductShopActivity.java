package com.example.project_prm.Activity.User.Shop;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.project_prm.R;
import com.example.project_prm.ViewModel.User.ProductViewModel;

import java.io.File;

public class DetailProductShopActivity extends AppCompatActivity {

    private ProductViewModel manageProductViewModel;
    private ImageView imageView;
    private TextView tvName, tvBrand, tvPrice, tvManufacture,
           tvUnit, tvStock;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_product_shop);

        imageView = findViewById(R.id.imageViewProductDetail);
        tvName = findViewById(R.id.tvProductNameDetail);
        tvBrand = findViewById(R.id.tvBrandDetail);
        tvPrice = findViewById(R.id.tvPriceDetail);
        tvManufacture = findViewById(R.id.tvManufacture);

        tvUnit = findViewById(R.id.tvUnits);

        tvStock = findViewById(R.id.tvStock);


        manageProductViewModel = new ViewModelProvider(this).get(ProductViewModel.class);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("PRODUCT_ID")) {
            int productId = intent.getIntExtra("PRODUCT_ID", -1);
            manageProductViewModel.getProductById(productId).observe(this, product -> {
                if (product != null) {
                    tvName.setText("Name: "+ product.getName());
                    tvBrand.setText("Brand: " + product.getBrand());
                    tvPrice.setText("Sale Price: "+ String.format("%,.0f", product.getSale_price()));

                    tvUnit.setText("Unit: "+product.getUnit());

                    tvManufacture.setText("Manufacture: "+product.getManufacturer());
                    tvStock.setText("Stock: " +product.getStock());



                    // Hiển thị ảnh sản phẩm
                    if (product.getImage() != null && !product.getImage().isEmpty()) {
                        File imgFile = new File(product.getImage());
                        if (imgFile.exists()) {
                            Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                            imageView.setImageBitmap(bitmap);
                        } else {
                            imageView.setImageResource(R.drawable.img_avatar);
                        }
                    } else {
                        imageView.setImageResource(R.drawable.img_avatar);
                    }
                }
            });
        }
        findViewById(R.id.btnBackDetail).setOnClickListener(v -> finish());

    }
}