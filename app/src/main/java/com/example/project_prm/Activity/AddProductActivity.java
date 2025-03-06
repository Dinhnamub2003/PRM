package com.example.project_prm.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.project_prm.Entities.Product;
import com.example.project_prm.R;

public class AddProductActivity extends AppCompatActivity {
    private EditText etProductName, etProductPrice;
    private Button btnSaveProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        etProductName = findViewById(R.id.etProductName);
        etProductPrice = findViewById(R.id.etProductPrice);
        btnSaveProduct = findViewById(R.id.btnSaveProduct);

        btnSaveProduct.setOnClickListener(v -> saveProduct());
    }

    private void saveProduct() {
        String name = etProductName.getText().toString().trim();
        String priceStr = etProductPrice.getText().toString().trim();

        if (name.isEmpty() || priceStr.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        double price = Double.parseDouble(priceStr);

        Intent resultIntent = new Intent();
        resultIntent.putExtra("name", name);
        resultIntent.putExtra("price", price);
        setResult(RESULT_OK, resultIntent);
        finish();
    }
}
