package com.example.project_prm.Adapter.User;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_prm.Activity.Admin.Product.DetailProductActivity;
import com.example.project_prm.Activity.User.Shop.DetailProductShopActivity;
import com.example.project_prm.Entities.Product;
import com.example.project_prm.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ProductViewHolder> {
    private List<Product> productList = new ArrayList<>();
    private List<Product> originalList = new ArrayList<>();
    private String currentQuery = "";
    private boolean isPriceAscending = true;
    private int selectedPosition = -1;
    private OnAddToCartClickListener addToCartListener;

    public interface OnAddToCartClickListener {
        void onAddToCartClick(Product product);
    }

    public ProductListAdapter(List<Product> productList, OnAddToCartClickListener addToCartListener) {

        if (productList != null) {
            this.productList = new ArrayList<>(productList);
            this.originalList = new ArrayList<>(productList);
        }
        this.addToCartListener = addToCartListener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Product product = productList.get(position);
        holder.name.setText(product.getName());
        holder.brand.setText(product.getBrand());
        holder.price.setText(String.format("%,.0f VND", product.getSale_price()));

        if (product.getImage() != null && !product.getImage().isEmpty()) {
            File imgFile = new File(product.getImage());
            if (imgFile.exists()) {
                Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                holder.imageViewProduct.setImageBitmap(bitmap);
            } else {
                holder.imageViewProduct.setImageResource(R.drawable.img_avatar);
            }
        } else {
            holder.imageViewProduct.setImageResource(R.drawable.img_avatar);
        }

        holder.btnAddToCart.setOnClickListener(v -> addToCartListener.onAddToCartClick(product));
        holder.itemView.setOnClickListener(v -> {
            Context context = v.getContext();
            Intent intent = new Intent(context, DetailProductShopActivity.class);
            intent.putExtra("PRODUCT_ID", product.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public void setProductList(List<Product> products) {
        this.originalList = new ArrayList<>(products);
        applyFilters();
    }

    public void filterByName(String query) {
        this.currentQuery = query.toLowerCase();
        applyFilters();
    }

    public void sortByPrice(boolean ascending) {
        this.isPriceAscending = ascending;
        applyFilters();
    }

    private void applyFilters() {
        List<Product> filteredList = new ArrayList<>();
        for (Product product : originalList) {
            if (product.getName().toLowerCase().contains(currentQuery)) {
                filteredList.add(product);
            }
        }
        if (isPriceAscending) {
            Collections.sort(filteredList, Comparator.comparingDouble(Product::getSale_price));
        } else {
            filteredList.sort((p1, p2) -> Double.compare(p2.getSale_price(), p1.getSale_price()));
        }
        productList = filteredList;
        notifyDataSetChanged();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView name, price, brand;
        ImageView imageViewProduct;

        ImageButton btnAddToCart;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewProduct = itemView.findViewById(R.id.imageView);
            name = itemView.findViewById(R.id.tvName);
            brand = itemView.findViewById(R.id.tvBrand);
            price = itemView.findViewById(R.id.tvPrice);

            btnAddToCart = itemView.findViewById(R.id.btnAddToCart);
        }
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    public Product getProductAt(int position) {
        return productList.get(position);
    }

}
